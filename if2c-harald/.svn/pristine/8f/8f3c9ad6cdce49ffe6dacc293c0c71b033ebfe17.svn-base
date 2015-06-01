package com.if2c.harald.migration.i18n;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.if2c.harald.migration.ImageMigrationTask;

/**
 * key=table_id_lang value=name;
 * 
 * @author shengqiang
 * 
 *         2013-11-14
 */
public class I18Handler {

	private Connection conn = null;
	private Statement stat = null;
	private Properties props=null;
	public I18Handler(String url, String user, String password) throws Exception {
		try {
			String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			throw new Exception("Failed to load MySQL driver.");
		}
		try {
			conn = DriverManager.getConnection(url, user, password);
			stat = conn.createStatement();
		} catch (SQLException e) {
			throw new Exception("Failed to create connnection or Statement");
		}
		ImageMigrationTask task=new ImageMigrationTask() {
			@Override
			public Properties getProps() {
				return super.getProps();
			}
		};
		props=task.getProps();
	}

	public List<RedisBean> operateList(Statement stat, String tableName,
			List<RedisBean> list) throws SQLException {
		ResultSet result = stat.executeQuery("SELECT * FROM " + tableName);
		while (result.next()) {
			RedisBean bean = new RedisBean();
			//相关功能的表中只有以下这二个字段
			String id = result.getString("id");
			String value = result.getString("translation");
			if (StringUtils.isBlank(value) || StringUtils.equals(value, "null")) {
				continue;
			}
			bean.setTable(tableName);
			bean.setLang("en");
			bean.setId(id);
			//有的数据带双引号
			bean.setTranslation(StringUtils.stripToEmpty(StringUtils.replace(
					value, "\"", "")));
			System.out.println(bean.toString());
			list.add(bean);
		}
		return list;
	}

	public List<RedisBean> getDataList() {
		List<RedisBean> list = new ArrayList<RedisBean>();
		try {
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, null,
					new String[] { "TABLE" });
			int i=0;
			while (rs.next()) {
				//遍历当前DB内的所有表
				String tableName = rs.getString("TABLE_NAME");
				String foreignTableNames=props.getProperty("table.names");
				String[] foreignTableNamesArray=StringUtils.split(foreignTableNames, ",");
				if(foreignTableNamesArray.length==i){
					break;
				}
  				if(StringUtils.equals(tableName, foreignTableNamesArray[i])){
					operateList(stat, tableName, list);
					i++;
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void main(String args[]) {
		I18Handler tc;
		try {
			tc = new I18Handler(
					"jdbc:mysql://localhost:3306/translation", "root", "shengqiang");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		tc.getDataList();
	}

}
