package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Title: CombineShopsTask.java
 * @Package com.if2c.harald.migration
 * @Description: TODO(更新店铺信息)
 * @author niexijuan
 * @date 2014年3月19日 下午3:04:27
 * @version V1.0
 */
public class CombineShopsTask extends ImageMigrationTask {

	private int[] shopIDs = null;
	private String masterShopName = null;
	private int masterID = -1;

	@Override
	public void run() throws Exception {
		conn = getConnection();
		conn.setAutoCommit(false);
		// 更新店铺品牌表中所有的shopid为主店铺
		updateShopBrandInfo(conn);
		// 更新 goods 表 设置shop_id 为主店铺id, brand_id 不变
		updateGoods(conn);
		// 更新 goods_series 表 设置shop_id 为主店铺id, brand_id 不变
		updateGoodsSeries(conn);
		// 更新店铺商品属性值得shop_id
		updateGoodsAttrValue(conn);
		// 更新shop_deduction和shipping_address
		updateShopDeductionAndAddredss(conn);
		// 删除子店铺装修信息以及子店铺
		delShopChild(conn);
		// 更新主店铺的名字
		updateMasterShopName(conn);
		conn.commit();
		System.out.println("===========");
	}

	/**
	 * 
	*
	 */
	private void updateMasterShopName(Connection conn) throws SQLException {
		if(masterShopName==null){
			return;
		}
		PreparedStatement ps = conn
				.prepareStatement("update shop set name=? where id = ?");
		ps.setString(1, masterShopName);
		ps.setInt(2, masterID);
		ps.execute();
	}

	/**
	 * 
	 * @Title: updateShopBrandInfo
	 * @Description: TODO(更改shop_brand 的所有店铺-品牌的关联关系 为  主店铺 <--> 品牌)
	 * @param @param conn
	 * @param @throws SQLException 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author niexijuan
	 */
	private void updateShopBrandInfo(Connection conn) throws SQLException {
		String sql = "update shop_brand set shop_id=? where shop_id in ("
				+ getInSQLWithMasterID() + ")";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, masterID);
		ps.execute();
	}

	/**
	 * 
	 * @Title: updateGoods
	 * @Description: 更新 goods 表 设置shop_id 为主店铺id, brand_id 不变
	 * @param @param conn
	 * @param @throws SQLException 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author niexijuan
	 */
	private void updateGoods(Connection conn) throws SQLException {
		String sql = "update goods set shop_id=? where brand_id in ("
				+ "select brand_id from shop_brand where shop_id in ("
				+ getInSQLWithMasterID() + "))";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, masterID);
		ps.execute();
	}

	/**
	 * 
	 * @Title: updateGoodsSeries
	 * @Description: 更新 goods_series 表 设置shop_id 为主店铺id, brand_id 不变
	 * @param @param conn
	 * @param @throws SQLException 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author niexijuan
	 */
	private void updateGoodsSeries(Connection conn) throws SQLException {
		String sql = "update goods_series set shop_id=? where brand_id in ("
				+ "select brand_id from shop_brand where shop_id in ("
				+ getInSQLWithMasterID() + "))";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, masterID);
		ps.execute();
	}

	/**
	 * 
	 * @Title: updateGoodsAttrValue
	 * @Description: 更新 goods_attr_value 表 设置shop_id 为主店铺id
	 * @param @param conn
	 * @param @throws SQLException 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author niexijuan
	 */
	private void updateGoodsAttrValue(Connection conn) throws SQLException {
		String sql = "update goods_attr_value set shop_id=? where shop_id in ("
				+ getInSQLWithMasterID() + ")";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, masterID);
		ps.execute();
	}

	/**
	 * 
	 * @Title: delShopChild
	 * @Description: 删除子店铺装修信息以及子店铺
	 * @param @param conn
	 * @param @throws SQLException 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author niexijuan
	 */
	private void delShopChild(Connection conn) throws SQLException {
		String sql = "delete from shop_decoration where shop_id in ("
				+ getInSQLWithoutMasterID() + ")";
		String shopSql = "delete from shop where id in ("
				+ getInSQLWithoutMasterID() + ")";
		PreparedStatement ps = conn.prepareStatement(sql);
		PreparedStatement ps1 = conn.prepareStatement(shopSql);
		ps.execute();
		ps1.execute();
	}

	/**
	 * 
	 * @Title: updateShopDeductionAndAddredss
	 * @Description: 更新 shop_deduction 和 shipping_address 表 设置shop_id 为主店铺id
	 * @param @param conn
	 * @param @throws SQLException 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author niexijuan
	 */
	private void updateShopDeductionAndAddredss(Connection conn)
			throws SQLException {
		String sql = "update shop_deduction set shop_id=? where shop_id in ("
				+ getInSQLWithMasterID() + ")";
		String addressSql = "update shipping_address set shop_id=? where shop_id in ("
				+ getInSQLWithMasterID() + ")";
		PreparedStatement ps = conn.prepareStatement(sql);
		PreparedStatement ps1 = conn.prepareStatement(addressSql);
		ps.setInt(1, masterID);
		ps1.setInt(1, masterID);

		ps.execute();
		ps1.execute();
	}

	// return x,y,z
	public String getInSQLWithMasterID() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < shopIDs.length; i++) {
			sb.append(shopIDs[i]).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	// return y,z
	public String getInSQLWithoutMasterID() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < shopIDs.length; i++) {
			sb.append(shopIDs[i]).append(",");

		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public int[] getShopIDs() {
		return shopIDs;
	}

	public void setShopIDs(int[] shopIDs) {
		this.shopIDs = shopIDs;
		masterID = shopIDs[0];
	}

	public String getMasterShopName() {
		return masterShopName;
	}

	public void setMasterShopName(String masterShopName) {
		this.masterShopName = masterShopName;
	}

	public CombineShopsTask() throws FileNotFoundException, IOException {
		super();
	}

	private static void addDetailData(List<Combine> list) {
		{
			Combine combine = new Combine();
			combine.name = "bodycare美货通";
			combine.shopIDs = new int[] { 676, 677, 707, 708, 725, 726, 762,
					792, 804, 811, 844, 858, 871, 950, 951, 970, 1062 };
			list.add(combine);
		}
	}
	
	LinkedHashMap<String, Combine> map=new LinkedHashMap<String, Combine>();
	
	public List<Combine> getCombinedShopData() throws SQLException {
		conn = getConnection();
		String sql = "select seller.id,shop.id from seller left join shop on shop.seller_id=seller.id order by seller.id;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			String sellerID=rs.getString(1);
			String shopID=rs.getString(2);
			Combine combine=map.get(sellerID);
			
			if(combine==null){
				 combine=new Combine();
				 map.put(sellerID, combine);
			}
			combine.addShop(shopID);
		}
		rs.close();
		ps.close();
		return new ArrayList(map.values());
		
	}

	public static void main(String[] args) throws Exception {
		List<Combine> list = new ArrayList<Combine>();
		// 添加数据
		addDetailData(list);
		
		CombineShopsTask task = new CombineShopsTask();
		list=task.getCombinedShopData();
		for (Combine combine : list) {
			if(combine.shopIDs.length<=1){
				continue;
			}
			System.out.println(combine);
			task.setShopIDs(combine.shopIDs);
			task.setMasterShopName(combine.name); 
			task.run();
		}
	}
}

class Combine {
	String name = null;
	int[] shopIDs = null;
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (int i : shopIDs) {
			sb.append(i).append(",");
		}
		return sb.toString();
	}
	
	public void addShop(String shopID) {
		if(shopIDs==null){
			shopIDs=new int[]{Integer.parseInt(shopID)};
			return;
		}
		int[] newIDs=new int[shopIDs.length+1];
		System.arraycopy(shopIDs, 0, newIDs, 0, shopIDs.length);
		newIDs[shopIDs.length]=Integer.parseInt(shopID);
		shopIDs=newIDs;
	}
}
