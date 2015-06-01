package com.if2c.harald.migration;

import java.io.File;
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
 * 迁移2C-V1R2B001 的商品图片存储规则 到 图片服务器。
 * 
 * @author Qian Bing <br>
 *         Created at 2013年10月9日
 */
public class GoodsImageMigrationTaskPatch1 extends ImageMigrationTask {

	private List<String> seriesNoImage = new ArrayList<String>();
	private List<String> goodsNoImage = new ArrayList<String>();
	private List<String> goodsNoSeries = new ArrayList<String>();

	public GoodsImageMigrationTaskPatch1() throws FileNotFoundException,
			IOException {
		super();
	}

	public void run() throws SQLException {
		conn = getConnection();
		List<Goods> goodsList = getAllGoods(conn);

		calcImageLocalPath(goodsList);

		save2TFS(goodsList);

		save2DB(conn, goodsList);
		printErrors();
		conn.close();
	}

	private void printErrors() {
		printListInfo("series no image error size", seriesNoImage);
		printListInfo("goods no image error size", goodsNoImage);
		printListInfo("goods no series error size", goodsNoSeries);
		printListInfo("save image to tfs fail size", saveImageFail);
	}

	private void save2DB(Connection conn, List<Goods> goodsList2)
			throws SQLException {
		conn.setAutoCommit(false);
		String sql2 = "UPDATE goods SET img=? WHERE id=?";
		PreparedStatement ps2 = conn.prepareStatement(sql2);

		for (Goods goods : goodsList2) {
			ps2.setString(1, goods.getTfsImage());
			ps2.setLong(2, goods.getId());
			ps2.addBatch();
		}
		ps2.executeBatch();
		conn.commit();
		ps2.close();
	}

	private void save2TFS(List<Goods> goodsList) {
		for (Goods goods : goodsList) {
			File file = new File(goods.getLocalImage());
			if (!file.exists()) {
				goodsNoImage.add("Goods " + goods.getId()
						+ " 's image not exist");
				continue;
			}
			String tfsImage = getTFSManager().saveFile(goods.getLocalImage(),
					null, IMAGE_SUFFIX);
			if (tfsImage == null) {
				saveImageFail.add("save Series " + goods.getId()
						+ " image to tsf failed. " + goods.getLocalImage());
			} else {
				tfsImage = tfsImage + IMAGE_SUFFIX;
				goods.setTfsImage(tfsImage);
			}
		}
	}

	private void calcImageLocalPath(List<Goods> goodsList) {
		for (Goods goods : goodsList) {
			String path = getGoodsImagePath(goods.getSeriesID(), goods.getId());
			goods.setLocalImage(path);
		}
	}

	private List<Goods> getAllGoods(Connection conn2) throws SQLException {
		String sql = "select id,series_id,img from goods where img is null";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		List<Goods> goodsList = new ArrayList<Goods>();
		while (rs.next()) {
			Goods goods = new Goods();
			goods.setId(rs.getLong("id"));
			goods.setSeriesID(rs.getLong("series_id"));
			goodsList.add(goods);
		}
		rs.close();
		ps.close();
		return goodsList;
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
		GoodsImageMigrationTaskPatch1 task = new GoodsImageMigrationTaskPatch1();
		 task.run();
		 System.exit(0);

		// System.out.println(task.getGoodsSeriesImagePath(1205));
		System.out.println(task.getGoodsImagePath(1750, 2385));
	}
}
