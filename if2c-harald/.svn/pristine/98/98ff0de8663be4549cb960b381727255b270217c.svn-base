package com.if2c.harald.category;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.if2c.harald.migration.MySQLDB;

public class CategoryReader {
	private int exAttrId = 1;
	public static final String IMPORT_FILE = "C:\\Users\\131486\\Desktop\\category.csv";
	public static final String EXPORT_FILE = "C:\\Users\\131486\\Documents\\exportCategory.csv";
	public static final String IMPORT_ATTR_FILE = "C:\\Users\\131486\\Desktop\\attr.csv";
	public static final String IMPORT_EXTEND_ATTR_FILE = "C:\\Users\\131486\\Desktop\\attr_extend.csv";
	public static final String EXPORT_ATTR_FILE = "C:\\Users\\131486\\Documents\\export_attr.csv";
	public static final String EXPORT_EXTEND_ATTR_FILE = "C:\\Users\\131486\\Documents\\export_attr_extend.csv";
	public static final int IMPORT = 1;
	public static final int EXPORT = 2;
	Map<String, Category> leve2Map = new HashMap<String, Category>();
	Map<String, Category> leve3Map = new HashMap<String, Category>();
	List<String[]> list = null;
	List<String[]> attrList = new ArrayList<String[]>();
	int num = 1;
	private int trade = 2;

	public int getTrade() {
		return trade;
	}

	public int getExAttrId() {
		return exAttrId;
	}

	public void setExAttrId(int exAttrId) {
		this.exAttrId = exAttrId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setTrade(int trade) {
		this.trade = trade;
	}

	public static void main(String[] args) throws IOException, SQLException {
		CategoryReader c = new CategoryReader();
		// c.setTrade(2);
		// c.read();
		// c.setTrade(2);
		// c.read();
		//c.setTrade(1);
//		c.genAttr();
		//c.genExAttr();
		c.setTrade(2);
		c.setExAttrId(3001);
		c.setNum(20001);
		c.genExAttr();

	}

	/**
	 * 
	 * @param importOrExport
	 *            import 1 export 2
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public List<Category> read() throws IOException, SQLException {
		CSVReader reader = getCategoryCSVReader();
		list = reader.readAll();
		System.out.println(list.size());
		List<Category> cats = new ArrayList();
		int num = 0;
		Category c1 = null;
		Category c2 = null;
		Category c3 = null;
		for (int i = 0; i < list.size(); i++) {

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}
			if (!array[0].isEmpty()) {
				c1 = new Category();
				c1.setName(array[0]);
				cats.add(c1);
			}

			try {
				if (!array[1].isEmpty()) {
					c2 = new Category();
					c2.setName(array[1]);
					c1.addChild(c2);
				}
				if (!array[2].isEmpty()) {
					c3 = new Category();
					c3.setName(array[2]);
					c2.addChild(c3);
					System.out.println(num++);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("========================" + i);
			}

		}
		genID(cats);
		print("	", cats);
		printSummary(cats);
		// genAttr();
		//intoDB(cats);
		return cats;
	}

	public CSVReader getCategoryCSVReader() throws FileNotFoundException {
		return trade == 1 ? new CSVReader(new FileReader(IMPORT_FILE))
				: new CSVReader(new FileReader(EXPORT_FILE));
	}

	public CSVReader getAttrCSVReader() throws FileNotFoundException {
		return trade == 1 ? new CSVReader(new FileReader(IMPORT_ATTR_FILE))
				: new CSVReader(new FileReader(EXPORT_ATTR_FILE));
	}

	public CSVReader getExAttrCSVReader() throws FileNotFoundException {
		return trade == 1 ? new CSVReader(new FileReader(
				IMPORT_EXTEND_ATTR_FILE)) : new CSVReader(new FileReader(
				EXPORT_EXTEND_ATTR_FILE));
	}

	public void genAttr() throws IOException, SQLException {
		// Connection conn = getConnection();
		// String sql2 = "delete from goods_attr_value";
		// String sql3 = "delete from goods_attr";
		// Statement stat = conn.createStatement();
		// stat.execute(sql2);
		// stat.execute(sql3);
		// stat.close();
		// conn.close();
		int i = 0;

		List<Category> level1List = read();
		for (Category c1 : level1List) {
			List<Category> level2List = c1.getChildren();
			for (Category c2 : level2List) {
				List<Category> level3List = c2.getChildren();
				leve2Map.put(c2.getName(), c2);
				for (Category c3 : level3List) {
					leve3Map.put(c2.getName() + c3.getName(), c3);
					System.out.println(i++);
				}
			}
		}
		CSVReader reader = getAttrCSVReader();
		List<String[]> list = reader.readAll();
		List<Attr> listAttrs = new ArrayList<Attr>();
		List<AttrValue> listAttrValues = new ArrayList<AttrValue>();
		System.out.println("--->" + list.size());
		List<Category> cats = new ArrayList<Category>();
		Category c1 = null;
		Category c2 = null;
		for (String[] array : list) {
			if (!array[0].isEmpty()) {
				c1 = new Category();
				c1.setName(array[0]);
				cats.add(c1);
			}

			try {
				if (!array[1].isEmpty()) {
					c2 = new Category();
					c2.setName(array[1]);
					c1.addChild(c2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Attr attr = new Attr();
				attr.setName(array[2]);
				System.out.println(c2.getName());
				attr.setCatagoryId(leve2Map.get(c2.getName()).getId());
				attr.setId(exAttrId);
				exAttrId++;
				String[] values = array[4].split("、");
				for (String value : values) {
					AttrValue attrValue = new AttrValue();
					attrValue.setAttrId(attr.getId());
					attrValue.setValue(value);
					attrValue.setId(num++);
					listAttrValues.add(attrValue);
				}
				listAttrs.add(attr);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println(listAttrs.size());
		attrToDB(listAttrs);
	}

	public void genExAttr() throws IOException, SQLException {
		// Connection conn = getConnection();
		// String sql = "delete from extended_attr";
		// String sql1 = "delete from extended_attr_value";
		// Statement stat = conn.createStatement();
		// stat.execute(sql1);
		// stat.execute(sql);
		// stat.close();
		// conn.close();
		int i = 0;
		List<Category> level1List = read();
		for (Category c1 : level1List) {
			List<Category> level2List = c1.getChildren();
			for (Category c2 : level2List) {
				List<Category> level3List = c2.getChildren();
				leve2Map.put(c2.getName(), c2);
				for (Category c3 : level3List) {
					leve3Map.put(c2.getName() + c3.getName(), c3);
					System.out.println(i++);
				}
			}
		}
		CSVReader reader = getExAttrCSVReader();
		List<String[]> list = reader.readAll();
		List<Attr> listAttrs = new ArrayList<Attr>();
		List<AttrValue> listAttrValues = new ArrayList<AttrValue>();
		System.out.println("--->" + list.size());
		List<Category> cats = new ArrayList<Category>();
		Category c1 = null;
		Category c2 = null;
		Category c3 = null;
		for (String[] array : list) {
			if (array[3].contains("颜色")) {
				continue;
			}
			if (!array[0].isEmpty()) {
				c1 = new Category();
				c1.setName(array[0]);
				cats.add(c1);
			}

			try {
				if (!array[1].isEmpty()) {
					c2 = new Category();
					c2.setName(array[1]);
					c1.addChild(c2);
				}
				if (!array[2].isEmpty()) {
					c3 = new Category();
					c3.setName(array[2]);
					c2.addChild(c3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Attr attr = new Attr();
				attr.setName(array[3]);
				attr.setCatagoryId(leve3Map.get(c2.getName() + c3.getName())
						.getId());
				attr.setId(exAttrId);
				exAttrId++;

				System.out.print(leve3Map.get(c2.getName() + c3.getName())
						.getId() + c2.getName() + ":" + array[3] + ": ");
				String[] values = array[4].split("、");
				for (String value : values) {
					AttrValue attrValue = new AttrValue();
					attrValue.setAttrId(attr.getId());
					attrValue.setValue(value);
					attrValue.setId(num++);
					listAttrValues.add(attrValue);
				}
				listAttrs.add(attr);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println(listAttrs.size());
		exAttrToDB(listAttrs);
		exAttrValueToDB(listAttrValues);
	}

	public void exAttrValueToDB(List<AttrValue> listAttrValues)
			throws IOException, SQLException {
		Connection conn = getConnection();
		String sql = "INSERT INTO `extended_attr_value` (id,extends_attr_id,value,position) VALUES (?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		AttrValue preAttrValue = null;
		int position = 1;
		for (AttrValue attr : listAttrValues) {
			if (preAttrValue != null
					&& attr.getAttrId().intValue() != preAttrValue.getAttrId()
							.intValue()) {
				position = 1;
			}
			ps.setInt(1, attr.getId());
			ps.setInt(2, attr.getAttrId());
			ps.setString(3, attr.getValue());
			ps.setInt(4, position);
			ps.addBatch();
			preAttrValue = attr;
			position++;
			System.out.println(position);
		}
		ps.executeBatch();
		ps.close();
		conn.close();
	}

	private void printSummary(List<Category> cats) {
		System.out.println("一级 类目 " + cats.size());
		int size2 = 0;
		int size3 = 0;

		for (Category c1 : cats) {
			size2 += c1.getChildren().size();
			for (Category c2 : c1.getChildren()) {
				size3 += c2.getChildren().size();
			}
		}
		System.out.println("二级 类目 " + size2);
		System.out.println("三级 类目 " + size3);
	}

	private void intoDB(List<Category> cats) throws IOException, SQLException {
		Connection conn = getConnection();
		String sql = "INSERT INTO `category` (`id`, `name`, `level`, `parent_id`, `position`,trade) VALUES (?,?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);

		// insert level one;
		for (Category category : cats) {
			addBatch(category, ps);
		}
		// insert level 2;
		for (Category category : cats) {
			for (Category c2 : category.getChildren()) {
				addBatch(c2, ps);
			}
		}
		// level 3
		for (Category category : cats) {
			for (Category c2 : category.getChildren()) {
				for (Category c3 : c2.getChildren()) {
					addBatch(c3, ps);
				}
			}
		}

		ps.executeBatch();
		ps.close();
		conn.close();
	}

	private void attrToDB(List<Attr> list) throws IOException, SQLException {
		Connection conn = getConnection();
		String sql = "INSERT INTO `goods_attr` (id, `name`, category_id, position,need_img) VALUES (?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		Attr preAttr = null;
		int position = 1;
		for (Attr attr : list) {
			if (preAttr != null
					&& preAttr.getCatagoryId().intValue() != attr
							.getCatagoryId().intValue()) {
				position = 1;
			}
			ps.setInt(1, attr.getId());
			ps.setString(2, attr.getName());
			ps.setInt(3, attr.getCatagoryId());
			ps.setInt(4, position);
			if (attr.getName().contains("颜色")) {
				ps.setInt(5, 1);
			} else {
				ps.setInt(5, 0);
			}
			ps.addBatch();
			preAttr = attr;
			position++;
		}
		ps.executeBatch();
		ps.close();
		conn.close();
	}

	private void exAttrToDB(List<Attr> list) throws IOException, SQLException {
		Connection conn = getConnection();
		String sql = "INSERT INTO `extended_attr` (id, `name`, category_id, position) VALUES (?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		Attr preAttr = null;
		int position = 1;
		for (Attr attr : list) {
			if (preAttr != null
					&& preAttr.getCatagoryId().intValue() != attr
							.getCatagoryId().intValue()) {
				position = 1;
			}
			ps.setInt(1, attr.getId());
			ps.setString(2, attr.getName());
			ps.setInt(3, attr.getCatagoryId());
			ps.setInt(4, position);
			ps.addBatch();
			preAttr = attr;
			position++;
		}
		ps.executeBatch();
		ps.close();
		conn.close();
	}

	private void addBatch(Category category, PreparedStatement ps)
			throws SQLException {
		ps.setInt(1, category.getId());
		ps.setString(2, category.getName());
		ps.setInt(3, category.getLevel());
		ps.setInt(4, category.getParentID());
		ps.setInt(5, category.getPosition());
		ps.setInt(6, trade);
		ps.addBatch();

	}

	private void genID(List<Category> cats) {
		int i = trade * 100 + 1;
		int position = 1;
		for (Category category : cats) {
			category.setId(i);
			category.setParentID(trade);
			category.setPosition(position);
			position++;
			category.setLevel(1);
			i++;
			int j = 1;
			System.out.println(category.getName() + ":"
					+ category.getChildren());
			for (Category cat2 : category.getChildren()) {
				cat2.setId(cat2.getParent().getId() * 1000 + j);
				cat2.setParentID(cat2.getParent().getId());
				cat2.setPosition(j);
				cat2.setLevel(2);
				j++;
				int k = 1;
				for (Category cat3 : cat2.getChildren()) {
					cat3.setId(cat3.getParent().getId() * 1000 + k);
					cat3.setParentID(cat3.getParent().getId());
					cat3.setPosition(k);
					cat3.setLevel(3);
					k++;
				}
			}
		}
	}

	private void print(String append, List<Category> cats) {
		if (cats == null) {
			return;
		}
		for (Category category : cats) {
			System.out.println(append + category.getId() + "  "
					+ category.getName());
			print(append + append, category.getChildren());

		}
	}

	public Connection getConnection() throws IOException {
		MySQLDB db = new MySQLDB(
				"jdbc:mysql://10.0.16.115:3306/qianbing_debug", "dev-c",
				"f2c!@#qwe");
		Connection conn = db.getConnection();
		return conn;
	}

}
