package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.taobao.common.tfs.DefaultTfsManager;

public abstract class ImageMigrationTask {

	public static final String File_Separator = "/";
	public static final String IMAGE_SUFFIX = ".JPG";
	protected Properties props = null;
	protected Connection conn = null;
	private DefaultTfsManager tfsManager;
	private String export_file=null;
	private String file_path;
	private String img_path;

	protected List<String> saveImageFail = new ArrayList<String>();

	public ImageMigrationTask() throws FileNotFoundException, IOException {
		props = new Properties();
		props.load(new FileReader("config.properties"));
		MySQLDB db = new MySQLDB(props.getProperty("database.url"),
				props.getProperty("database.username"),
				props.getProperty("database.password"));
		conn = db.getConnection();
	}

	public Properties getProps() {
		return props;
	}

	public Connection getConnection() {
		return conn;
	}

	public String getImageLocation() {
		return props.getProperty("image.location");
	}
	

	public String getExport_file() {
		return props.getProperty("export_file");
	}
	public String getOperator_id() {
		return props.getProperty("operator_id");
	}
	public String getCity() {
		return props.getProperty("city");
	}

	public String getFile_path() {
		return props.getProperty("file_path");
	}

	public String getImg_path() {
		return props.getProperty("img_path");
	}

	public DefaultTfsManager getTFSManager() {
		if (tfsManager == null) {
			tfsManager = new DefaultTfsManager();
			tfsManager.setMasterIP(props.getProperty("tfs.masterIP"));
			tfsManager.setNsip(props.getProperty("tfs.nsip"));
			tfsManager.setTimeout(2000);
			tfsManager.setMaxCacheTime(600000);
			tfsManager.setMaxCacheItemCount(10000);
			tfsManager.init();
		}

		return tfsManager;
	}

	public void printListInfo(String label, List<String> list) {
		System.out.println(label + list.size());
		for (String s : list) {
			System.out.println(s);
		}
	}

	public void run() throws Exception {
	}
}