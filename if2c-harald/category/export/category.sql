ALTER TABLE `category`
	DROP FOREIGN KEY `category_ibfk_1`;
ALTER TABLE `extended_attribute_category`
	DROP FOREIGN KEY `fk_extended_attribute_category_category`;
ALTER TABLE `country_pictrue_brand_category`
	DROP FOREIGN KEY `country_pictrue_brand_category_ibfk_1`;
ALTER TABLE `country_picture_category`
	DROP FOREIGN KEY `FK_country_picture_category_category`;
ALTER TABLE `coupon_category_relation`
	DROP FOREIGN KEY `coupon_category_relation_ibfk_1`;
ALTER TABLE `front_back_category_relation`
	DROP FOREIGN KEY `fk_front_back_category_relation_id`,
	DROP FOREIGN KEY `fk_front_back_category_relation_font_id`;
	
ALTER TABLE `front_back_category_relation`
	CHANGE COLUMN `front_category_id` `front_category_id` BIGINT NOT NULL COMMENT '前台类目id' AFTER `id`,
	CHANGE COLUMN `category_id` `category_id` BIGINT NOT NULL COMMENT '后台类目id' AFTER `front_category_id`;
ALTER TABLE `front_category`
	CHANGE COLUMN `id` `id` BIGINT NOT NULL COMMENT '分类id，1xx一级分类，1xxx二级分类，1xxxxx三级分类' FIRST;
	
	
ALTER TABLE `goods`
	DROP FOREIGN KEY `goods_ibfk_1`;
ALTER TABLE `goods_series`
	DROP FOREIGN KEY `goods_series_ibfk_1`;
ALTER TABLE `operation_promoting_category`
	DROP FOREIGN KEY `operation_promoting_category_ibfk_3`;
ALTER TABLE `shop_deduction`
	DROP FOREIGN KEY `shop_deduction_ibfk_2`;
ALTER TABLE `tax_point`
	DROP FOREIGN KEY `tax_point_ibfk_1`;
ALTER TABLE `front_category`
	DROP FOREIGN KEY `fkfront_category`;
ALTER TABLE `category`
	CHANGE COLUMN `id` `id` BIGINT NOT NULL COMMENT '分类id，1xx一级分类，1xxx二级分类，1xxxxx三级分类' FIRST;
