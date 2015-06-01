package com.if2c.harald.migration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB_exa_export {
	final static String logisticsName = "China Post Air Mail";
	final static String templateName = "新手模板";
	final static String hostIp = "10.10.6.78";
	final static String user = "erp_if2c";
	final static String password = "shzygjrmdwg";
	final static String dataBase = "exagoods_product";
	static ArrayList<Integer> sellerIDs = new ArrayList<Integer>();
//	private static Connection conn;
//	 static Connection reConn()throws Exception{
//		try {
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//			String url = "jdbc:mysql://" + hostIp + ":3306/" + dataBase
//					+ "?user=" + user + "&password=" + password
//					+ "&characterEncoding=utf-8";
//			// myDB为数据库名
//			conn = DriverManager.getConnection(url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return conn;
//	}

	public static void exportData(Connection conn,String templateName, Integer sellerId,
			Integer logisticsId, Integer minDays, Integer maxDays,
			ArrayList<String> listArea) throws Exception {
		Statement sm = conn.createStatement();
		String insertNationalRelation = "";
		conn.setAutoCommit(false);
		String sqlInsert = "insert into freight_template (`name`,seller_id,`default`) values('"
				+ templateName + "'," + sellerId + ",1)";// #模板名称作为参数，
		/******** 插入模板 **/
		System.out.println("***********" + sqlInsert);
		sm.execute(sqlInsert);// 插入模板
		String selectLastId = "select LAST_INSERT_ID() as id ";// ;#返回最后一次插入的模板id
		ResultSet result = sm.executeQuery(selectLastId);// 返回模板的id
		Integer lastIdTemplate_id = 0;
		Integer detailLastId = 0;
		if (result.next()) {
			lastIdTemplate_id = result.getInt("id");// 获取上一个的id
		}
		// 插入详细表
		String insert = "	insert   into   freight_template_detail (template_id,logistics_id,freight_type,min_days,max_days,update_time,seller_id)   values("
				+ lastIdTemplate_id
				+ ","
				+ logisticsId
				+ ",2,"
				+ minDays
				+ ","
				+ maxDays
				+ ",'"
				+ new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date())
				+ "'," + sellerId + ")";
		/******** 插入模板详细表 **/
		System.out.println("%&%&%&&->" + insert);
		sm.execute(insert);
		String templateDetailLastId = "select LAST_INSERT_ID() as id";// #返回最后一次插入的模板详细id
		ResultSet detailLastIdResult = sm.executeQuery(templateDetailLastId);
		if (detailLastIdResult.next()) {
			detailLastId = detailLastIdResult.getInt("id");
		}
		for (String s : listArea) {
			insertNationalRelation = "insert  into  freight_template_national_relation  (freight_template_detail_id,country_id  ) values("
					+ detailLastId + "," + s + ")";
			sm.addBatch(insertNationalRelation);
		}
		/*** 插入截止 */
		sm.executeBatch();
		conn.commit();
	}

	public static void exportRun(Connection conn) throws Exception {
		/** 准备数据 **/
		System.out.println(conn==null);
		Integer rsLogisticsId = 0;
		Integer minDays = 0;
		Integer maxDays = 0;
		Statement sm = conn.createStatement();
		Statement sm1 = conn.createStatement();
		String selectLogistics = "select *  from  logistics_company where name='"
				+ logisticsName + "'";
		ResultSet rsLogisticsIdResult = sm.executeQuery(selectLogistics);
		if (rsLogisticsIdResult.next()) {
			rsLogisticsId = rsLogisticsIdResult.getInt("id");
			minDays = 20;// rsLogisticsIdResult.getInt("min_days");//20
			maxDays = 25;// rsLogisticsIdResult.getInt("max_days");//25
		}
		/** 准备数据 */
		String selectArea = "select *    from  logistics_area where logistics_id="
				+ rsLogisticsId + " and  name <>'" + 11 + "'";
		ResultSet rsArea = sm.executeQuery(selectArea);
		ArrayList<String> areaList = new ArrayList<String>();
		while (rsArea.next()) {
			areaList.add(rsArea.getInt("country_id") + "");
		}
		/** 准备数据结束 */
		ResultSet rs = sm1
				.executeQuery(" SELECT DISTINCT sl.id FROM seller sl INNER JOIN shop s ON s.seller_id = sl.id WHERE NOT EXISTS "
						+ "(SELECT id FROM freight_template ft WHERE ft.seller_id = sl.id)  and NOT EXISTS"
						+ "(SELECT id FROM freight_template ft WHERE ft.seller_id = sl.id)");
		while (rs.next()) {
		   exportData(conn,templateName, rs.getInt("id"), rsLogisticsId,
					minDays, maxDays, areaList);
//		   sellerIDs.add(rs.getInt("id"));
		}
//			updateOldTemplate(conn);
	}

	public static void updateOldTemplate(Connection conn) throws Exception {
		Statement smExcute = conn.createStatement();
		Statement smUpdate = conn.createStatement();
		String  sellerIDes="(";
		for(Integer sellerID: sellerIDs){
			sellerIDes+=sellerID+",";
		}
		sellerIDes=sellerIDes.substring(0,sellerIDes.length()-1)+")";
		ResultSet rs = smUpdate
				.executeQuery("select  *  from freight_template  where in "+sellerIDes);
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		int index = 0;
		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("templateId", rs.getInt("id"));
			map.put("sellerId", rs.getInt("seller_id"));
			l.add(map);
			index++;
			System.out.println("当前数量：" + index);
		}
		for (Map<String, Object> m : l) {
			try {
				String sql = "update  goods_series  set freight_template_id = "
						+ m.get("templateId")
						+ "  where shop_id in   (SELECT id  FROM shop  where    seller_id="
						+ m.get("sellerId") + ")";
				System.out.println("开始更新");
				smExcute.addBatch(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("sql异常");
			}
		}
		smExcute.executeBatch();
		conn.commit();
	}

}
