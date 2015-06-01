package com.if2c.harald.job;

import java.sql.SQLException;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;

import com.if2c.common.bean.SaleAfterModel;
import com.if2c.harald.Enums.AuditStatusEunm;
import com.if2c.harald.exception.DateIsNullException;
import com.if2c.harald.tools.DateUtils;
@DisallowConcurrentExecution
public class GoodsSaleAfterJob extends JobBase {

	@Override
	public void run() {
		try {
			 String today = DateUtils.convertDate2StringWithHMS(new Date());
			// 退货申请，3天内如未处理，系统则自动审核通过
			String cancelSelectSql1 = "select * from sale_after_info "
					 + "where adddate(DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),3)<='"
					+ today + "' and auditing_status="
					+ AuditStatusEunm.SELLERAUDITING.getCode()
					+ " and after_sale_type in(1)";
			opt(cancelSelectSql1, AuditStatusEunm.SELLERAUDITED.getCode(),SaleAfterModel.SaleAfterOptEnum.INSERTCANCELADDRESS);
			// 退货申请商家审核通过或客服审核通过后：买家7天内如未填写快递公司和快递单号，系统默认用户取消退货
			String cancelSelectSql2 = "select sai.* from sale_after_info sai, sale_after_info_history saih "
                    + " where sai.id=saih.sale_after_info_id " +
                    " and adddate(DATE_FORMAT(saih.created_time,'%Y-%m-%d %H:%i:%s'),7)<='"
                   + today + "' and saih.created_time =(SELECT MAX(s.created_time) from sale_after_info_history s WHERE s.sale_after_info_id=saih.sale_after_info_id) and saih.auditing_status in("
                   + AuditStatusEunm.SELLERAUDITED.getCode() + ","
                   + AuditStatusEunm.PLATAUDITED.getCode() + ")"
                   + " and sai.after_sale_type in(1)";
           opt(cancelSelectSql2,
                   AuditStatusEunm.USERCANCEL.getCode());
			// 退货流程中，在商家申请审核不通过，5天内用户没有申请维权，此次售后服务结束
			String cancelSelectSql3 = "select sai.* from sale_after_info sai, sale_after_info_history saih "
					 + "where sai.id=saih.sale_after_info_id " +
					 " and adddate(DATE_FORMAT(saih.created_time,'%Y-%m-%d %H:%i:%s'),5)<='"
					+ today + "' and saih.created_time =(SELECT MAX(s.created_time) from sale_after_info_history s WHERE s.sale_after_info_id=saih.sale_after_info_id) and saih.auditing_status in("
					+ AuditStatusEunm.SELLERNOTPASS.getCode()+ ") "
					+ " and sai.after_sale_type in(1)";
			opt(cancelSelectSql3,
					AuditStatusEunm.UNCOMPLETEGOODS.getCode());
			// 退货流程中，自用户填写物流单号之日起15天内，如果不操作审核，系统默认审核通过,等待财务退款。
			String cancelSelectSql4 = "select sai.* from sale_after_info sai, sale_after_info_history saih "
					 + "where sai.id=saih.sale_after_info_id " +
					 " and adddate(DATE_FORMAT(saih.created_time,'%Y-%m-%d %H:%i:%s'),15)<='"
					+ today + "' and saih.created_time =(SELECT MAX(s.created_time) from sale_after_info_history s WHERE s.sale_after_info_id=saih.sale_after_info_id) and saih.auditing_status ="
					+ AuditStatusEunm.SELLERRECEIVING.getCode()
					+ " and sai.after_sale_type in(1)";
			opt(cancelSelectSql4,
					AuditStatusEunm.SELLERRECEIVEDPASSED.getCode());
			//退货流程中，商家收货审核不通过，用户不维权或商家收货审核不通过客服收货审核不通过，商家3天内没寄出，等待财务退款
			String cancelSelectSql5 = "select sai.* from sale_after_info sai, sale_after_info_history saih "
                    + "where sai.id=saih.sale_after_info_id " +
                    " and adddate(DATE_FORMAT(saih.created_time,'%Y-%m-%d %H:%i:%s'),3)<='"
                   + today + "' and saih.created_time =(SELECT MAX(s.created_time) from sale_after_info_history s WHERE s.sale_after_info_id=saih.sale_after_info_id) and saih.auditing_status in("
                   + AuditStatusEunm.SELLERRECEIVEDNOTPASSUSERNOCOMPLAIN.getCode()+","+AuditStatusEunm.SELLERRECEIVEDNOTPASSPLATNOTPASS.getCode()+")"
                   + " and sai.after_sale_type in(1)";
           opt(cancelSelectSql5,
                   AuditStatusEunm.WAITTOREFUND.getCode());
         //退货流程中，15天内不操作确认收货按钮，系统默认自动收货
           String cancelSelectSql6 = "select sai.* from sale_after_info sai, sale_after_info_history saih "
                   + "where sai.id=saih.sale_after_info_id " +
                   " and adddate(DATE_FORMAT(saih.created_time,'%Y-%m-%d %H:%i:%s'),15)<='"
                  + today + "' and saih.created_time =(SELECT MAX(s.created_time) from sale_after_info_history s WHERE s.sale_after_info_id=saih.sale_after_info_id) and saih.auditing_status in("
                  + AuditStatusEunm.SELLERRECEIVEDNOTPASSSENDTOUSER.getCode()+")"
                  + " and sai.after_sale_type in(1)";
          opt(cancelSelectSql6,
                  AuditStatusEunm.UNCOMPLETEGOODS.getCode(),SaleAfterModel.SaleAfterOptEnum.CHECKORDERSTATUS);
       // 退货流程中，在商家收货审核不通过，5天内用户没有申请维权，等待商家发货
          String cancelSelectSql7 = "select sai.* from sale_after_info sai, sale_after_info_history saih "
                   + "where sai.id=saih.sale_after_info_id " +
                   " and adddate(DATE_FORMAT(saih.created_time,'%Y-%m-%d %H:%i:%s'),5)<='"
                  + today + "' and saih.created_time =(SELECT MAX(s.created_time) from sale_after_info_history s WHERE s.sale_after_info_id=saih.sale_after_info_id) and saih.auditing_status in("
                  + AuditStatusEunm.SELLERRECEIVEDNOTPASS.getCode()+") "
                  + " and sai.after_sale_type in(1)";
          opt(cancelSelectSql7,
                  AuditStatusEunm.SELLERRECEIVEDNOTPASSUSERNOCOMPLAIN.getCode());
		} catch (SQLException e) {
			error(e.getMessage(), e);
		} catch (DateIsNullException e) {
			error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
        new GoodsSaleAfterJob().run();
    }

}
