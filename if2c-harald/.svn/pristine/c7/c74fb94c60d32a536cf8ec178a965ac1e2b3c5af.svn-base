package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.PreparedStatement;
import com.tenpay.RequestHandler;
import com.tenpay.client.ClientResponseHandler;
import com.tenpay.client.TenpayHttpClient;

public class AutoUpdateExchangeRateJob extends JobBase{

	 static final Logger logger = LoggerFactory.getLogger(PromotionStatusJob.class);
	 
	 private static String partner = "1213027301";
	    // 密钥
	    private static String key = "32ae3b1544df2453d264cc1d14410aed";
	    // 网关url
	    private static String gateUrl = "https://gw.tenpay.com/intl/gateway/exchangeratequery.xml";
	    private RequestHandler reqHandler = new RequestHandler(null, null);
	    // 通信对象
	    private TenpayHttpClient httpClient = new TenpayHttpClient();
	    // 应答对象
	    private ClientResponseHandler resHandler = new ClientResponseHandler();
	    
	public void run(){
		Connection conn=null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			autoUpdateExchangeRate(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}
	}
	
	
	  public void autoUpdateExchangeRate(Connection conn) throws SQLException {
	        // 查询正在使用的币种
	        ResultSet currencyExchangeRs = null;
	        java.sql.PreparedStatement currentExchageRatePst = null;
	        java.sql.PreparedStatement exchangeRateListPst = null;
	        java.sql.PreparedStatement updateCurrentExchangeRatePst = null;
	        java.sql.PreparedStatement updateExchangeRateUseHistoryStatusPst = null;
	        java.sql.PreparedStatement insertExchangeRateUseHistoryPst = null;
	        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	        Date date = new Date();
	        try {
	            currentExchageRatePst =conn.prepareStatement("select currency,exchange_rate from current_exchange_rate");
	            exchangeRateListPst =  conn.prepareStatement("replace into exchange_rate_list(currency,create_time,exchange)values(?,?,?)");
	            updateCurrentExchangeRatePst = conn.prepareStatement("update current_exchange_rate set exchange_rate=?  where currency=?");
	            updateExchangeRateUseHistoryStatusPst = conn.prepareStatement("update exchange_rate_use_history set status=0 where currency_type=?");
	            insertExchangeRateUseHistoryPst =  conn.prepareStatement("insert into exchange_rate_use_history(currency_type,current_exchange,pre_exchange)values(?,?,?)");
	            currencyExchangeRs = currentExchageRatePst.executeQuery();
	            while (currencyExchangeRs.next()) {
	                String feeType = currencyExchangeRs.getString("currency");
	                double currentExchangeRate = currencyExchangeRs.getDouble("exchange_rate");
	                if ("CNY".equals(feeType)) {
	                    continue;
	                }
	                double minExchangeRate = currentExchangeRate - 0.02;
	                double maxExchangeRate = currentExchangeRate + 0.02;
	                double exchangeRate = getRateByRemoteInterface(feeType);
	                double currenyExchangeRate = exchangeRate / 100;
	                // 远程获取的汇率小于或大于数据库取的现有汇率+0.02时更新数据库
	                exchangeRateListPst.setString(1, feeType);
	                exchangeRateListPst.setString(2, dateFormatter.format(date));
	                exchangeRateListPst.setDouble(3, currenyExchangeRate);
	                exchangeRateListPst.addBatch();

	                if (minExchangeRate > currenyExchangeRate || maxExchangeRate < currenyExchangeRate) {
	                    updateCurrentExchangeRatePst.setDouble(1, currenyExchangeRate);
	                    updateCurrentExchangeRatePst.setString(2, feeType);
	                    updateCurrentExchangeRatePst.addBatch();
	                    // 修改当前汇率有效状态
	                    updateExchangeRateUseHistoryStatusPst.setString(1, feeType);
	                    updateExchangeRateUseHistoryStatusPst.addBatch();

	                    // 写入当前汇率
	                    insertExchangeRateUseHistoryPst.setString(1, feeType);
	                    insertExchangeRateUseHistoryPst.setDouble(2, currenyExchangeRate);
	                    insertExchangeRateUseHistoryPst.setDouble(3, currentExchangeRate);
	                    insertExchangeRateUseHistoryPst.addBatch();
	                }
	            }
	             
	            //批处理提交数据
	            updateCurrentExchangeRatePst.executeBatch();
	            exchangeRateListPst.executeBatch();
	            updateExchangeRateUseHistoryStatusPst.executeBatch();
	            insertExchangeRateUseHistoryPst.executeBatch();
	            conn.commit();
	        } catch (Exception e) {
	            try {
	                conn.rollback();
	            } catch (SQLException e1) {
	                logger.error(e1.getMessage(), e1);
	            }
	        } finally {
	            try {
	                if (currentExchageRatePst != null) {
	                    currentExchageRatePst.close();
	                }
	                if (updateCurrentExchangeRatePst != null) {
	                    updateCurrentExchangeRatePst.close();
	                }
	                if (updateExchangeRateUseHistoryStatusPst != null) {
	                    updateExchangeRateUseHistoryStatusPst.close();
	                }
	                if (insertExchangeRateUseHistoryPst != null) {
	                    insertExchangeRateUseHistoryPst.close();
	                }
	                if (currencyExchangeRs != null) {
	                    currencyExchangeRs.close();
	                }
	            } catch (Exception e2) {
	                logger.error(e2.getMessage(), e2);
	            }
	        }
	    }

	
	
	
	  private double getRateByRemoteInterface(String feeType) throws Exception {
	        // 设置查询请求参数
	        reqHandler.init();
	        reqHandler.setKey(key);
	        reqHandler.setGateUrl(gateUrl);
	        // 设置查询请求参数
	        reqHandler.setParameter("partner", partner); // 商户号
	        reqHandler.setParameter("fee_type", feeType); // USD,EUR,RMB
	        // 通信参数设置
	        httpClient.setTimeOut(500);
	        httpClient.setMethod("POST");
	        // 设置请求内容
	        String requestUrl = reqHandler.getRequestURL();
	        httpClient.setReqContent(requestUrl);
	        logger.info("getRateByRemoteInterface:" + requestUrl);
	        boolean isCallSuccess = httpClient.call();
	        double sellingPrice = 0;
	        if (isCallSuccess) {
	            String rescontent = httpClient.getResContent();
	            resHandler.setContent(rescontent);
	            resHandler.setKey(key);
	            String retcode = resHandler.getParameter("retcode");
	            if (resHandler.isTenpaySign() && "0".equals(retcode)) {
	                sellingPrice = Double.valueOf(resHandler.getParameter("selling_price"));
	            }
	        }
	        return sellingPrice;

	    }
	

	public static void main(String[] args) throws Exception {
		AutoUpdateExchangeRateJob autoUpdateExchangeRateJob = new AutoUpdateExchangeRateJob();
		autoUpdateExchangeRateJob.run();
			System.exit(0);
		}
	
}
