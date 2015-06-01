package com.if2c.harald.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.if2c.harald.beans.Giftcard;
import com.if2c.harald.beans.Goods;
import com.if2c.harald.beans.GoodsSeries;
import com.if2c.harald.tools.DesUtil;

/**
 * 迁移2C-V1R2B001 的商品图片存储规则 到 图片服务器。
 * 
 * @author Qian Bing <br>
 *         Created at 2013年10月9日
 */
public class giftcardEncryptionTask extends ImageMigrationTask {

	public giftcardEncryptionTask() throws FileNotFoundException,
			IOException {
		super();
	}

	public void run() throws SQLException {
		conn = getConnection();
		List<Giftcard> GiftcardsList;
		try {
			GiftcardsList = getAllGiftcard(conn);
			updatePassWord(conn, GiftcardsList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		conn.close();
		
		//calcImageLocalPath(goodsList);
		//save2TFS(goodsList);
		//printErrors();

	}


	private void updatePassWord(Connection conn, List<Giftcard> GiftcardsList2)
			throws SQLException {
		if(GiftcardsList2!=null && GiftcardsList2.size()>0){
		conn.setAutoCommit(false);
		String sql2 = "UPDATE giftcard SET passWord=? WHERE id=?";
		PreparedStatement ps2 = conn.prepareStatement(sql2);

		for (Giftcard giftcard : GiftcardsList2) {
			ps2.setString(1, giftcard.getPassWord());
			ps2.setInt(2, giftcard.getId());
			ps2.addBatch();
		}
		ps2.executeBatch();
		conn.commit();
		ps2.close();
		}
	}

	

	private List<Giftcard> getAllGiftcard(Connection conn2) throws Exception {
		String sql = "select id,passWord from giftcard where   length(passWord)<21";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List <Giftcard> noType = new ArrayList<Giftcard>();
		List<Giftcard> GiftcardsList = new ArrayList<Giftcard>();
		while (rs.next()) {
			Giftcard giftcard = new Giftcard();	
			giftcard.setId(rs.getInt("id"));
			
				giftcard.setPassWord(DesUtil.encrypt(rs.getString("passWord"),"if2c20140217"));
				
			
			GiftcardsList.add(giftcard);
		}
		rs.close();
		ps.close();
		return GiftcardsList;
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

	public String getGoodsImagePath(long goodsSeriesID, long goodsID) {
		Long fileNum = goodsSeriesID / 100;
		String goodsPath = getImageLocation() + "goods" + File_Separator
				+ "goods" + File_Separator + fileNum + File_Separator;
		// sku图
		String goods = goodsPath + "goods_";
		String SkudetailBig = goods + goodsID + "_detail_big.jpg";
		return SkudetailBig;
	}

	public String[] getGoodsSeriesImagePath(long goodsSeriesID, int imgNunber) {
		String[] pathes = new String[imgNunber];

		Long fileNum = goodsSeriesID / 100;
		String seriesPath = getImageLocation() + "goods" + "/" + "goods_series"
				+ File_Separator + fileNum + File_Separator;

		// 主图大图
		String series = seriesPath + "series_";
		for (int i = 0; i < pathes.length; i++) {
			pathes[i] = series + goodsSeriesID + "_detail_big" + i + ".jpg";
		}

		return pathes;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		giftcardEncryptionTask task = new giftcardEncryptionTask();
		 task.run();
		 System.exit(0);
	}
}
