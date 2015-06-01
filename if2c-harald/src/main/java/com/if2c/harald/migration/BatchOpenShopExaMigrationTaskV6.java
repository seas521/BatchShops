package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.if2c.harald.beans.Seller;
import com.if2c.harald.tools.Security;

/**
 * 出口商家创建建帐户、子账号以及开店
 *  出口商家创建建帐户、子账号以及开店
 * [针对重庆商家  开店二级类目多个（规则例：[201001,201009][208007]）]
 * 
 * @author zhw <br>
 *         Created at 2014年7月23日
 */
public class BatchOpenShopExaMigrationTaskV6 extends ImageMigrationTask {

	public BatchOpenShopExaMigrationTaskV6() throws FileNotFoundException,
			IOException {
		super();
	}

	List<String[]> list = null;
	public static final String EXPORT_FILE = "E:/批量导入/batch0723.csv";
	public static final String FILE_PATH = "E:/义乌开店帐号密码备份";// 帐号密码保存路径
	public static final String FILE_NAME = "新建账号密码batch0723";// 文件名
	public static final String ERROR_FILE_NAME = "开店失败商家batch0723";// 开店失败商家文件名

	public List<String[]> getList() {
		return list;
	}

	public void setList(List<String[]> list) {
		this.list = list;
	}

	public static String getExportFile() {
		return EXPORT_FILE;
	}

	public void run() throws SQLException {
		conn = getConnection();
		List<Seller> SellerList;
		try {
			SellerList = read();
			if (SellerList != null) {
				createSellerBrandShop(conn, SellerList);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}

	private void createSellerBrandShop(Connection conn, List<Seller> sellerList)
			throws SQLException, IOException {

		// create seller
		String createSellerSQL = "INSERT INTO `seller` (`accountName`,  `password`, `companyName`, `companyAddress`, `email`, `contactPerson`, `contactPhone`, `delivery_country_id`,`operating_mode`,`payment_destination`,`business_location`,`bank_location`,`trade`,`city`,`seller_country_id`) VALUES (?,?,?, ?, ?, ?, ?, '10', '1','1' , '1' , '1' , '2' , '4','10')";
		//createSellerSQL city=1字段代表义乌商家
		String createSubAccountSQL = "INSERT INTO `seller_subaccount` (`name`,  `password`,  `type`,  `seller_id`,  `email`,  `note`) VALUES (?,?,?,?,?,'')";
		String createBrandSQL = "INSERT INTO `brand` (`name`,  `country_id`,  `trade`,  `color`) VALUES (?,'10', '2', '')";
		String createShopSQL = "INSERT INTO `shop` (`name`,  `seller_id`,`type`,  `trade`,  `operator_id`) VALUES (?,?,?, '2', '191')";
		String createShopBrandSQL = "INSERT INTO `shop_brand` (`shop_id`,  `brand_id`) VALUES (?,?)";
		String createShopDeductionSQL = "INSERT INTO `shop_deduction` (`shop_id`,  `category_id`,  `deduction`) VALUES (?,?,?)";

		PreparedStatement ps = conn.prepareStatement(createSellerSQL);
		PreparedStatement psSubAccount = conn
				.prepareStatement(createSubAccountSQL);
		PreparedStatement psBrand = conn.prepareStatement(createBrandSQL);
		PreparedStatement psShop = conn.prepareStatement(createShopSQL);
		PreparedStatement psShopBrand = conn
				.prepareStatement(createShopBrandSQL);
		PreparedStatement psShopDeduction = conn
				.prepareStatement(createShopDeductionSQL);

		conn.setAutoCommit(false);
		int count = sellerList.size();
		for (Seller seller : sellerList) {
			try {
				// seller
				String password = Security.getRandomPassword();
				seller.setPassword(password);
				seller.setAccountName("exa_" + seller.getAccountName());
				ps.setString(1, seller.getAccountName());
				ps.setString(2, seller.getPassword());
				ps.setString(3, seller.getCompanyName());
				ps.setString(4, seller.getAddress());
				ps.setString(5, seller.getEmail());
				ps.setString(6, seller.getContactPerson());
				ps.setString(7, seller.getContactPhone());
				ps.execute();
				/*System.out.println(seller.getAccountName() + ","
						+ seller.getPassword());*/

				// fy
				String password1 = Security.getRandomPassword();
				//seller.setPassword(password1);
				seller.setSubAccountName(seller.getAccountName() + ":" + "fy");
				String fyAccount=seller.getSubAccountName();
				int id = 0;
				int idd = 0;
				int sid = 0;
				String sellerId = "SELECT id FROM seller where accountName=?";
				PreparedStatement psSellerId = conn.prepareStatement(sellerId);
				psSellerId.setString(1, seller.getAccountName());

				ResultSet rs = null;
				rs = psSellerId.executeQuery();
				while (rs.next()) {
					id = rs.getInt("id");
					idd = rs.getInt("id");
					sid = rs.getInt("id");
				}

				psSellerId.close();
				psSubAccount.setString(1, seller.getSubAccountName());
				psSubAccount.setString(2, password1);
				psSubAccount.setInt(3, 1);
				psSubAccount.setInt(4, id);
				psSubAccount.setString(5, seller.getEmail());
				psSubAccount.execute();
				/*System.out.println(seller.getSubAccountName() + ","
						+ seller.getPassword());*/

				// pb
				String password2 = Security.getRandomPassword();
				//seller.setPassword(password2);
				seller.setSubAccountName(seller.getAccountName() + ":" + "pb");
				String pbAccount=seller.getSubAccountName();
				psSubAccount.setString(1, seller.getSubAccountName());
				psSubAccount.setString(2, password2);
				psSubAccount.setInt(3, 2);
				psSubAccount.setInt(4, idd);
				psSubAccount.setString(5, seller.getEmail());
				psSubAccount.execute();
				/*System.out.println(seller.getSubAccountName() + ","
						+ seller.getPassword());*/

				// brand
				String brandExist = "SELECT id FROM brand WHERE name=?";
				PreparedStatement psBrandExist = conn
						.prepareStatement(brandExist);
				psBrandExist.setString(1, seller.getBrandName());
				ResultSet brs = null;
				int bid = 0;
				brs = psBrandExist.executeQuery();

				while (brs.next()) {
					bid = brs.getInt("id");
				}
				if (bid == 0) {
					psBrand.setString(1, seller.getBrandName());
					psBrand.executeUpdate();
				} else {
					
				}
				psBrandExist.close();

				// shop
				psShop.setString(1, seller.getShopName());
				psShop.setInt(2, sid);
				psShop.setInt(3, 3);// 店铺类型 3专营店
				psShop.execute();

				// shop_brand

				int sbid = 0;
				int brandId = 0;
				int sdid = 0;
				String shopId = "SELECT id FROM shop where name=?";
				PreparedStatement psShopId = conn.prepareStatement(shopId);
				psShopId.setString(1, seller.getShopName());

				ResultSet sbrs = null;
				sbrs = psShopId.executeQuery();
				while (sbrs.next()) {
					sbid = sbrs.getInt("id");
					sdid = sbrs.getInt("id");
				}
				psShopId.close();

				String BrId = "SELECT id FROM brand where name=?";
				PreparedStatement psBrandId = conn.prepareStatement(BrId);
				psBrandId.setString(1, seller.getBrandName());

				ResultSet Brs = null;
				Brs = psBrandId.executeQuery();
				while (Brs.next()) {
					brandId = Brs.getInt("id");
				}
				psBrandId.close();

				psShopBrand.setInt(1, sbid);
				psShopBrand.setInt(2, brandId);
				psShopBrand.execute();

				// shop_deduction

				if (seller.getCatLev2Id().equals("1")) {
					String getCatLv2 = "SELECT id FROM category where parent_id=?";
					PreparedStatement psGetCatLv2 = conn
							.prepareStatement(getCatLv2);
					psGetCatLv2.setString(1, seller.getCatLev1Id());

					ResultSet Crs = null;
					Crs = psGetCatLv2.executeQuery();
					while (Crs.next()) {
						int catId = Crs.getInt("id");
						psShopDeduction.setInt(1, sdid);
						psShopDeduction.setLong(2, catId);
						psShopDeduction.setString(
								3,
								seller.getDeduction().substring(0,
										seller.getDeduction().length() - 1));
						psShopDeduction.execute();
					}
				} else {
					List<String> catList = new ArrayList<String>();
					catList = calculateCategoryLevel2(seller.getCatLev2Id());
					for (String s : catList) {
						psShopDeduction.setInt(1, sdid);
						psShopDeduction.setString(2, s);
						psShopDeduction.setString(
								3,
								seller.getDeduction().substring(0,
										seller.getDeduction().length() - 1));
						psShopDeduction.execute();
					}

				}
				
				conn.commit();
				System.out.println(seller.getAccountName() + ","
						+ seller.getPassword());
				System.out.println(fyAccount + ","
						+ password1);
				System.out.println(pbAccount + ","
						+ password2);
				ReadWriteTextFile.writeFile(seller, FILE_PATH, FILE_NAME,
						fyAccount, password1, pbAccount, password2);
				
			} catch (Exception e) {
				count--;
				conn.rollback();
				String errorMessage;
				if ((e.getMessage().indexOf("Duplicate entry") != -1)
						&& (e.getMessage().indexOf("accountName") != -1)) {
					System.err.println("账号  " + seller.getAccountName()
							+ " 已存在");
					errorMessage="账号  " + seller.getAccountName()+ " 已存在";
				} else if (e
						.getMessage()
						.indexOf(
								"Cannot add or update a child row: a foreign key constraint fails") != -1) {
					System.err.println("账号  " + seller.getAccountName()
							+ " 的二级类目Id有误，存在数据没有的二级类目Id");
					errorMessage="账号  " + seller.getAccountName()
							+ " 的二级类目Id有误，存在数据没有的二级类目Id";
				} else {
					System.err.println(seller.getAccountName() + " 失败"
							+ e.getMessage());
					errorMessage=seller.getAccountName() + " 失败"
							+ e.getMessage();
				}
				ReadWriteTextFile.writeErrorFile(errorMessage, FILE_PATH, ERROR_FILE_NAME);
				/*System.err.println(seller.getAccountName() + " 失败"
						+ e.getMessage());*/

			}
		}
		System.out.print("执行成功 " + count + " 条");
		ps.close();
		psSubAccount.close();
		psBrand.close();
		psShop.close();
		psShopBrand.close();
		psShopDeduction.close();

	}

	public List<Seller> read() throws IOException, SQLException {
		CSVReader reader = getExportSellerCSVReader();
		list = reader.readAll();
		System.out.println(list.size());
		List<Seller> SellerList = new ArrayList<Seller>();

		for (int i = 0; i < list.size(); i++) {
			Seller seller = new Seller();

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}
			int n = i + 1;

			try {
				if (!array[0].isEmpty() && array[0] != null) {
					if (!array[1].isEmpty()) {
						seller.setAccountName(array[1]);
					}
					if (array[1] == null || array[1].isEmpty()) {
						throw new Exception("第" + n + "个商家账户名为空");
					}
					if (!array[2].isEmpty()) {
						seller.setCompanyName(array[2]);
					}
					if (array[2] == null || array[2].isEmpty()) {
						throw new Exception("商家  " + array[1] + " 公司名称为空");
					}
					if (!array[3].isEmpty()) {
						seller.setContactPerson(array[3]);
					}
					if (array[3] == null || array[3].isEmpty()) {
						throw new Exception("商家  " + array[1] + " 联系人为空");
					}
					if (!array[4].isEmpty()) {
						seller.setContactPhone(array[4]);
					}
					if (array[4] == null || array[4].isEmpty()) {
						throw new Exception("商家  " + array[1] + " 联系电话为空");
					}
					if (!array[5].isEmpty()) {
						seller.setAddress(array[5]);
					}
					if (array[5] == null || array[5].isEmpty()) {
						throw new Exception("商家  " + array[1] + " 公司地址为空");
					}
					if (!array[10].isEmpty()) {
						seller.setEmail(array[10]);
					}
					if (array[10] == null || array[10].isEmpty()) {
						throw new Exception("商家 " + array[1] + " Email为空");
					}
					if (!array[12].isEmpty()) {
						seller.setBrandName(array[12]);
					}
					if (array[12] == null || array[12].isEmpty()) {
						throw new Exception("商家 " + array[1] + " 品牌名为空");
					}
					if (!array[13].isEmpty()) {
						seller.setShopName(array[13]);
					}
					if (array[13] == null || array[13].isEmpty()) {
						throw new Exception("商家 " + array[1] + " 店铺名称为空");
					}
					/*if (!array[15].isEmpty()) {
						seller.setCatLev1Id(array[15]);
					}
					if (array[15] == null || array[15].isEmpty()) {
						//throw new Exception("商家 " + array[1] + " 一级类目id为空");
					}*/
					if (!array[14].isEmpty()) {
						seller.setCatLev2Id(array[14]);
					}
					if (array[14] == null || array[14].isEmpty()) {
						throw new Exception("商家 " + array[1] + " 二级类目id为空");
					}
					if (!array[15].isEmpty()) {
						seller.setDeduction(array[15]);
					}
					if (array[15] == null || array[15].isEmpty()) {
						throw new Exception("商家 " + array[1] + " 扣点为空");
					}
					SellerList.add(seller);
				}
			} catch (Exception e) {
				e.printStackTrace();
				SellerList = null;
				break;
				// System.out.println("========================" + i);
			}

		}
		return SellerList;
	}

	public CSVReader getExportSellerCSVReader() throws FileNotFoundException {
		return new CSVReader(new FileReader(EXPORT_FILE));
	}

	public List<String> calculateCategoryLevel2(String input) {
		List<String> catList = new ArrayList<String>();
		int begin = 0;
		int end = 0;
		int mid = 0;
		String code1;
		String code2;
		int cod1;
		int cod2;
        input=input.replaceAll("，", ",");
		for (int j = 0; j < input.length(); j++) {
			if (input.charAt(j) == '[') {
				begin = j;
			}
			if (input.charAt(j) == ',') {
				mid = j;
			}
			if (input.charAt(j) == ']') {
				end = j;
			}
			if (mid != 0 && end != 0) {
				code1 = input.substring(begin + 1, mid);
				cod1 = Integer.valueOf(code1);
				code2 = input.substring(mid + 1, end);
				cod2 = Integer.valueOf(code2);
				for (int cnt = cod1; cnt <= cod2; cnt++) {
					String value = String.valueOf(cnt);
					if (!catList.contains(value)) {
						catList.add(value);
					}
					begin = 0;
					end = 0;
					mid = 0;
				}
			}
			if (mid == 0 && end != 0) {
				String code = input.substring(begin + 1, end);
				if (!catList.contains(code)) {
					catList.add(code);
				}
				begin = 0;
				end = 0;
				mid = 0;

			}
		}
		return catList;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		BatchOpenShopExaMigrationTaskV6 task = new BatchOpenShopExaMigrationTaskV6();
		task.run();
		System.exit(0);
	}
}