package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.if2c.harald.beans.GoodsRule;
import com.if2c.harald.tools.BackupLog;

public class PromotionInventoryJob extends JobBase {

	Connection conn = null;

	@Override
	public void run() {
		try {
			conn = this.getConnection();
			handleInventoryBegin();// 处理团购活动开始，从goods_inventory拉库存
			handleInventoryEnd();// 处理秒杀，团购活动结束，从促销表归还库存
		} catch (SQLException e) {
			BackupLog.logError("PromotionInventoryJob function run runs fail", e);
		} finally {
			closeConnection(conn);
			conn = null;
		}
	}

	// 处理团购活动开始，从goods_inventory拉库存
	private void handleInventoryBegin() {
		List<GoodsRule> list = new ArrayList<GoodsRule>();
		// 查询进行中团购的促销商品
		String sql = "select pg.goods_id,i.front_inventory inventory,pg.promotion_rule_id from promoting_brand_category_goods pg,promotion_rule p,goods_inventory i "
				+ " where p.id = pg.promotion_rule_id  and i.goods_id = pg.goods_id and p.type = 3 and  pg.goods_id !=0 "
				+ " and pg.`status` = 2 and pg.is_synchronized = 0";
		PreparedStatement ps = null;
		PreparedStatement prest1 = null;
		PreparedStatement prest2 = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					GoodsRule good = new GoodsRule();
					good.setId(rs.getLong("goods_id"));
					good.setInventory(rs.getInt("inventory"));
					good.setRuleId(rs.getInt("promotion_rule_id"));
					list.add(good);
				}
			}
			if (list != null && list.size() > 0) {
				// 如果存在团购活动处于开始状态且未同步库存的，需要同步库存
				conn.setAutoCommit(false);
				String updatesql = "update promoting_brand_category_goods set inventory = ?,is_synchronized = 1 where promotion_rule_id = ? and goods_id = ?";
				prest1 = conn.prepareStatement(updatesql,
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				for (GoodsRule goodsRule : list) {
					prest1.setInt(1, goodsRule.getInventory());
					prest1.setInt(2, goodsRule.getRuleId());
					prest1.setLong(3, goodsRule.getId());
					prest1.addBatch();
					BackupLog.logInfo("团购活动id为"+goodsRule.getRuleId()+"商品id为"+goodsRule.getId()+"从goods_inventory表取front_inventory库存"+goodsRule.getInventory()+",取完库存后，前台库存为0");
				}
				prest1.executeBatch();
				String uSql = "update `goods_inventory` SET front_inventory = 0  WHERE  `goods_id`= ? ";
				prest2 = conn.prepareStatement(uSql,
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				for (GoodsRule goodsRule : list) {
					prest2.setLong(1, goodsRule.getId());
					prest2.addBatch();
				}
				prest2.executeBatch();
				conn.commit();
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			BackupLog.logError("PromotionInventoryJob handleInventoryBegin run runs fail", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				BackupLog.logError("PromotionInventoryJob handleInventoryBegin run runs fail", e1);
			}
		} finally {
			closeRs(rs);
			closePs(ps);
			closePs(prest1);
			closePs(prest2);
		}
	}

	// 处理秒杀，团购活动结束，从促销表归还库存
	private void handleInventoryEnd() {
		List<GoodsRule> list = new ArrayList<GoodsRule>();
		String sql = "select pg.goods_id,pg.inventory,pg.promotion_rule_id from promoting_brand_category_goods pg,promotion_rule p "
				+ "where p.id = pg.promotion_rule_id and p.type in (3,4) and pg.inventory !=0 and pg.goods_id !=0  and pg.`status` = 3";
		PreparedStatement ps = null;
		PreparedStatement prest1 = null;
		PreparedStatement prest2 = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					GoodsRule good = new GoodsRule();
					good.setId(rs.getLong("goods_id"));
					good.setInventory(rs.getInt("inventory"));
					good.setRuleId(rs.getInt("promotion_rule_id"));
					list.add(good);
				}
				// 如果存在活动结束时未退换库存的商品，需要退库存（针对团购，秒杀）
				conn.setAutoCommit(false);
				String updateSql = "UPDATE `goods_inventory` SET front_inventory = front_inventory + ?  WHERE  `goods_id`= ? ";
				prest1 = conn.prepareStatement(updateSql,
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				for (GoodsRule goodsRule : list) {
					prest1.setInt(1, goodsRule.getInventory());
					prest1.setLong(2, goodsRule.getId());
					prest1.addBatch();
					BackupLog.logInfo("促销活动id为"+goodsRule.getRuleId()+"商品id为"+goodsRule.getId()+"从promoting_brand_category_goods表取剩余库存"+goodsRule.getInventory()+"归还库存,活动结束后促销库存为更新为0");
				}
				prest1.executeBatch();
				String uSql = "update `promoting_brand_category_goods` SET `inventory` = 0 WHERE  `promotion_rule_id`= ? and `status`= 3";
				prest2 = conn.prepareStatement(uSql,
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				for (GoodsRule goodsRule : list) {
					prest2.setLong(1, goodsRule.getRuleId());
					prest2.addBatch();
				}
				prest2.executeBatch();
				conn.commit();
			}
		} catch (SQLException e) {
			BackupLog.logError("PromotionInventoryJob handleInventoryEnd run runs fail", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				BackupLog.logError("PromotionInventoryJob handleInventoryEnd run runs fail", e1);
			}
		} finally {
			closeRs(rs);
			closePs(ps);
			closePs(prest1);
			closePs(prest2);
		}
	}

	public static void main(String[] args) {
		PromotionInventoryJob indexGoodsUpdateJob = new PromotionInventoryJob();
		indexGoodsUpdateJob.run();
		System.exit(0);
	}
}
