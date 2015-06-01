package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.if2c.harald.tools.DecimalUtil;

/**
 * 更新优惠券批次表coupon_batch中的优惠券使用总数，发出总数，使用率，成本，下单金额
 * @author shengqiang
 *
 * 2014-1-27
 */
@DisallowConcurrentExecution
public class CouponJob extends JobBase {
    static final Logger log=LoggerFactory.getLogger(CouponJob.class);
	@Override
	public void run() {
		Connection con = null;
		Statement stat = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con1 = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
		try {
			con = this.getConnection();
			con.setAutoCommit(false);
			con1=this.getConnection();
			con1.setAutoCommit(false);
			stat = con.createStatement();
			try {
				String sql = "SELECT c.batch_id, count(c.id) AS sendNumber, cb.sum FROM coupon_batch cb, coupon c WHERE c.batch_id = cb.id GROUP BY c.batch_id;";
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					long batchId = rs.getLong("batch_id");
					// 领取总数
					int sendNumber = rs.getInt("sendNumber");
					if(sendNumber==0L){
                        log.error("", new Exception("batch_id=["+batchId+"]'s sendNumber=0 ........"));
                        continue;
                    }
					//代金券或满减券金额
					Float sum= rs.getFloat("sum");
					String sql1="SELECT COUNT(oc.order_id) as useNumber from orders_coupon oc,coupon c,orders o " +
							" WHERE oc.coupon_id=c.id and oc.order_id=o.id and o.`status` in(2,3,4,5) and c.batch_id="+batchId;
					ps1 = con1.prepareStatement(sql1);
	                rs1 = ps1.executeQuery();
	                // 订单使用总数:订单（只统计已付款的订单，不统计退货或退款的售后单）中使用该批次的优惠券的总数
	                int useNumber=0;
	                while (rs1.next()) {
	                    useNumber=rs1.getInt("useNumber");
	                }
					// 使用率
					String useRate = DecimalUtil.decimalFormat((float)useNumber /(float)sendNumber);
					//成本=(代金券或满减券)*领取总数
					float totalCost=sum*sendNumber;
					String sql2 = "update coupon_batch set use_number="
							+ useNumber + ",send_number="+sendNumber+",use_rate=" + useRate + ",total_cost="+totalCost+" where id="
							+ batchId;
					stat.executeUpdate(sql2);
				}
				String sql2 = "SELECT oc.order_id as orderId,SUM(o.total_money) as totalMoney from orders o,orders_coupon oc " +
						" WHERE oc.order_id=o.id and o.`status` in(2,3,4,5) GROUP BY oc.order_id";
				ps = con.prepareStatement(sql2);
				rs = ps.executeQuery();
				while (rs.next()) { 
					// 下单金额：所有订单（只统计已付款的订单，不统计退货或退款的售后单）中的“商品总金额”的总和。
					long totalMoney = rs.getLong("totalMoney");
					long orderId=rs.getLong("orderId");
					String sql3 = "update coupon_batch set total_amount="+totalMoney+" where id in(select batch_id from coupon ,orders_coupon where coupon.id=coupon_id and orders_coupon.order_id="+orderId+");";
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
	public static void main(String[] args) {
        new CouponJob().run();
    }
}
