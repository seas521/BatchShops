package com.if2c.harald.job;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.if2c.pt.priceterminator.PriceTerminator;

@Component
public class AutoGoodsPriceAndFreightJob extends JobBase {

	static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(PromotionStatusJob.class);
	SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");

	public void run() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			autoConfirmAuto(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}
		logger.info("结束");
	}

	public void autoConfirmAuto(Connection conn) throws SQLException {
		PreparedStatement autoComments = null;
		try {
			String sqlSpu = "select gs.id,seller.`type`  from goods_series gs "
					+ " left join shop on shop.id=gs.shop_id "
					+ " left join seller on seller.id=shop.seller_id "
					+ " where gs.is_virtual=1 and gs.status in (4,5)";
		 
			autoComments = conn.prepareStatement(sqlSpu);
			ResultSet autcommentOrder = autoComments.executeQuery();
			while (autcommentOrder.next()) {
				
				long id = autcommentOrder.getLong("id");
				try {
					int type = autcommentOrder.getInt("type");
					// 更新sku详情页价格
					if (type == 1 || type == 5) {
						
						PriceTerminator f = new PriceTerminator(conn, String.valueOf(id));
						List<Map<String,Object>> freghtType=f.getFreightPrice();
						updateGoodsSalePriceType(freghtType,conn);
					}
					else
					{
						PriceTerminator f = new PriceTerminator(conn, String.valueOf(id));
						List<Map<String,Object>> freghtType=f.getSalePrice();
						updateGoodsSalePrice(freghtType,conn);
					}
				
					// 更新spu:update_time
					updateGoodsSeriesTime(id, conn);
					conn.commit();

				} catch (Exception e) {
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					logger.info("spuId=" + id + "更新商品信息失败");
					e.printStackTrace();
				}
			}
		
		} catch (SQLException e) {

			e.printStackTrace();
		}

		finally {
			closePs(autoComments);
		}
	}

	
	public void updateGoodsSeriesTime(long id, Connection conn) {
		PreparedStatement updateGoodsSeriesTime = null;
		String sqlUpdateGoodsSeries = "UPDATE goods_series SET update_time=? WHERE id=?";

		
			SimpleDateFormat formatUpdateTime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
			 updateGoodsSeriesTime = conn
					.prepareStatement(sqlUpdateGoodsSeries);
			updateGoodsSeriesTime.setString(1,
					formatUpdateTime.format(new Date()));
			updateGoodsSeriesTime.setLong(2, id);
			updateGoodsSeriesTime.execute();
			
		} catch (SQLException e) {
			logger.info("spuId=" + id + "更新updateTime失败");
			e.printStackTrace();
		}
			finally {
				closePs(updateGoodsSeriesTime);
			}
	}
	public void updateGoodsSalePriceType( List<Map<String,Object>> freghtType, Connection conn)
			throws SQLException {
		PreparedStatement autoSkuType = null;
	try{
		// 更新商品价格
		String sqlUpdateGoods = "UPDATE goods SET sale_price=?,freight_price=?,tax=?,price=? WHERE id=?";

		for(int i=0;i<freghtType.size();i++)
		{
			 autoSkuType = conn.prepareStatement(sqlUpdateGoods);
				autoSkuType.setString(1, freghtType.get(i).get("salePrice").toString());
				autoSkuType.setString(2, freghtType.get(i).get("freight").toString());
				autoSkuType.setString(3, freghtType.get(i).get("taxPrice").toString());
				autoSkuType.setString(4, freghtType.get(i).get("ratePrice").toString());
				autoSkuType.setString(5, freghtType.get(i).get("goodsId").toString());
				 autoSkuType.execute();
				
		}
	
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
			finally {
				closePs(autoSkuType);
			
			}
		

		
	}
	
	public void updateGoodsSalePrice( List<Map<String,Object>> freghtType, Connection conn)
			throws SQLException {
		PreparedStatement autoSku = null;
		try{
	     // 更新商品价格
		String sqlUpdateGoods = "UPDATE goods SET sale_price=?,price=? WHERE id=?";
		for(int i=0;i<freghtType.size();i++)
		{
			autoSku = conn.prepareStatement(sqlUpdateGoods);
			autoSku.setString(1, freghtType.get(i).get("ratePrice").toString());
			autoSku.setString(2, freghtType.get(i).get("ratePrice").toString());
			autoSku.setString(3, freghtType.get(i).get("goodsId").toString());
			autoSku.execute();
				
		}
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
			finally {
				closePs(autoSku);
				
			}
		

		
	}

	
	public static void main(String[] args) throws Exception {
		/* AutoConfirmOrder obj = new AutoConfirmOrder(); */
		AutoGoodsPriceAndFreightJob obj = new AutoGoodsPriceAndFreightJob();
		obj.run();
		System.exit(0);
	}
}
