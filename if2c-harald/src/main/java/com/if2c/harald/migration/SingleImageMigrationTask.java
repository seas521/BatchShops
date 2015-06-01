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

public abstract class SingleImageMigrationTask extends ImageMigrationTask {

	private List<String> noImage = new ArrayList<String>();

	public SingleImageMigrationTask() throws FileNotFoundException, IOException {
		super();
	}

	public void run() throws SQLException {
		conn = getConnection();
		Map<Integer, String> map = getAllItems(conn);
		save2TFS(map);
		save2DB(conn, map);

		printErrors();
		conn.close();
	}

	private void printErrors() {
		printListInfo("no local image size", noImage);
		printListInfo("save image to tfs fail size", saveImageFail);
	}

	private void save2DB(Connection conn, Map<Integer, String> map)
			throws SQLException {
		conn.setAutoCommit(false);
		String sql = getSaveSql();
		PreparedStatement ps = conn.prepareStatement(sql);
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			ps.setString(1, entry.getValue());
			ps.setInt(2, entry.getKey());
			ps.addBatch();
		}
		ps.executeBatch();
		conn.commit();
		ps.close();
	}

	protected abstract String getSaveSql();

	private void save2TFS(Map<Integer, String> map) {
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			int id = entry.getKey();
			String oldImage = entry.getValue();
			String oldPath = getImageLocation() + oldImage;

			File file = new File(oldPath);
			if (!file.exists()) {
				noImage.add(id + " 's image not exist "+oldPath);
				entry.setValue(null);
				continue;
			}
			String newImage = getTFSManager().saveFile(oldPath, null,
					IMAGE_SUFFIX);
			if (newImage == null) {
				saveImageFail.add(id + " save image to tsf failed. ");
			} else {
				newImage = newImage + IMAGE_SUFFIX;
				entry.setValue(newImage);
			}
		}
	}

	private Map<Integer, String> getAllItems(Connection conn)
			throws SQLException {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		String sql = getAllItemsSQL();
		String[] columns = getDBColumns();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			map.put(rs.getInt(columns[0]), rs.getString(columns[1]));
		}
		rs.close();
		ps.close();
		return map;
	}

	protected abstract String[] getDBColumns();

	protected abstract String getAllItemsSQL();

}