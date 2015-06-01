package com.if2c.harald.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 根据图片的名字改名为对应的类目ID作为名字。
 * 
 * @author Qian Bing <br>
 *         Created at 2014年1月15日
 */
public class GenerateLevel2CategoryIconNameTask extends ImageMigrationTask {

	public GenerateLevel2CategoryIconNameTask() throws FileNotFoundException,
			IOException {
		super();
	}

	public void run() throws SQLException {
		conn = getConnection();
		Map<String, String> map = getAllImportCategory(conn);
		System.out.println(map);
		conn.close();
		
		
		String rootFolder = "C:/Users/131275/Desktop/small_icon原版";
		genName(rootFolder, map);

		// calcImageLocalPath(goodsList);
		// save2TFS(goodsList);
		// printErrors();

	}

	public void whatislack() throws SQLException {
		conn = getConnection();
		Map<String, String> map = getAllImportCategory(conn);
		conn.close();
		Set<String> allCatID=new HashSet<String>(map.values());
		String rootFolder = "C:/Users/131275/Desktop/small_icon原版/new";
		
		File root = new File(rootFolder);
		File[] files = root.listFiles();
		Set<String> newCatID=new HashSet<String>();
		for (File file : files) {
			String name = file.getName();
			name=name.replaceAll("cat", "");
			name=name.replaceAll(".png", "");
			newCatID.add(name);
		}

		System.out.println(newCatID);
		
		
		allCatID.removeAll(newCatID);
		
		
		System.out.println(allCatID);
		
		Map<String, String> result = new HashMap<String,String>();
		for (String key : map.keySet()) {
			String value=map.get(key);
			if(allCatID.contains(value)){
				result.put(value, key);
			}
		}
		
		System.out.println(result);
	}
	
	private void genName(String rootFolder, Map<String, String> map) {
		File root = new File(rootFolder);
		File[] files = root.listFiles();
		for (File file : files) {
			String name = file.getName();
			if (!name.endsWith(".png")) {
				continue;
			}
			name = name.replaceAll(".png", "");
			String id = map.get(name);
			if (id == null) {
				continue;
			}
//			if(id.startsWith("111")){
//				continue;
//			}

			File newFile = new File(file.getParent() + "/new/", "cat" + id
					+ ".png");
			// try {
			// newFile.createNewFile();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			file.renameTo(newFile);
		}
	}

	private Map<String, String> getAllImportCategory(Connection conn2)
			throws SQLException {
		String sql = "select id,name from category where trade=1 and level =2";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		Map<String, String> map = new HashMap<String, String>();
		while (rs.next()) {
			String name = rs.getString("name");
			String id = rs.getString("id");
			if(id.startsWith("111")){
				name="奢侈品"+name;
			}
			map.put(name, id);
		}
		rs.close();
		ps.close();
		return map;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		GenerateLevel2CategoryIconNameTask task = new GenerateLevel2CategoryIconNameTask();
		//task.run();
		task.whatislack();
	}
}
