package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 迁移2C-V1R2B001 的Shop_Decoration图片存储规则 到 图片服务器。
 * 
 * @author zhw <br>
 *         Created at 2013年10月11日
 */
public class ShopDecorationImageMigrationTask extends SingleImageMigrationTask {
	public ShopDecorationImageMigrationTask() throws FileNotFoundException, IOException {
		super();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		ShopDecorationImageMigrationTask task = new ShopDecorationImageMigrationTask();
		task.run();
		System.exit(0);
	}

	@Override
	protected String getAllItemsSQL() {
		return "select id,image from shop_decoration";
	}

	@Override
	protected String[] getDBColumns() {
		return new String[] { "id", "image" };
	}

	protected String getSaveSql() {
		return "UPDATE shop_decoration SET image=? WHERE id=?";
	}
}
