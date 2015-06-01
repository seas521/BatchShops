package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.if2c.harald.beans.Goods;
import com.if2c.harald.beans.GoodsSeries;

/**
 * 生成sku spu 的 标识列。
 * 
 * @author Qian Bing <br>
 *         Created at 2013年10月9日
 */
public class GoodsIdentificationTask extends ImageMigrationTask {

	public GoodsIdentificationTask() throws FileNotFoundException,
			IOException {
		super();
	}

	public void run() throws SQLException {
		conn = getConnection();
		List<Goods> goodsList = getAllGoods(conn);

		save2DB(conn, goodsList);
		conn.close();
		
		//calcImageLocalPath(goodsList);
		//save2TFS(goodsList);
		//printErrors();

	}


	private void save2DB(Connection conn, List<Goods> goodsList2)
			throws SQLException {
		conn.setAutoCommit(false);
		String sql2 = "UPDATE goods SET identification=? WHERE id=?";
		PreparedStatement ps2 = conn.prepareStatement(sql2);

		for (Goods goods : goodsList2) {
			if(goods.getIdentification()!=null){
			ps2.setString(1, goods.getIdentification()+goods.getId());
			ps2.setLong(2, goods.getId());
			ps2.addBatch();}
		}
		ps2.executeBatch();
		String sql3 = "UPDATE goods_series SET identification=? WHERE id=?";
		PreparedStatement ps3 = conn.prepareStatement(sql3);

		for (Goods goods : goodsList2) {
			if(goods.getIdentification()!=null){
			ps3.setString(1, goods.getIdentification()+goods.getSeriesID());
			ps3.setLong(2, goods.getSeriesID());
			ps3.addBatch();}
		}
		ps3.executeBatch();
		conn.commit();
		ps2.close();
		ps3.close();
	}

	

	private List<Goods> getAllGoods(Connection conn2) throws SQLException {
		String sql = "select goods.series_id,goods.id,seller.trade,seller.accountName as name,seller.delivery_country_id,c2.parent_id,seller.city,seller.`type` from goods "+ 
				"left join brand on brand.id=goods.brand_id "+
	"left join category on category.id=goods.category_id "+
	"left join category c2 on c2.id=category.parent_id "+
	"left join shop_brand on shop_brand.brand_id=brand.id "+
	"left join shop on shop.id=shop_brand.shop_id "+
	"left join seller on seller.id=shop.seller_id";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List <Goods> noType = new ArrayList<Goods>();
		List<Goods> goodsList = new ArrayList<Goods>();
		while (rs.next()) {
			Goods goods = new Goods();
			goods.setId(rs.getLong("id"));
			goods.setTrade(rs.getInt("trade"));
			goods.setDeliverCountry(rs.getInt("delivery_country_id"));
			goods.setClass1categoryId(rs.getString("parent_id"));
			goods.setCity(rs.getInt("city"));
			goods.setType(rs.getInt("type"));
			goods.setName(rs.getString("name"));
			goods.setSeriesID(rs.getLong("series_id"));
			makeidentification(goods);
			if(goods.getIdentification()==null){
				noType.add(goods);
			}
			goodsList.add(goods);
		}
		rs.close();
		ps.close();
		if(noType.size()>0){
			System.out.println("以下商家没有设置商家类型");
		}
		for (int i = 0; i < noType.size(); i++) {
			System.out.println("name："+noType.get(i).getName()+" , id:"+noType.get(i).getSeriesID());
		}
		return goodsList;
	}
	private void  makeidentification(Goods goods){
		StringBuffer identification = new StringBuffer();
		if(goods.getTrade()==1){
			identification.append("10");
		}else {
			identification.append("11");
		}
		
		int city = goods.getCity();
		int type = goods.getType();
		if(!(city==0&&type==0)&&!(city!=0&&type!=0)){
		//if(true){
		// 出口商家
		if (city != 0) {
			//义乌 
			if(city==1){
				identification.append("01");
			}
			//克拉玛依
			else if(city==2){
				identification.append("02");
			}
			else if(city==3){
				identification.append("03");
				}
			else if(city==4){
				identification.append("04");
				}
			else if(city==5){
				identification.append("05");
				}else if(city==99){
					identification.append("99");
					}
			
		}
		// 进口商家
		else if (type != 0) {
			// 国外品牌
			if (type == 1) {
				identification.append("01");
			}
			// 国外代理
			else if (type == 2) {
				identification.append("02");
			}
			// 国内代理
			else if (type == 3) {
				identification.append("03");
			}
			// 代购
			else if (type == 4) {
				identification.append("04");
			}else {
				identification.append("xx");
			}
		}
		if(goods.getDeliverCountry()<10){
			identification.append("00"+goods.getDeliverCountry());
		}else{
			identification.append("0"+goods.getDeliverCountry());
		}
		String class1Id=goods.getClass1categoryId();
		identification.append(class1Id);
		/*System.out.println(identification.toString());*/
		
		goods.setIdentification(identification.toString());
		}
		
	};
	
	public Connection getConnection() {
		if (conn == null) {
			MySQLDB db = new MySQLDB(props.getProperty("database.url"),
					props.getProperty("database.username"),
					props.getProperty("database.password"));
			conn = db.getConnection();
		}

		return conn;
	}

	public List<GoodsSeries> getAllGoodsSeries(Connection conn)
			throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("select id,img_num from goods_series");
		ResultSet rs = ps.executeQuery();

		List<GoodsSeries> goodsSeriesList = new ArrayList<GoodsSeries>();
		while (rs.next()) {
			GoodsSeries goodsSeries = new GoodsSeries();
			goodsSeries.setId(rs.getLong("id"));
			goodsSeries.setImageNumber(rs.getInt("img_num"));
			goodsSeriesList.add(goodsSeries);
		}
		rs.close();
		ps.close();
		return goodsSeriesList;
	}
	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		GoodsIdentificationTask task = new GoodsIdentificationTask();
		 task.run();
		 System.exit(0);
	}
}
