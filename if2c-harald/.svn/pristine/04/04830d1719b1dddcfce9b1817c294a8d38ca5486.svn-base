package com.if2c.harald.migration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.if2c.harald.migration.category.ExtendedAttributePool;
import com.if2c.harald.migration.category.GoodsAttributePool;
import com.if2c.harald.migration.category.JAXBUtils;
import com.if2c.harald.migration.category.LeanExtendedAttribute;
import com.if2c.harald.migration.category.LeanExtendedAttributeValue;
import com.if2c.harald.migration.category.LeanGoodsAttribute;
import com.if2c.harald.migration.category.LeanGoodsAttributeValue;
import com.if2c.harald.migration.category.model.Category;
import com.if2c.harald.migration.category.model.CategoryRoot;
import com.if2c.harald.migration.category.model.ExtendedAttribute;
import com.if2c.harald.migration.category.model.GoodsAttribute;
import com.if2c.harald.migration.category.model.ObjectFactory;
//delete from attribute_category_value_relation;
//delete from attribute_category;
//delete from attribute;
//delete from attribute_value;
//
//
//delete from extended_attribute_value_relation;
//delete from extended_attribute_category;
//delete from extended_attribute;
//delete from extended_attribute_value;
//
//delete from category where level=4;
//delete from category where level=3;
//delete from category where level=2;
//delete from category where level=1;

//ALTER TABLE `category` CHANGE COLUMN `name` `name` VARCHAR(128) NOT NULL COMMENT '分类名称' AFTER `id`;
//ALTER TABLE `attribute` CHANGE COLUMN `name` `name` VARCHAR(128) NOT NULL COMMENT '商品属性名' AFTER `id`;
//ALTER TABLE `extended_attribute` CHANGE COLUMN `name` `name` VARCHAR(128) NOT NULL COMMENT '扩展属性名' AFTER `id`;
//ALTER TABLE `extended_attribute_value` CHANGE COLUMN `value` `value` VARCHAR(128) NOT NULL COMMENT '扩展属性值' AFTER `id`;

public class CategoryImportTask extends ImageMigrationTask {
	private String srcCateogryXML = "category/export/category.xml";
	private String destCateogryXML = "category/export/category_combined.xml";
	private GoodsAttributePool goodAttributePool;
	private ExtendedAttributePool extendedAttributePool;
	private boolean ifImport = false;
	private boolean combineIDs = true;

	public CategoryImportTask() throws FileNotFoundException, IOException {
		super();
	}

	public void run() throws Exception {
		// the src is the originally generated id,
		// the destination is the combined new id.
		// please use beyongcompare to check the difference.
		CategoryRoot categoryRoot = null;
		LinkedHashMap<Long, Category> flatCategoriesMap=null;
		if (combineIDs) {
			InputStream is = new FileInputStream(srcCateogryXML);
			categoryRoot = (CategoryRoot) JAXBUtils.unmarshal(
					ObjectFactory.class, is);

			flatCategoriesMap = tree2Flat(categoryRoot);
			initialGoodsAttributePool(categoryRoot.getCategory());
			initialExtendedAttributePool(categoryRoot.getCategory());

			leanGoodAttributeAndValue(flatCategoriesMap);
			leanExtendedAttributeAndValue(flatCategoriesMap);
			printInfo(flatCategoriesMap);
			checkSuspiciousOnes(flatCategoriesMap);

			OutputStream os = new FileOutputStream(destCateogryXML);
			JAXBUtils.marshal(ObjectFactory.class, categoryRoot, os);
		} else {
			// no need to generate the destCateogryXML, use the destCateogryXML
			// to insert db directly
			InputStream is = new FileInputStream(destCateogryXML);
			categoryRoot = (CategoryRoot) JAXBUtils.unmarshal(
					ObjectFactory.class, is);
			flatCategoriesMap = tree2Flat(categoryRoot);
		}

		if (ifImport) {
			importCategory(categoryRoot.getCategory());
			// import goods attributes stuff
			importGoodsAttribute(goodAttributePool);
			importGoodsAttributeValue(goodAttributePool);
			importCategoryAttributeValueRelationShip(flatCategoriesMap);

			// import extended attributes stuff
			importExtendedAttribute(extendedAttributePool);
			importExtendedAttributeValue(extendedAttributePool);
			importCategoryExtendedAttributeValueRelationShip(flatCategoriesMap);
		}
	}

	private void importCategoryAttributeValueRelationShip(
			LinkedHashMap<Long, Category> flatCategoriesMap)
			throws SQLException {
		String attributeSQL = "INSERT INTO `attribute_category` ( `attribute_id`, `position`, `category_id`, `status`, `need_img`, `need_value`) VALUES (?,?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(attributeSQL);

		String valueSQL = "INSERT INTO `attribute_category_value_relation` (`attribute_category_id`,`attribute_value_id`, `position`, `status`) VALUES (?,?,?,?);";
		PreparedStatement ps2 = conn.prepareStatement(valueSQL);

		// the goods attributes and values are all leaned ones.
		for (Category category : flatCategoriesMap.values()) {
			for (GoodsAttribute goodsAttribute : category.getGoodsAttribute()) {
				LeanGoodsAttribute leanAttribute = (LeanGoodsAttribute) goodsAttribute;
				ps.setInt(1, (int) leanAttribute.getId());
				ps.setInt(2, 1);
				ps.setLong(3, category.getId());
				ps.setInt(4, 1);
				ps.setInt(5, leanAttribute.isHasImage() ? 1 : 0);
				ps.setInt(6, 1);
				ps.execute();
				int lastID = getLastInsertID();

				for (LeanGoodsAttributeValue value : leanAttribute.getValues()) {
					ps2.setInt(1, lastID);
					ps2.setInt(2, value.getId());
					ps2.setInt(3, 1);
					ps2.setInt(4, 1);
					try {
						ps2.execute();
					} catch (SQLException e) {
						System.err.println(e.getMessage());
					}
					
				}
			}
		}
		ps.close();
		ps2.close();
	}

	private void importCategoryExtendedAttributeValueRelationShip(
			LinkedHashMap<Long, Category> flatCategoriesMap)
			throws SQLException {
		String attributeSQL = "INSERT INTO `extended_attribute_category` (`extended_attribute_id`, `category_id`, `position`, `status`, `need_img`, `need_value`) VALUES (?,?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(attributeSQL);

		String valueSQL = "INSERT INTO `extended_attribute_value_relation` (`extended_attribute_category_id`,`extended_attribute_value_id`, `position`, `status`) VALUES (?,?,?,?);";
		PreparedStatement ps2 = conn.prepareStatement(valueSQL);

		// the goods attributes and values are all leaned ones.
		for (Category category : flatCategoriesMap.values()) {
			for (ExtendedAttribute extendedAttribute : category
					.getExtendedAttribute()) {
				LeanExtendedAttribute leanAttribute = (LeanExtendedAttribute) extendedAttribute;
				ps.setInt(1, (int) leanAttribute.getId());
				ps.setLong(2, category.getId());
				ps.setInt(3, 1);
				ps.setInt(4, 1);
				ps.setInt(5, 0);
				ps.setInt(6, 1);
				try {
					ps.execute();
				} catch (Exception e) {
					System.err.println(e.getMessage());
					continue;
				}
				
				int lastID = getLastInsertID();

				for (LeanExtendedAttributeValue value : leanAttribute
						.getValues()) {
					ps2.setInt(1, lastID);
					ps2.setInt(2, value.getId());
					ps2.setInt(3, 1);
					ps2.setInt(4, 1);
					try {
						ps2.execute();
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}

				}
			}
		}
		ps.close();
		ps2.close();
	}

	private void importGoodsAttributeValue(GoodsAttributePool goodAttributePool2)
			throws SQLException {
		String sql = "INSERT INTO `attribute_value` (`id`, `value`, `position`, `shop_id`, `group`, `status`) VALUES (?,?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		Set<LeanGoodsAttributeValue> list = goodAttributePool
				.getGoodsAttributeValues();
		for (LeanGoodsAttributeValue value : list) {
			ps.setInt(1, value.getId());
			ps.setString(2, value.getName());
			ps.setInt(3, 1);
			ps.setInt(4, 0);
			ps.setInt(5, 0);
			ps.setInt(6, 1);
			ps.execute();
		}
		ps.close();

	}

	private void importExtendedAttributeValue(
			ExtendedAttributePool extendedAttributePool2) throws SQLException {
		String sql = "INSERT INTO `extended_attribute_value` (`id`, `value`, `position`, `shop_id`, `status`) VALUES (?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		Set<LeanExtendedAttributeValue> list = extendedAttributePool2
				.getExtendedAttributeValue();
		for (LeanExtendedAttributeValue value : list) {
			ps.setInt(1, value.getId());
			ps.setString(2, value.getName());
			ps.setInt(3, 1);
			ps.setInt(4, 0);
			ps.setInt(5, 1);
			ps.execute();
		}
		ps.close();
	}

	private void importGoodsAttribute(GoodsAttributePool goodAttributePool)
			throws SQLException {
		String sql = "INSERT INTO `attribute` (`id`, `name`, `shop_id`, `position`, `status`) VALUES (?, ?, ?, ?, ?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		List<LeanGoodsAttribute> list = goodAttributePool.getGoodsAttributes();
		for (LeanGoodsAttribute leanGoodsAttribute : list) {
			ps.setInt(1, (int) leanGoodsAttribute.getId());
			ps.setString(2, leanGoodsAttribute.getName());
			ps.setInt(3, 0);
			ps.setInt(4, 1);
			ps.setInt(5, 1);
			ps.execute();
		}
		ps.close();
	}

	private void importExtendedAttribute(
			ExtendedAttributePool extendedAttributePool) throws SQLException {
		String sql = "INSERT INTO `extended_attribute` (`id`, `name`, `position`, `shop_id`, `single`, `status`) VALUES (?,?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		List<LeanExtendedAttribute> list = extendedAttributePool
				.getExtendedAttribute();
		for (ExtendedAttribute extended : list) {
			ps.setInt(1, (int) extended.getId());
			ps.setString(2, extended.getName());
			ps.setInt(3, 1);
			ps.setInt(4, 0);
			ps.setInt(5, extended.isIsCheck() ? 0 : 1);
			ps.setInt(6, 1);
			ps.execute();
		}
		ps.close();
	}

	private void printInfo(LinkedHashMap<Long, Category> flatCategoriesMap) {
		for (Category category : flatCategoriesMap.values()) {

			if (category.getGoodsAttribute().isEmpty()) {
				continue;
			}
			System.out
					.println("==================================================================");
			System.out.println(category.getId() + "  " + category.getName());
			System.out.println("===Goods Attribute");
			for (GoodsAttribute ga : category.getGoodsAttribute()) {
				System.out.println(ga);

				LeanGoodsAttribute lga = (LeanGoodsAttribute) ga;
				for (LeanGoodsAttributeValue gav : lga.getValues()) {
					System.out.println(gav);
				}
			}
			System.out.println("===Extended Attribute");
			if(category.getExtendedAttribute()==null){
				
			}
			for (ExtendedAttribute ga : category.getExtendedAttribute()) {
				System.out.println(ga);

				LeanExtendedAttribute lga = (LeanExtendedAttribute) ga;
				if( lga.getValues()==null){
					System.out.println("Extended Attribute has no values ");
					continue;
				}
				for (LeanExtendedAttributeValue gav : lga.getValues()) {
					System.out.println(gav);
				}
			}
		}
	}

	private void checkSuspiciousOnes(
			LinkedHashMap<Long, Category> flatCategoriesMap) {
		for (Category category : flatCategoriesMap.values()) {

			if (category.getGoodsAttribute().isEmpty()) {
				continue;
			}
			for (GoodsAttribute ga : category.getGoodsAttribute()) {
				System.out.println(ga);

				LeanGoodsAttribute lga = (LeanGoodsAttribute) ga;
				for (LeanGoodsAttributeValue gav : lga.getValues()) {
					if (leftBracketNumber(gav.getName()) > 1) {
						System.err.println(gav);
					}
				}
			}
			for (ExtendedAttribute ga : category.getExtendedAttribute()) {
				LeanExtendedAttribute lga = (LeanExtendedAttribute) ga;
				for (LeanExtendedAttributeValue gav : lga.getValues()) {
					if (gav.getName().indexOf("E10") > 0) {
						System.err.println(gav);
					}

					if (leftBracketNumber(gav.getName()) > 1) {
						System.err.println(gav);
					}
				}
			}
		}
	}

	private int leftBracketNumber(String a) {
		char[] b1 = a.toCharArray();
		int count1 = 0;
		for (int i = 0; i < a.length(); i++) {
			char c = b1[i];
			if (c == '(') {
				count1++;
			}
		}
		return count1;
	}

	// process the goods attributes and values
	private void leanGoodAttributeAndValue(
			LinkedHashMap<Long, Category> flatCategoriesMap) {
		for (Category category : flatCategoriesMap.values()) {
			List<GoodsAttribute> list = category.getGoodsAttribute();

			List<GoodsAttribute> result = processGoodsAttributeList(list);
			category.setGoodsAttribute(result);
		}
	}

	// process the goods attributes and values
	private void leanExtendedAttributeAndValue(
			LinkedHashMap<Long, Category> flatCategoriesMap) {
		for (Category category : flatCategoriesMap.values()) {
			List<ExtendedAttribute> list = category.getExtendedAttribute();

			List<ExtendedAttribute> result = processExtendedAttributeList(list);
			category.setExtendedAttribute(result);
		}
	}

	// import the data from the pool and make a copy result
	private List<GoodsAttribute> processGoodsAttributeList(
			List<GoodsAttribute> list) {
		List<GoodsAttribute> result = new ArrayList<GoodsAttribute>();
		for (GoodsAttribute goodsAttribute : list) {
			GoodsAttribute ga = retreiveFromPool(goodsAttribute);
			result.add(ga);
		}
		return result;
	}

	private List<ExtendedAttribute> processExtendedAttributeList(
			List<ExtendedAttribute> list) {
		List<ExtendedAttribute> result = new ArrayList<ExtendedAttribute>();
		for (ExtendedAttribute goodsAttribute : list) {
			ExtendedAttribute ga = retreiveFromPool(goodsAttribute);
			result.add(ga);
		}
		return result;
	}

	// get the corresponding LeanGoodsAttribute from the pool
	private GoodsAttribute retreiveFromPool(GoodsAttribute goodsAttribute) {
		GoodsAttribute ga = goodAttributePool.returnCopy(goodsAttribute);
		return ga;
	}

	private ExtendedAttribute retreiveFromPool(ExtendedAttribute goodsAttribute) {
		ExtendedAttribute ga = extendedAttributePool.returnCopy(goodsAttribute);
		return ga;
	}

	private void initialGoodsAttributePool(List<Category> list) {
		// make the goods attridute and value pool
		goodAttributePool = new GoodsAttributePool();
		goodAttributePool.add(list);
		goodAttributePool.printInfo();
	}

	private void initialExtendedAttributePool(List<Category> list) {
		// make the goods attridute and value pool
		extendedAttributePool = new ExtendedAttributePool();
		extendedAttributePool.add(list);
		extendedAttributePool.printInfo();
	}

	private LinkedHashMap<Long, Category> tree2Flat(CategoryRoot categoryRoot) {
		LinkedHashMap<Long, Category> map = new LinkedHashMap<Long, Category>();

		List<Category> list = categoryRoot.getCategory();
		for (Category category : list) {
			tree2Flat(category, map);
		}

		printGoodsAttributes(map);
		return map;
	}

	private void printGoodsAttributes(LinkedHashMap<Long, Category> map) {
		for (Category category : map.values()) {
			List<GoodsAttribute> list = category.getGoodsAttribute();
			if (list.isEmpty()) {
				continue;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(category.getId() + "  " + category.getName())
					.append("]");
			for (GoodsAttribute goodsAttribute : list) {
				sb.append("\n\r").append(
						goodsAttribute.getId() + "  "
								+ goodsAttribute.getName() + " "
								+ goodsAttribute.getGoodsAttributeValue());
			}
			System.out.println(sb.toString());
		}
	}

	private void tree2Flat(Category category, LinkedHashMap<Long, Category> map) {
		map.put(category.getId(), category);
		List<Category> list = category.getCategory();
		if (list != null && !list.isEmpty()) {
			for (Category child : list) {
				tree2Flat(child, map);
			}
		}
	}

	private void importCategory(List<Category> category) throws SQLException {
		String sql = "INSERT INTO `category` ( `id`,  `name`,  `level`,  `parent_id`,  `status`,  `position`,  `trade`,  `display`) VALUES (?, ?, ?, ?, ?, ?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);

		insertCategories(ps, category, 0);

		ps.close();
	}

	private void insertCategories(PreparedStatement ps,
			List<Category> categoryList, long parentID) throws SQLException {
		for (Category category : categoryList) {
			insertCategory(ps, category, parentID);
			List<Category> children = category.getCategory();
			if (children != null && !children.isEmpty()) {
				insertCategories(ps, children, category.getId());
			}

		}

	}

	private void insertCategory(PreparedStatement ps, Category category,
			long parentID) throws SQLException {
		ps.setLong(1, category.getId());
		ps.setString(2, category.getName());
		ps.setInt(3, category.getLevel());
		if (parentID == 0) {
			ps.setString(4, null);
		} else {
			ps.setLong(4, parentID);
		}
		ps.setInt(5, 1);
		ps.setInt(6, 1);
		ps.setInt(7, 2);
		ps.setInt(8, 1);
		try {
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("insert failed: " + category.getId() + " "
					+ category.getName() + "  parent id" + parentID);
		}
	}

	public int getLastInsertID() throws SQLException {
		String sql = "SELECT LAST_INSERT_ID() as last_id";
		Statement statement = conn.createStatement();
		ResultSet set = statement.executeQuery(sql);
		int id = -1;
		if (set.next()) {
			id = set.getInt(1);
		}
		set.close();
		statement.close();
		if (id < 0) {
			throw new RuntimeException("get LAST_INSERT_ID error");
		}
		return id;
	}

	public static void main(String[] args) throws Exception {
		CategoryImportTask task = new CategoryImportTask();
		task.run();
	}
}
