package com.if2c.harald.job;

import java.sql.SQLException;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;

import com.if2c.harald.Enums.AuditStatusEunm;
import com.if2c.harald.Enums.CancelType;
import com.if2c.harald.exception.DateIsNullException;
import com.if2c.harald.tools.DateUtils;
@DisallowConcurrentExecution
public class MoneySaleAfterJob extends JobBase {

    @Override
    public void run() {

        try {
             String today = DateUtils.convertDate2StringWithHMS(new Date());
            // 退款申请后商家3天内如未处理，系统则自动审核通过，退款给买家，订单状态改为交易关闭，售后单改为完成退款
            String cancelSelectSql1 = "select * from sale_after_info "
                     + "where adddate(DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),3)<='"
                    + today + "' and auditing_status="
                    + AuditStatusEunm.SELLERAUDITING.getCode()
                    + " and after_sale_type in(2)";
            opt(cancelSelectSql1, AuditStatusEunm.SELLERAUDITED.getCode());
            opt(cancelSelectSql1, AuditStatusEunm.REFUNDCOMPLETEDLY.getCode(),CancelType.CANCELMONEY.getSaleCode());
         // 退款流程中，商家审核不通过，5天内用户没有申请维权，订单状态改为待发货,售后单改为完成未退款
            String cancelSelectSql3 = "select sai.* from sale_after_info sai, sale_after_info_history saih "
                     + " where sai.id=saih.sale_after_info_id " +
                     " and adddate(DATE_FORMAT(saih.created_time,'%Y-%m-%d %H:%i:%s'),5)<='"
                    + today + "' and saih.created_time =(SELECT MAX(s.created_time) from sale_after_info_history s WHERE s.sale_after_info_id=saih.sale_after_info_id) and saih.auditing_status="
                    + AuditStatusEunm.SELLERNOTPASS.getCode()
                    + " and sai.after_sale_type in(2)";
            opt(cancelSelectSql3,
                    AuditStatusEunm.NOTREFUNDCOMPLETEDLY.getCode());
        } catch (SQLException e) {
            error(e.getMessage(), e);
        } catch (DateIsNullException e) {
            error(e.getMessage(), e);
        }
    }
    
    public static void main(String[] args) {
        new MoneySaleAfterJob().run();
    }
    
}
