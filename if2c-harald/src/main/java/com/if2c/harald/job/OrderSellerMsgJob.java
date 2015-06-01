package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.if2c.harald.exception.DateIsNullException;
import com.if2c.harald.tools.DateUtils;
import com.if2c.sms.service.SMSService;

@DisallowConcurrentExecution
/**
 * 每天16：00给商家发送订单发货提醒短信
 * @author shengqiang
 *
 * 2014-8-22
 */
public class OrderSellerMsgJob extends JobBase {
    private Logger logger = LoggerFactory.getLogger(OrderSellerMsgJob.class);

    private void sendSellerMsgByOrder(){
        logger.info("start execute sendSellerMsgByOrder........");
        Connection conn = null;
        Connection conn1=null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            Calendar c = Calendar.getInstance();
            String today = DateUtils.convertDate2StringWithHMS(c.getTime());
            c.add(Calendar.DATE, -2);
            String beforeToday=DateUtils.convertDate2StringWithHMS(c.getTime());
            String sql="SELECT distinct DATE_FORMAT( o.payment_time, '%Y-%m-%d') as date,count(DISTINCT o.id) AS count,se.accountName as sellerName,o.order_num as orderNumber,u.name as username,se.contactPhone as contactPhone FROM orders o"+
                    " inner join user_address ua on o.user_address_id=ua.id "+
                    " inner join user u on ua.user_id=u.id "+
                    " INNER JOIN order_goods_relation ogr ON ogr.order_id = o.id "+
                    " INNER JOIN  goods g ON ogr.goods_id = g.id  "+
                    " INNER JOIN  shop sh ON g.shop_id = sh.id "+
                    " INNER JOIN  seller se ON sh.seller_id = se.id "+
                    " WHERE  o.status =2 and DATE_FORMAT(o.payment_time,'%Y-%m-%d %H:%i:%s')<='"+today+"' "
                            + "and DATE_FORMAT(o.payment_time,'%Y-%m-%d %H:%i:%s')>='"+beforeToday+"'";
            conn = this.getConnection();
            conn.setAutoCommit(false);
            if(null!=conn){
                ps=conn.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next()){
                    String sql1="SELECT content from sms_batch sb where sb.type=3";
                    conn1 = this.getConnection();
                    conn1.setAutoCommit(false);
                    ps1 =conn1.prepareStatement(sql1); 
                    rs1 = ps1.executeQuery();
                    String content="";
                    while(rs1.next()){
                        content = rs1.getString("content");
                    }
                    String dateArr[]=rs.getString("date").split("-");
                    String year=dateArr[0];
                    String month=dateArr[1];
                    String day=dateArr[2];
                    int count=rs.getInt("count");
                    String sellerName=rs.getString("sellerName");
                    String orderNumber=rs.getString("orderNumber");
                    String username= rs.getString("username");
                    String contactPhone=rs.getString("contactPhone");
                    content=StringUtils.replaceEach(content, new String[]{"[","]"}, new String[]{"",""});
                    if(StringUtils.contains(content, "username")){
                        content=StringUtils.replace(content, "username", username);
                    }
                    if(StringUtils.contains(content, "seller")){
                        content=StringUtils.replace(content, "seller", sellerName);
                    }
                    if(StringUtils.contains(content, "time")){
                        content=StringUtils.replace(content, "time", year+"年"+month+"月"+day+"日");
                    }
                    if(StringUtils.contains(content, "count")){
                        content=StringUtils.replace(content, "count", String.valueOf(count));
                    }
                    if(StringUtils.contains(content, "ordernumber")){
                        content=StringUtils.replace(content, "ordernumber", orderNumber);
                    }
                    SMSService sMSService=new SMSService();
                    String result=sMSService.sendCustomServiceSMS(contactPhone,content);
                    String[] str = result.split("\\:");
                    String isSuccess = str.length > 0 ? str[0] : "fail";
                    int status = "OK".equalsIgnoreCase(isSuccess) ? 1 : -1;
                    if(status!=1){
                        logger.error("Sending content=["+content+"] error...");
                    }
                }
            }
        } catch (SQLException | DateIsNullException e) {
            e.printStackTrace();
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs1 != null){
                try {
                    rs1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    error(e.getMessage(), e);
                }
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (SQLException e) {
                    error(e.getMessage(), e);
                }
            }
            if (conn != null) {
                try {
                    conn.commit();
                    conn.close();
                } catch (SQLException e) {
                    error(e.getMessage(), e);
                }
            }
            if (conn1 != null) {
                try {
                    conn1.commit();
                    conn1.close();
                } catch (SQLException e) {
                    error(e.getMessage(), e);
                }
            }
            
        }
    }
    
    @Override
    public void run() {
        sendSellerMsgByOrder();
    }

    
    public static void main(String[] args) {
        OrderSellerMsgJob emailTest=new OrderSellerMsgJob();
        emailTest.run();
        
    }
}
