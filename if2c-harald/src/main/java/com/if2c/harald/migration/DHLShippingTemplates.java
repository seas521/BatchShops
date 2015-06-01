package com.if2c.harald.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;









import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.if2c.harald.beans.Giftcard;
import com.if2c.harald.tools.DesUtil;

/**
 * 迁移2C-V1R2B001 的商品图片存储规则 到 图片服务器。
 * 
 * @author Qian Bing <br>
 *         Created at 2013年10月9日
 */
public class DHLShippingTemplates extends ImageMigrationTask {
    private static final Logger logger = Logger.getLogger(DHLShippingTemplates.class);

	public DHLShippingTemplates() throws FileNotFoundException,
			IOException {
		super();
	}

	public void run() throws SQLException {
		conn = getConnection();
		conn.setAutoCommit(false);
		try {
			updateSellerTemplate(conn);
			updateGoodsTemplatePrice(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		conn.close();
		
		//calcImageLocalPath(goodsList);
		//save2TFS(goodsList);
		//printErrors();

	}
	public String notDhlTemplate(String deliverTypeCode,int countryId, double weight,String sql,Connection conn) throws SQLException{
		String priceTemplate="";
		PreparedStatement autoSql = conn.prepareStatement(sql);
		autoSql.setString(1, deliverTypeCode);
		autoSql.setString(2, deliverTypeCode);
		autoSql.setInt(3, countryId);
		autoSql.setDouble(4, weight);
		autoSql.setDouble(5, weight);
		autoSql.setDouble(6, weight);
		autoSql.setDouble(7, weight);
		ResultSet sqlResult = autoSql.executeQuery();
		
		while (sqlResult.next()){
		if ("0".endsWith(sqlResult.getString("freight"))) {
			BigDecimal freight = new BigDecimal(sqlResult.getDouble("continue_freight")*weight
					* (1f + sqlResult.getDouble("float")*0.01)).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			priceTemplate = freight.toString();
		} else {
			BigDecimal freight = new BigDecimal(sqlResult.getDouble("freight")
					* (1f + sqlResult.getDouble("float")*0.01)).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			priceTemplate = freight.toString();
		}
		break;
		}
		
		return priceTemplate;
	}
	private void updateSellerTemplate(Connection conn) throws Exception {
		//把战略直供的商家运费模板制成非默认
		String sqlUpdateFreight=" update freight_template,seller  set  freight_template.is_default=1 where freight_template.seller_id=seller.id and seller.`type` in (1,5) ";
		//把战略直供的商家
		String sqlSelectSellerId=" select seller.id from seller where seller.`type` in (1,5) ";
		//插入默认运费模板
		String sqlInsertTemplate=" insert into freight_template (name,price_type,notes,seller_id,status,is_default) values ('DHL运费模板',3,'',?,0,0) ";
		//插入默认运费模板计价方式
		String sqlInsertDeliver=" insert into deliver_type (deliver_type_code,zone_code,freight_id,price_float) values ('dhl',0,?,0) ";
		//查找spu商品
		String sqlSelectSpu="select gs.id from goods_series gs left join shop on shop.id=gs.shop_id  left join seller on seller.id=shop.seller_id where gs.is_virtual=1 and gs.status in (4,5) and seller.id =? ";
		//spu插入运费模板	
		String squUpdateSpu="update goods_series set freight_template_id= ? where id=? ";
		PreparedStatement sqlUpdateFreightResult = conn.prepareStatement(sqlUpdateFreight);
		sqlUpdateFreightResult.execute();
		PreparedStatement sqlSelectSellerIdResult = conn.prepareStatement(sqlSelectSellerId);
		ResultSet sqlSelectSellerIdrs = sqlSelectSellerIdResult.executeQuery();
		while (sqlSelectSellerIdrs.next()) {
			int templateId=0;
			try {
			PreparedStatement sqlInsertTemplateResult = conn.prepareStatement(sqlInsertTemplate);
			sqlInsertTemplateResult.setInt(1,sqlSelectSellerIdrs.getInt("id"));
			sqlInsertTemplateResult.execute();	
			 templateId = getLastInsertID();		
			PreparedStatement sqlInsertDeliverResult = conn.prepareStatement(sqlInsertDeliver);
			sqlInsertDeliverResult.setInt(1,templateId);
			sqlInsertDeliverResult.execute();	
			 logger.info("设置DHL运费模,商家ID="+sqlSelectSellerIdrs.getInt("id")+"  成功");
			}
			catch (SQLException e) {
				
				e.printStackTrace();
				 logger.info("设置DHL运费模,商家ID="+sqlSelectSellerIdrs.getInt("id")+"失败");
			}
			
			PreparedStatement sqlSpuResult = conn.prepareStatement(sqlSelectSpu);
			sqlSpuResult.setInt(1,sqlSelectSellerIdrs.getInt("id"));
			ResultSet SpuResult =sqlSpuResult.executeQuery();
			while (SpuResult.next()){
				try {
					PreparedStatement squUpdateSpuResult = conn.prepareStatement(squUpdateSpu);
					squUpdateSpuResult.setInt(1,templateId);
					squUpdateSpuResult.setLong(2,SpuResult.getLong("id"));
					squUpdateSpuResult.execute();	
					 logger.info("spu插入运费模板,spuID="+SpuResult.getLong("id")+"  成功");
					}
					catch (SQLException e) {
						
						e.printStackTrace();
						 logger.info("spu插入运费模板,spuID="+SpuResult.getLong("id")+"失败");
					}
			}
			 conn.commit();
			
		}
	}
private void updateGoodsTemplatePrice(Connection conn) throws Exception {
		
		PreparedStatement autoComments = null;
		//查询非dhl运费计价方式
		String sql = "select logistics_freight_new.*,(select fuel_float.float from  fuel_float where  "
				+ " fuel_float.deliver_type_code=? order by create_time desc limit 0,1 ) as 'float' from logistics_freight_new "
				+ " left join logistics_company_new on logistics_company_new.deliver_type_code=? "
				+ " left join logistics_area on logistics_area.logistics_id=logistics_company_new.id "
				+ " and logistics_area.country_id = ? "
				+ " where logistics_freight_new.logistics_area_id=logistics_area.id "
				+ "and ((logistics_freight_new.start_weight < ? AND logistics_freight_new.end_weight >= ?) "
				+ "OR (logistics_freight_new.start_weight <= ? AND logistics_freight_new.end_weight = 0) "
				+ "OR (logistics_freight_new.end_weight>?)) limit 0,1";
		//查找spu商品
		String sqlSpu="select gs.id,gs.category_id,gs.delivery_country_id,gs.currency_id,gs.freight_template_id,gs.length,gs.width,gs.height,gs.weight,seller.`type`  from goods_series gs "
				+ " left join shop on shop.id=gs.shop_id "
				+ " left join seller on seller.id=shop.seller_id "
				+ " where gs.is_virtual=1 and gs.status in (4,5) and seller.type in (1,5) ";
		//查找sku商品
		String sqlSku="select goods.id,goods.base_price from goods where goods.status in (4,5) "
				   + " and goods.active=1 and goods.series_id=? order by goods.base_price  ";
		//查找燃油浮动
	   String sqlFuel="select fuel_float.deliver_type_code,fuel_float.float from fuel_float ";
	   //查找税率
		   String sqlTp="select tax_point.category_id,tax_point.tax from tax_point";
	   //查找所有最新汇率
		String sqlRate="select * from exchange_rate_list where id in (select max(id) from exchange_rate_list group by currency order by create_time desc) ";
		//更新商品价格
		String sqlUpdateGoods = "UPDATE goods SET sale_price=? WHERE id=?";
		//更新商品运费模板
				String sqlUpdateGoodsTemplate = "UPDATE goods_series SET freight_template_id=? WHERE id=?";
		//删除spu的运费
		String sqldelFreight="delete from  goods_deliver_type where goods_deliver_type.goods_series_id=? ";
		//添加spu的运费
		String sqlInFreight="insert into goods_deliver_type (goods_series_id,deliver_type_id,freight) values (?,?,?)";
		//查询运费模板下的具体方法
		String sqlDeliverType="select dt.id,dt.deliver_type_code,dt.price_float from deliver_type dt where dt.freight_id=?";
		
		Map<String,Float> exchange = new HashMap<String,Float>();
		Map<Integer,Float> taxPoint = new HashMap<Integer,Float>();
		Map<String,Float> fuelFloat = new HashMap<String,Float>();
		
		PreparedStatement autoExchanges = conn.prepareStatement(sqlRate);
		ResultSet exchangResult = autoExchanges.executeQuery();
		
		while (exchangResult.next()){
			if(!"CNY".equals(exchangResult.getString("currency"))){
			exchange.put(exchangResult.getString("currency"), exchangResult.getFloat("exchange"));
			}
		}
		
		PreparedStatement autoTaxPoint = conn.prepareStatement(sqlTp);
		ResultSet taxPointResult = autoTaxPoint.executeQuery();
		
		while (taxPointResult.next()){
			taxPoint.put(taxPointResult.getInt("category_id"), taxPointResult.getFloat("tax"));
		}
		
		PreparedStatement autoFuelFloat = conn.prepareStatement(sqlFuel);
		ResultSet fuelFloatResult = autoFuelFloat.executeQuery();
		
		while (fuelFloatResult.next()){
			fuelFloat.put(fuelFloatResult.getString("deliver_type_code"), fuelFloatResult.getFloat("float"));
		}
		
	
		try {
			autoComments = conn.prepareStatement(sqlSpu);
			ResultSet autcommentOrder = autoComments.executeQuery();
			
			while (autcommentOrder.next()) {
				
				
				
				BigDecimal price = null;
				long id=autcommentOrder.getLong("id");
				try {
				
				String currencyType=autcommentOrder.getString("currency_id");
				int categoryId=autcommentOrder.getInt("category_id");
				int countryId=autcommentOrder.getInt("delivery_country_id");
				int freightTemplateId=autcommentOrder.getInt("freight_template_id");
				Double length=autcommentOrder.getDouble("length");
				Double width=autcommentOrder.getDouble("width");
				Double height=autcommentOrder.getDouble("height");
				Double weight=autcommentOrder.getDouble("weight");
				int type=autcommentOrder.getInt("type");
				float tax=taxPoint.get(categoryId);
				Double weights=length*width*height/5000 ;
				if(weights>weight){
					weight=weights;
				}
				
			
				
				PreparedStatement autoSku = conn.prepareStatement(sqlSku);
				autoSku.setLong(1,id);
				ResultSet autoSkuResult = autoSku.executeQuery();
				//更新sku详情页价格
				while (autoSkuResult.next()) {
					 price=new BigDecimal(autoSkuResult.getString("base_price"));
					if("CNY".equals(currencyType)){
						 tax = 1f;
						BigDecimal rate=new  BigDecimal(String.valueOf(tax));
						price.add(price.multiply(rate).multiply(new BigDecimal("1.02")));
					}
					else{
					BigDecimal rate=new  BigDecimal(exchange.get(currencyType));
					price=price.multiply(rate).multiply(new BigDecimal("1.02"));
					}
					/*try {
					PreparedStatement updateGoods = conn
							.prepareStatement(sqlUpdateGoods);
					updateGoods.setString(1, checkDecimal(price.toString()));
					updateGoods.setLong(2, autoSkuResult.getLong("id"));
					updateGoods.execute();
					//conn.commit();
					logger.info("skuId="+autoSkuResult.getLong("id") + "更新商品详情页价格"+checkDecimal(price.toString())+"成功");
					}
					catch (SQLException e) {
						logger.info("skuId="+autoSkuResult.getLong("id") + "更新商品详情页价格"+checkDecimal(price.toString())+"失败");
						e.printStackTrace();
					}*/
					
				}
				//更新运费
				if(type==1||type==5)
				{
					
					autoComments = conn.prepareStatement(sqldelFreight);
					autoComments.setLong(1,id);
					autoComments.execute();
					//conn.commit();
					autoComments = conn.prepareStatement(sqlDeliverType);
					autoComments.setInt(1,freightTemplateId);
					ResultSet autoDeliverType = autoComments.executeQuery();
					while (autoDeliverType.next()){
						String priceTemplate = "";
						int deliverTypeId=autoDeliverType.getInt("id");
						String deliverTypeCode=autoDeliverType.getString("deliver_type_code");
						int priceFloat=autoDeliverType.getInt("price_float");
						float fuel=fuelFloat.get(deliverTypeCode);
						 priceTemplate=notDhlTemplate(deliverTypeCode,countryId,weight,sql,conn);
					      String totalPrice="";
						 totalPrice=String.valueOf(Float.valueOf(priceTemplate)*(1+priceFloat*0.01)+(Float.valueOf(priceTemplate)*(1+priceFloat*0.01)+Float.valueOf(price.toString()))*tax*0.01);
						 int salePrice=Math.round(Float.valueOf(totalPrice));
						 try {
						 PreparedStatement ps2 = conn.prepareStatement(sqlInFreight);
						 ps2.setLong(1,id);
						 ps2.setInt(2,deliverTypeId);
						 ps2.setInt(3,salePrice);
						 ps2.execute();
						// conn.commit();
							logger.info("更新商品运费信息"+"spuID="+id+"deliverID="+deliverTypeId+"运费="+salePrice+",成功");
							}
							catch (SQLException e) {
								logger.info("更新商品运费信息"+"spuID="+id+"deliverID="+deliverTypeId+"运费="+salePrice+",失败");
								e.printStackTrace();
							}
					}
				
					
				}
				conn.commit();
				logger.info("spuId="+id+"更新商品信息成功");
				
				}
				catch (Exception e) {
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					logger.info("spuId="+id+"更新商品信息失败");
					e.printStackTrace();
				}
				
				
				
				
				}
				
			

			conn.close();
		} catch (SQLException e) {
			/*try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public int getLastInsertID() throws SQLException {
		String sql = "SELECT LAST_INSERT_ID() as last_id";
		PreparedStatement sqllastID = conn.prepareStatement(sql);
		ResultSet lastIdResult =sqllastID.executeQuery();
		int lastID=0;
		while (lastIdResult.next()) {
			lastID=lastIdResult.getInt("last_id");
		}
		return lastID;
    } 
	public Connection getConnection() {
		if (conn == null) {
			MySQLDB db = new MySQLDB(props.getProperty("database.url"),
					props.getProperty("database.username"),
					props.getProperty("database.password"));
			conn = db.getConnection();
		}

		return conn;
	}
	public static String checkDecimal(String value) {
		String temp = null;
		if (StringUtils.isNotBlank(value)) {
			if (value.contains(".")) {
				String result = value.substring(value.indexOf(".") + 1);
				if(result.length() > 2)
				{
					temp=value.substring(0, value.indexOf(".") + 3);
				}
				else if (result.length() > 1) {
					temp = value;
				} else if (result.length() > 0) {
					temp = value + "0";
				} else if (result.length() == 0) {
					temp = value + "00";
				}
			} else {
				temp = value + ".00";
			}
		}
		else
		{
			temp="0.00";	
		}
		return temp;
	}
	
	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		DHLShippingTemplates task = new DHLShippingTemplates();
		 task.run();
		 System.exit(0);
	}
}
