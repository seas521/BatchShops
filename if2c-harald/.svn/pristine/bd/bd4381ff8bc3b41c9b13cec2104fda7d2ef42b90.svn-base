package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.if2c.harald.tools.DateUtils;
@DisallowConcurrentExecution
public class PromotionStatusJob extends JobBase {

    static final Logger log = LoggerFactory.getLogger(PromotionStatusJob.class);
    
	@Override
	public void run() {
		Connection con = null;
		Statement stat = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			con = this.getConnection();
			stat = con.createStatement();
			con.setAutoCommit(false);
			//获得系统当前时间
			Date currentDate = DateUtils.strToDatehhmmss(DateUtils.getCurrentDateTime());
			//查询要执行定时任务的活动集合，活动状态为未开始，进行中状态的活动集合
			String selSql = "select id,status,type,r.date_from,r.date_to from promotion_rule  r where status in(1,2) order by id";
			try {
				ps = con.prepareStatement(selSql);
				rs = ps.executeQuery();
				while(rs.next()){
					long promotionRuleId=rs.getLong("id");
					int status=rs.getInt("status");
					int type = rs.getInt("type");
					Date dateFrom = rs.getTimestamp("date_from");
					Date dateTo = rs.getTimestamp("date_to");
					//活动要更新的状态
					int changeStatus= 0;
					//根据时间与状态更新活动状态
					//如果状态为未开始1，且当前时间大于等于促销开始时间并且小于等于促销结束时间，活动更新为进行中2.如果时间大于结束时间更新状态为结束3
					//如果状态为进行中2，且当前时间大于结束时间，活动状态更新为已结束3
					//单品和满减促销状态与促销活动同步，秒杀团购需要根据不同spu设置的促销时间进行判断
					if(type==1||type==2){
						changeStatus = getChangeStatus(currentDate, status,
								dateFrom, dateTo, changeStatus);
						String updateSql = "update promotion_rule set status="+changeStatus+", audit_time = now() where id="+promotionRuleId;
						//这里面status加了条件应为参加促销活动的商品可以部分通过，部分不通过。
						String updateSql1="update promoting_brand_category_goods set status="+changeStatus+" where promotion_rule_id="+promotionRuleId+" and status in(1,2)";
						//有状态改变的活动才去更新活动
						if(changeStatus!=0){
							stat.executeUpdate(updateSql);
							stat.executeUpdate(updateSql1);
							log.info("Updating promotion_rule promotion_rule_id=["+promotionRuleId+"]......");
							log.info("Updating promoting_brand_category_goods promotion_rule_id=["+promotionRuleId+"]......");
						}
					}else{
						changeStatus = getChangeStatus(currentDate, status,
								dateFrom, dateTo, changeStatus);
						String updateSql = "update promotion_rule set status="+changeStatus+", audit_time = now() where id="+promotionRuleId;
						//有状态改变的活动才去更新活动
						if(changeStatus!=0){
							stat.executeUpdate(updateSql);
							log.info("Updating promotion_rule promotion_rule_id=["+promotionRuleId+"]......");
						}
						//查询秒杀，团购对应商品集合
						String selSql2 = "select pg.id,pg.`status`,pg.date_from,pg.date_to from promoting_brand_category_goods pg where  pg.`status` in(1,2) and pg.promotion_rule_id = ? and date_from is not null and date_to is not null order by pg.id";
						ps2 = con.prepareStatement(selSql2);
						ps2.setLong(1, promotionRuleId);
						rs2 = ps2.executeQuery();
						while(rs2.next()){
							int id = rs2.getInt("id");
							int statusGood = rs2.getInt("status");
							Date dateFromGood = rs2.getTimestamp("date_from");
							Date dateToGood = rs2.getTimestamp("date_to");
							changeStatus = 0;
							changeStatus = getChangeStatus(currentDate, statusGood,
									dateFromGood, dateToGood, changeStatus);
							if(changeStatus!=0){
								String  uSql = "update promoting_brand_category_goods set `status` = " + changeStatus + " where id = "+id;
								stat.executeUpdate(uSql);
								log.info("Updating promoting_brand_category_goods id =["+id+"] and promotion_rule_id=["+promotionRuleId+"]......");
							}
						}
					}
				}
			} catch (Exception e) {
				error(e.getMessage(), e);
				con.rollback();
			}
		} catch (SQLException e) {
			error(e.getMessage(), e);
		} finally {
			closeRs(rs);
			closeRs(rs2);
			closePs(ps);
			closePs(ps2);
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

	public int getChangeStatus(Date currentDate, int status, Date dateFrom,
			Date dateTo, int changeStatus) {
		// 1未开始， 2 进行中
		if(status==1){
			if(dateFrom.equals(currentDate)){
				changeStatus = 2;
			}
			if(dateFrom.before(currentDate)&&dateTo.after(currentDate)){
				changeStatus = 2;
			}
			if(dateTo.before(currentDate)){
				changeStatus = 3;
			}
		}
		if(status==2){
			if(dateTo.before(currentDate)){
				changeStatus = 3;
			}
		}
		return changeStatus;
	}
	
	public static void main(String[] args) {
		PromotionStatusJob job = new PromotionStatusJob();
		//int ss = job.getChangeStatus(DateUtils.strToDatehhmmss("2015-09-22 16:15:15"), 1, DateUtils.strToDatehhmmss("2014-09-22 16:15:15"), DateUtils.strToDatehhmmss("2015-08-20 16:15:15"), 0);
		job.run();
		System.exit(0);
	}
}
