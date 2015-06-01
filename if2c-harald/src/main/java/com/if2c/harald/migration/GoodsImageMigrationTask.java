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

import com.if2c.harald.beans.Goods;
import com.if2c.harald.beans.GoodsSeries;

/**
 * 迁移2C-V1R2B001 的商品图片存储规则 到 图片服务器。
 * 
 * @author Qian Bing <br>
 *         Created at 2013年10月9日
 */
public class GoodsImageMigrationTask extends ImageMigrationTask {

	private List<String> seriesNoImage = new ArrayList<String>();
	private List<String> goodsNoImage = new ArrayList<String>();
	private List<String> goodsNoSeries = new ArrayList<String>();

	public GoodsImageMigrationTask() throws FileNotFoundException, IOException {
		super();
	}

	public void run() throws SQLException {
		conn = getConnection();
		List<GoodsSeries> goodsSeriesList = getAllGoodsSeries(conn);
		List<Goods> goodsList = getAllGoods(conn);

		convert2Tree(goodsSeriesList, goodsList);
		calcReusedImage(goodsSeriesList);
		calcImageLocalPath(goodsSeriesList);

		save2TFS(goodsSeriesList);

		save2DB(conn, goodsSeriesList);
		print(goodsSeriesList);
		printErrors();
		conn.close();
	}

	private void printErrors() {
		printListInfo("series no image error size", seriesNoImage);
		printListInfo("goods no image error size", goodsNoImage);
		printListInfo("goods no series error size", goodsNoSeries);
		printListInfo("save image to tfs fail size", saveImageFail);
	}

	private void save2DB(Connection conn, List<GoodsSeries> goodsSeriesList)
			throws SQLException {
		conn.setAutoCommit(false);
		String sql1 = "INSERT INTO goods_series_image (series_id, image_path, size_type,image_type) VALUES (?,?,?,?)";
		PreparedStatement ps1 = conn.prepareStatement(sql1);
		String sql2 = "UPDATE goods SET img=? WHERE id=?";
		PreparedStatement ps2 = conn.prepareStatement(sql2);
		for (GoodsSeries goodsSeries : goodsSeriesList) {
			try {
				// save goods series images
				String[] tfsImages = goodsSeries.getTfsImage();
				for (int i = 0; i < tfsImages.length; i++) {
					ps1.setLong(1, goodsSeries.getId());
					ps1.setString(2, tfsImages[i]);
					ps1.setString(3, "80x80");
					ps1.setString(4, String.valueOf(i + 1));

					ps1.addBatch();
				}
				ps1.executeBatch();

				if (goodsSeries.getGoods() != null) {
					// save goods images
					List<Goods> goodsList = goodsSeries.getGoods();
					for (int i = 0; i < goodsList.size(); i++) {
						ps2.setString(1, goodsList.get(i).getTfsImage());
						ps2.setLong(2, goodsList.get(i).getId());
						ps2.addBatch();
					}
					ps2.executeBatch();
				}

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		ps1.close();
		ps2.close();
	}

	private void save2TFS(List<GoodsSeries> goodsSeriesList) {
		for (GoodsSeries goodsSeries : goodsSeriesList) {
			// save goods series images
			String[] tfsImages = new String[goodsSeries.getLocalImage().length];
			for (int i = 0; i < goodsSeries.getLocalImage().length; i++) {
				File file = new File(goodsSeries.getLocalImage()[i]);
				if (!file.exists()) {
					seriesNoImage.add("GoodsSeries " + goodsSeries.getId()
							+ " 's image not exist");
					continue;
				}
				tfsImages[i] = getTFSManager().saveFile(
						goodsSeries.getLocalImage()[i], null, IMAGE_SUFFIX);
				if (tfsImages[i] == null) {
					saveImageFail.add("save Series " + goodsSeries.getId()
							+ " image to tsf failed. " + tfsImages[i]);
				} else {
					tfsImages[i] = tfsImages[i] + IMAGE_SUFFIX;
				}
			}
			goodsSeries.setTfsImage(tfsImages);

			if (goodsSeries.getGoods() == null) {
				continue;
			}
			for (Goods goods : goodsSeries.getGoods()) {
				if (goods.getReusedImageGoods() == null) {
					File file = new File(goods.getLocalImage());
					if (!file.exists()) {
						goodsNoImage.add("Goods " + goods.getId()
								+ " 's image not exist");
						continue;
					}
					String tfsImage = getTFSManager().saveFile(
							goods.getLocalImage(), null, IMAGE_SUFFIX);
					if (tfsImage == null) {
						saveImageFail.add("save Series " + goods.getId()
								+ " image to tsf failed. "
								+ goods.getLocalImage());
					} else {
						tfsImage = tfsImage + IMAGE_SUFFIX;
						goods.setTfsImage(tfsImage);
					}
				}
			}
		}
	}

	private void print(List<GoodsSeries> goodsSeriesList) {
		for (GoodsSeries goodsSeries : goodsSeriesList) {
			System.out.println(goodsSeries);
			if (goodsSeries.getGoods() == null) {
				System.out.println("problem: goods series "
						+ goodsSeries.getId() + " has no goods");
				continue;
			}
			for (Goods goods : goodsSeries.getGoods()) {
				System.out.println(goods);
			}
		}

	}

	private void calcImageLocalPath(List<GoodsSeries> goodsSeriesList) {
		for (GoodsSeries goodsSeries : goodsSeriesList) {
			String[] path = getGoodsSeriesImagePath(goodsSeries.getId(),
					goodsSeries.getImageNumber());
			goodsSeries.setLocalImage(path);

			if (goodsSeries.getGoods() == null) {
				continue;
			}
			for (Goods goods : goodsSeries.getGoods()) {
				String goodsPath = getGoodsImagePath(goodsSeries.getId(),
						goods.getId());
				goods.setLocalImage(goodsPath);
			}
		}
	}

	private void calcReusedImage(List<GoodsSeries> goodsSeriesList) {
		for (GoodsSeries goodsSeries : goodsSeriesList) {
			if (goodsSeries.getGoods() == null) {
				continue;
			}

			for (int i = 0; i < goodsSeries.getGoods().size(); i++) {
				if (i == 0) {
					continue;
				}
				Goods goods = goodsSeries.getGoods().get(i);
				Goods preGoods = goodsSeries.getGoods().get(i - 1);
				if (goods.canReuseImage(preGoods)) {
					goods.setReusedImageGoods(preGoods);
				}
			}
		}
	}

	private void convert2Tree(List<GoodsSeries> goodsSeriesList,
			List<Goods> goodsList) {
		Map<Long, GoodsSeries> map = new LinkedHashMap<Long, GoodsSeries>();
		for (GoodsSeries goodsSeries : goodsSeriesList) {
			map.put(goodsSeries.getId(), goodsSeries);
		}

		for (Goods goods : goodsList) {
			Long goodsSeriesID = goods.getSeriesID();
			GoodsSeries goodsSeries = map.get(goodsSeriesID);
			if (goodsSeries == null) {
				goodsNoSeries.add("problem: goods " + goods.getId()
						+ " has no goods series");
				continue;
			}
			goodsSeries.addGoods(goods);
		}
		map.clear();
		map = null;
	}

	private List<Goods> getAllGoods(Connection conn2) throws SQLException {
		String sql = "select goods_id, goods_attr.id goods_attr_id,goods_attr_value_id,goods_attr.name,value,goods.series_id from goods_goods_attr_value_relation "
				+ " left join goods_attr on goods_attr.id=goods_goods_attr_value_relation.goods_attr_id "
				+ " left join  goods_attr_value on goods_attr_value.goods_attr_id=goods_attr.id and goods_goods_attr_value_relation.goods_attr_value_id=goods_attr_value.id "
				+ " left join  goods on goods.id=goods_id "
				+ " where goods_attr.need_img=1";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		List<Goods> goodsList = new ArrayList<Goods>();
		while (rs.next()) {
			Goods goods = new Goods();
			goods.setId(rs.getLong("goods_id"));
			goods.setAttrID(rs.getInt("goods_attr_id"));
			goods.setAttrValueID(rs.getInt("goods_attr_value_id"));
			goods.setName(rs.getString("name"));
			goods.setValue(rs.getString("value"));
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
		GoodsImageMigrationTask task = new GoodsImageMigrationTask();
		task.run();
		System.exit(0);

		// System.out.println(task.getGoodsSeriesImagePath(1205));
		System.out.println(task.getGoodsImagePath(1750, 2385));
	}
}
