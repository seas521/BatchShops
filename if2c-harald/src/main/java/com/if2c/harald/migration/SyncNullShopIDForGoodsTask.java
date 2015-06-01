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
public class SyncNullShopIDForGoodsTask extends ImageMigrationTask {

	public SyncNullShopIDForGoodsTask() throws FileNotFoundException,
			IOException {
		super();
	}

	// select
	// goods_series.name,goods_series.shop_id,goods_series.brand_id,shop_brand.shop_id,shop_brand.brand_id
	// from goods_series,shop_brand where
	// goods_series.brand_id=shop_brand.brand_id;
	//
	// select goods_series.name,goods_series.shop_id,shop_brand.shop_id from
	// goods_series,shop_brand where goods_series.brand_id=shop_brand.brand_id;
	//
	//
	// update goods_series,shop_brand set
	// goods_series.shop_id=shop_brand.shop_id where
	// goods_series.brand_id=shop_brand.brand_id;
}
