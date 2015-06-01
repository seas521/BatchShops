package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class PictureManageImageMigrationTask extends SingleImageMigrationTask {
	public PictureManageImageMigrationTask() throws FileNotFoundException,
			IOException {
		super();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		PictureManageImageMigrationTask task = new PictureManageImageMigrationTask();
		task.run();
		System.exit(0);
	}
	
	
	protected String getAllItemsSQL() {
		return "select id,img_path from picture_manage";
	}

	@Override
	protected String[] getDBColumns() {
		return new String[] { "id", "img_path" };
	}

	protected String getSaveSql() {
		return "UPDATE picture_manage SET img_path=? WHERE id=?";
	}
	

}
