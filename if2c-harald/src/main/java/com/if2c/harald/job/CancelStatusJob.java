package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;

import com.if2c.harald.Enums.AuditStatusEunm;
import com.if2c.harald.Enums.CancelType;
import com.if2c.harald.exception.DateIsNullException;
import com.if2c.harald.tools.DateUtils;
import com.if2c.harald.tools.Security;
@DisallowConcurrentExecution
public class CancelStatusJob extends JobBase {

	@Override
	public void run() {
		try {
			 String today = DateUtils.convertDate2String(new Date());
			//String today = DateUtils.convertDate2StringWithHMS(new Date());
			// 退款申请：请按时审核申请，5天内如未处理，系统则自动审核通过，IF2C会退款给买家
			// 退货申请：请按时审核申请，5天内如未处理，系统则自动审核通过，买家可填写快递单号，货物邮寄回给商家
			String cancelSelectSql1 = "select * from sale_after_info "
					 + "where adddate(DATE_FORMAT(create_time,'%Y-%m-%d'),5)<'"
					//+ "where DATE_ADD(DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),interval 5 MINUTE)<'"
					+ today + "' and auditing_status="
					+ AuditStatusEunm.SELLERAUDITING.getCode()
					+ " and after_sale_type in(1,2)";
			opt(cancelSelectSql1, AuditStatusEunm.SELLERAUDITED.getCode());
			// 退货申请已审核通过后：买家可在7天内填写快递公司和快递单号后，买家（用户）发货后，商家30天未确认收货，F2C退款给买家
			String cancelSelectSql2 = "select * from sale_after_info "
					 + "where adddate(DATE_FORMAT(create_time,'%Y-%m-%d'),30)<'"
					//+ "where DATE_ADD(DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),interval 30 MINUTE)<'"
					+ today + "' and auditing_status="
					+ AuditStatusEunm.SELLERRECEIVING.getCode()
					+ " and after_sale_type in(1)";
			opt(cancelSelectSql2, AuditStatusEunm.SELLERRECEIVED.getCode());
			// 退货退款流程中，在商家审核不通过，7天内用户没有提出投诉，此次售后服务结束
			String cancelSelectSql3 = "select * from sale_after_info "
					 + "where adddate(DATE_FORMAT(create_time,'%Y-%m-%d'),7)<'"
					//+ "where DATE_ADD(DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),interval 7 MINUTE)<'"
					+ today + "' and auditing_status="
					+ AuditStatusEunm.SELLERNOTPASS.getCode()
					+ " and after_sale_type in(1,2)";
			opt(cancelSelectSql3,
					AuditStatusEunm.NOTREFUNDCOMPLETEDLY.getCode());
			// 退货流程中，在商家审核通过或者平台审核通过，7天内未填写物流信息，此次售后服务结束。
			String cancelSelectSql4 = "select * from sale_after_info "
					 + "where adddate(DATE_FORMAT(create_time,'%Y-%m-%d'),7)<'"
					//+ "where DATE_ADD(DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),interval 7 MINUTE)<'"
					+ today + "' and auditing_status in("
					+ AuditStatusEunm.SELLERAUDITED.getCode() + ","
					+ AuditStatusEunm.PLATAUDITED.getCode() + ")"
					+ " and after_sale_type in(1)";
			opt(cancelSelectSql4,
					AuditStatusEunm.NOTREFUNDCOMPLETEDLY.getCode());
		} catch (SQLException e) {
			error(e.getMessage(), e);
		} catch (DateIsNullException e) {
			error(e.getMessage(), e);
		}
	}

	public void opt(String sql, int status) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = this.getConnection();
		if(con==null){
			error("获得的Connection==null",new NullPointerException());
			return;
		}
		Statement stat = con.createStatement();
		con.setAutoCommit(false);
		String now;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			con.setAutoCommit(false);
			while (rs.next()) {
				long id = rs.getLong("id");
				long orderId = rs.getLong("order_id");
				//丁大人说在财务退款的时候才生成礼品卡
				/*float giftcardMoney=rs.getFloat("giftcard_money");
				int causeType=rs.getInt("cause_type");
				long userId=rs.getLong("user_id");
				int afterSaleType=rs.getInt("after_sale_type");
				if(giftcardMoney>0){
					genGiftcard(giftcardMoney,causeType, userId, afterSaleType,orderId);
				}*/
				now = DateUtils.convertDate2StringWithHMS(new Date());
				String cancelUpdateSql = "update sale_after_info set auditing_status="
						+ status + " where id=" + id;
				stat.executeUpdate(cancelUpdateSql);
				String cancelInsertSql = "insert into sale_after_info_history(sale_after_info_id,auditing_status,op_info,op_desc,op_user,created_time,status) "
						+ "values("
						+ id
						+ ","
						+ status
						+ ",'系统自动处理','系统自动处理','3','" + now + "',1)";
				stat.executeUpdate(cancelInsertSql);
				if (status == AuditStatusEunm.SELLERAUDITED.getCode()) {
					int sign = addGoodsNumber(orderId);
					if (sign != 1) {
						con.rollback();
					}
				}
			}
		} catch (Exception e) {
			error(e.getMessage(), e);
			con.rollback();
		} finally {
			con.commit();
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 退款时要增加库存数量
	 * 
	 * @param orderId
	 */
	public int addGoodsNumber(long orderId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement stat = null;
		int sign = 0;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat=conn.createStatement();
			String sql1 = "select t.goods_id,t.goods_num from order_goods_relation t where t.order_id = "
					+ orderId;
			ps = conn.prepareStatement(sql1);
			rs = ps.executeQuery();
			while (rs.next()) {
				long goodsId = rs.getLong("goods_id");
				long goodsNum = rs.getInt("goods_num");
				String sql2 = "update goods_inventory  set front_inventory = (front_inventory + "
						+ goodsNum
						+ "),"
						+ "virtual_inventory = (virtual_inventory+ "
						+ goodsNum
						+ ") where goods_id = " + goodsId;
				sign = stat.executeUpdate(sql2);
			}
		} catch (SQLException e) {
			error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				if(conn!=null)
				conn.commit();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
		}
		return sign;
	}
	
	/**
	 * 生成礼品卡等操作
	 * @param giftcardMoney 礼品卡金额
	 * @param causeType 投诉类型：1质量原因，2非质量原因
	 * @param userId
	 * @param afterSaleType 售后类型 1退货， 2退款
	 */
	public void genGiftcard(float giftcardMoney,int causeType,long userId,int afterSaleType,long orderId){
		Connection conn = null;
		PreparedStatement ps = null;
		Statement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat=conn.createStatement();
			//插入giftcard_batch
			String card=CancelType.getCancelType(afterSaleType).getDesc()+"礼品卡"+DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");;
			String genGiftCardBatchSql = "insert into giftcard_batch(`name`,`number`,`date_from`,`sum`,`use`) values(?,?,?,?,?)";
			ps = conn.prepareStatement(genGiftCardBatchSql,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, card);
			ps.setInt(2, 1);
			ps.setString(3, DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			ps.setDouble(4, giftcardMoney);
			ps.setInt(5, CancelType.getCancelType(afterSaleType).getGiftcardCode());
			long giftcardBatchId=saveAndGetPK(ps,"giftcard_batch");
			//System.out.println("giftcardBatchId:"+giftcardBatchId);
			//插入giftcard
	        String card_number = "VIP_"
					+ Security.getRandomString(10);
			String password = Security.getGiftcardPassword();
	        String genGiftCardSql = "insert into giftcard(`card_number`,`password`,`created_time`,`date_from`,`sum`,`batch_id`,`use`) " +
	        		"values(?,?,?,?,?,?,?)";
	        ps = conn.prepareStatement(genGiftCardSql,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, card_number);
			ps.setString(2, password);
			ps.setString(3, DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			ps.setString(4, DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			ps.setDouble(5, giftcardMoney);
			ps.setLong(6, giftcardBatchId);
			ps.setInt(7, CancelType.getCancelType(afterSaleType).getGiftcardCode());
			long giftcardId=saveAndGetPK(ps,"giftcard");
			//System.out.println("giftcardId:"+giftcardId);
			conn.commit();
	        //插入order_giftcard_batch_relation
			String genOrderGiftCardBatchSql = "insert into order_giftcard_batch_relation(`order_id`,`giftcard_batch_id`,`create_Time`) " +
	        		"values(?,?,?)";
	        ps = conn.prepareStatement(genOrderGiftCardBatchSql);
			ps.setLong(1, orderId);
			ps.setLong(2, giftcardBatchId);
			ps.setString(3, DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			ps.executeUpdate();
			//插入user_giftcard_relation
			String genUserGiftCardRelationSql = "insert into user_giftcard_relation(`user_id`,`giftcard_id`,`create_Time`) " +
	        		"values(?,?,?)";
	        ps = conn.prepareStatement(genUserGiftCardRelationSql);
			ps.setLong(1, userId);
			ps.setLong(2, giftcardId);
			ps.setString(3, DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			ps.executeUpdate();
		} catch (SQLException e) {
			error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				if(conn!=null)
				conn.commit();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
		}
	}
	
	public long saveAndGetPK(PreparedStatement ps,String tableName) {
		ResultSet generatedKeys = null;
		long giftcardBatchId = 0;
		try {
			/*DatabaseMetaData dbMetaData=ps.getConnection().getMetaData();
			ResultSet tableSet = dbMetaData.getTables(null, "%", "%", new String[]{"TABLE"});
			String tableName = tableSet.getString("TABLE_NAME");*/
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException(
						"Creating "+tableName+" failed, no rows affected.");
			}
			generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				giftcardBatchId = generatedKeys.getLong(1);
			} else {
				throw new SQLException(
						"Creating "+tableName+" failed, no generated key obtained.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return giftcardBatchId;
	}
}
