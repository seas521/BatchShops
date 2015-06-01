package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 迁移2C-V1R2B001 的品牌图片存储规则 到 图片服务器。
 * 
 * @author Qian Bing <br>
 *         Created at 2013年10月9日
 */
public class BrandImageMigrationTask extends SingleImageMigrationTask {
	public BrandImageMigrationTask() throws FileNotFoundException, IOException {
		super();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		BrandImageMigrationTask task = new BrandImageMigrationTask();
		task.run();
		System.exit(0);
	}

	@Override
	protected String getAllItemsSQL() {
		return "select id,img_path from brand";
	}

	@Override
	protected String[] getDBColumns() {
		return new String[] { "id", "img_path" };
	}

	protected String getSaveSql() {
		return "UPDATE brand SET img_path=? WHERE id=?";
	}
}
