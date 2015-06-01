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
public class BrandStoryImageMigrationTask extends SingleImageMigrationTask {
	public BrandStoryImageMigrationTask() throws FileNotFoundException, IOException {
		super();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		BrandStoryImageMigrationTask task = new BrandStoryImageMigrationTask();
		task.run();
		System.exit(0);
	}

	@Override
	protected String getAllItemsSQL() {
		return "select id,photo_path from brand_story";
	}

	@Override
	protected String[] getDBColumns() {
		return new String[] { "id", "photo_path" };
	}

	protected String getSaveSql() {
		return "UPDATE brand_story SET photo_path=? WHERE id=?";
	}
}
