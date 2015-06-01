package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.quartz.DisallowConcurrentExecution;

import com.if2c.harald.tools.DecimalUtil;

/**
 * 更新礼品卡批次表giftcard_batch中的优惠券使用总数，发出总数，使用率，成本，下单金额
 * 
 * @author shengqiang
 * 
 *         2014-02-12
 */
@DisallowConcurrentExecution
public class GiftCardJob extends JobBase {

	@Override
	public void run() {
		Connection con = null;
		Statement stat = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stat = con.createStatement();
			con.setAutoCommit(false);
			try {
				String sql1 = "select c.batch_id,count(cb.id) as useNumber,cb.number,cb.sum from giftcard_batch cb,giftcard c,orders_giftcard og "
						+ "where c.batch_id=cb.id  "
						+ "and og.giftcard_id=c.id "
						+ "and og.order_id is not null "
						+ "group by c.batch_id";
				ps = con.prepareStatement(sql1);
				rs = ps.executeQuery();
				while (rs.next()) {
					long batchId = rs.getLong("batch_id");
					// 使用总数：订单（只统计已付款待发货状态的订单，不统计退货或退款的售后单）中使用该批次的中的优惠券的总数
					long useNumber = rs.getInt("useNumber");
					// 发出总数
					long number = rs.getInt("number");
					// 使用率
					String useRate = DecimalUtil
							.decimalFormat((float) useNumber / (float) number);
					// 成本=(代金券或满减券)*发出总数
					float totalCost = rs.getFloat("sum") * number;
					String sql2 = "update giftcard_batch set use_number="
							+ useNumber + ",use_rate=" + useRate
							+ ",total_cost=" + totalCost + " where id="
							+ batchId;
					stat.executeUpdate(sql2);
				}
				String sql2 = "SELECT og.order_id, sum(o.total_money) AS totalAmount FROM orders o, orders_giftcard og "
						+ "WHERE o.id = og.order_id "
						+ "AND og.order_id IS NOT NULL "
						+ "GROUP BY og.order_id";
				ps = con.prepareStatement(sql2);
				rs = ps.executeQuery();
				while (rs.next()) {
					// 下单金额：所有订单（只统计已付款待发货状态的订单，不统计退货或退款的售后单）中的“商品总金额”的总和。
					long usedTotalCount = rs.getLong("totalAmount");
					long orderId = rs.getLong("order_id");
					String sql3 = "update giftcard_batch set total_amount="
							+ usedTotalCount
							+ "  WHERE id in(SELECT g.batch_id FROM giftcard g,orders_giftcard og where og.giftcard_id=g.id and og.order_id="
							+ orderId + ")";
					stat.executeUpdate(sql3);
				}
			} catch (Exception e) {
				error(e.getMessage(), e);
				con.rollback();
			}
		} catch (SQLException e) {
			error(e.getMessage(), e);
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.commit();
					con.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
		}
	}

}
