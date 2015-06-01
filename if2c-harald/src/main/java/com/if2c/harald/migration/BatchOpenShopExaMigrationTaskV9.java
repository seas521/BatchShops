package com.if2c.harald.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;

import com.if2c.harald.beans.Seller;
import com.if2c.harald.beans.SellerFile;
import com.if2c.harald.tools.Security;
/**
 * 出口商家创建建帐户、子账号以及开店
 *  出口商家创建建帐户、子账号以及开店
 * [针对克拉玛依商家  开店二级类目多个（规则例：[201001,201009][208007]）]
 * 
 * @author 
 *         Created at 2014年6月20日
 */
public class BatchOpenShopExaMigrationTaskV9 extends ImageMigrationTask {

	public BatchOpenShopExaMigrationTaskV9() throws FileNotFoundException,
			IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	List<String[]> list = null;
//	public static final String EXPORT_FILE ="/var/exagoods_file/重庆企业/欧亚批量开户表.xlsx";
//	public static final String FILE_PATH = "/var/exagoods_file/重庆企业/克拉玛依重庆开店帐号密码备份";// 帐号密码保存路径
	public static final String FILE_NAME = "新建账号密码";// 文件名
	public static final String ERROR_FILE_NAME = "开店失败商家";// 开店失败商家文件名
//	public static final String ERROR_IMG_NAME = "资质文件夹为空的商家";//没有资质文件的商家
//	public static final String ERROR_IMG="上传失败的图片";//上传失败的图片
//	public static final String IMG_PATH="/var/exagoods_file/重庆企业/资质";
	
	public static final String[] SELLER_FILE={"营业执照" , "组织机构代码", "税务登记证" ,"法人身份证"};

	public List<String[]> getList() {
		return list;
	}
	public void setList(List<String[]> list) {
		this.list = list;
	}

//	public static String getExportFile() {
//		return EXPORT_FILE;
//	}

	public void run() throws SQLException {
		conn = getConnection();
		List<Seller> SellerList;
		
		try {
			
			try {
				SellerList = reader(new File(getExport_file()));
				if (SellerList != null) {
				createSellerBrandShop(conn, SellerList);
			}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			DB_exa_export dax=new DB_exa_export();
			try {
				dax.exportRun(conn);
			} catch (Exception e) {
				System.out.println("运费模板初始化失败");
				e.printStackTrace();
			}

//		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			conn.close();
		}

	}

	private void createSellerBrandShop(Connection conn, List<Seller> sellerList)
			throws SQLException, IOException {

		// create seller
		String createSellerSQL = "INSERT INTO `seller` (`accountName`,  `password`, `companyName`, `companyAddress`, `email`, `contactPerson`, `contactPhone`, `delivery_country_id`,`operating_mode`,`payment_destination`,`business_location`,`bank_location`,`trade`,`city`,`seller_country_id`) VALUES (?,?,?, ?, ?, ?, ?, '10', '1','1' , '1' , '1' , '2' , '"+getCity()+"','10')";
		//createSellerSQL city=4字段代表重庆商家
//		String createSubAccountSQL = "INSERT INTO `seller_subaccount` (`name`,  `password`,  `type`,  `seller_id`,  `email`,  `note`) VALUES (?,?,?,?,?,'')";
		String createBrandSQL = "INSERT INTO `brand` (`name`,  `country_id`,  `trade`,  `color`) VALUES (?,'10', '2', '')";
		String createShopSQL = "INSERT INTO `shop` (`name`,  `seller_id`,`type`,  `trade`,  operator_id,`zh_name`) VALUES (?,?,?, '2', '"+getOperator_id()+"',?)";
		String createShopBrandSQL = "INSERT INTO `shop_brand` (`shop_id`,  `brand_id`) VALUES (?,?)";
		String createShopDeductionSQL = "INSERT INTO `shop_deduction` (`shop_id`,  `category_id`,  `deduction`) VALUES (?,?,?)";
//		String createSellerFileSQL="INSERT INTO `seller_file` ( seller_id , file, type , is_del )VALUES( ?,?,?,1) ;";

		PreparedStatement ps = conn.prepareStatement(createSellerSQL);
//		PreparedStatement psSubAccount = conn
//				.prepareStatement(createSubAccountSQL);
		PreparedStatement psBrand = conn.prepareStatement(createBrandSQL);
		PreparedStatement psShop = conn.prepareStatement(createShopSQL);
		PreparedStatement psShopBrand = conn
				.prepareStatement(createShopBrandSQL);
		PreparedStatement psShopDeduction = conn
				.prepareStatement(createShopDeductionSQL);
//		PreparedStatement psSellerFile = conn
//				.prepareStatement(createSellerFileSQL);


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
//				psSubAccount.setString(1, seller.getSubAccountName());
//				psSubAccount.setString(2, password1);
//				psSubAccount.setInt(3, 1);
//				psSubAccount.setInt(4, id);
//				psSubAccount.setString(5, seller.getEmail());
//				psSubAccount.execute();
				/*System.out.println(seller.getSubAccountName() + ","
						+ seller.getPassword());*/

				// pb
				String password2 = Security.getRandomPassword();
				//seller.setPassword(password2);
				seller.setSubAccountName(seller.getAccountName() + ":" + "pb");
				String pbAccount=seller.getSubAccountName();
//				psSubAccount.setString(1, seller.getSubAccountName());
//				psSubAccount.setString(2, password2);
//				psSubAccount.setInt(3, 2);
//				psSubAccount.setInt(4, idd);
//				psSubAccount.setString(5, seller.getEmail());
//				psSubAccount.execute();
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
				psShop.setString(4, seller.getShopName());
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

//				if (seller.getCatLev2Id().equals("1")) {
//					String getCatLv2 = "SELECT id FROM category where parent_id=?";
//					PreparedStatement psGetCatLv2 = conn
//							.prepareStatement(getCatLv2);
//					psGetCatLv2.setString(1, seller.getCatLev1Id());
//
//					ResultSet Crs = null;
//					Crs = psGetCatLv2.executeQuery();
//					while (Crs.next()) {
//						int catId = Crs.getInt("id");
//						psShopDeduction.setInt(1, sdid);
//						psShopDeduction.setLong(2, catId);
//						psShopDeduction.setString(
//								3,
//								seller.getDeduction().substring(0,
//										seller.getDeduction().length() - 1));
//						psShopDeduction.execute();
//					}
//				} else {
				//开通全类目
					String getCategoryLv2 = "select id from category where id LIKE '______' and `status`=1";
					PreparedStatement psGetCategoryLv2 = conn
							.prepareStatement(getCategoryLv2);
					ResultSet crs = null;
					crs = psGetCategoryLv2.executeQuery();
					
					while (crs.next()) {
						int catId = crs.getInt("id");
						psShopDeduction.setInt(1, sdid);
						psShopDeduction.setLong(2, catId);
						psShopDeduction.setString(
								3,seller.getDeduction());
						psShopDeduction.execute();
					}

				
				conn.commit();
				System.out.println(seller.getAccountName() + ","
						+ seller.getPassword());
				System.out.println(fyAccount + ","
						+ password1);
				System.out.println(pbAccount + ","
						+ password2);
				ReadWriteTextFile.writeFile(seller, getFile_path(), FILE_NAME,
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
				ReadWriteTextFile.writeErrorFile(errorMessage, getFile_path(), ERROR_FILE_NAME);
				/*System.err.println(seller.getAccountName() + " 失败"
						+ e.getMessage());*/

			}
		}
		System.out.print("执行成功 " + count + " 条");
		ps.close();
//		psSubAccount.close();
		psBrand.close();
		psShop.close();
		psShopBrand.close();
		psShopDeduction.close();

	}
	public List<Seller> reader(File file) throws Exception {
		ExcelReader excel = new ExcelReader(file);
		ArrayList<HashMap<String, String>> list = excel.reader();
		HashMap<String, String> titleMap = list.get(0);
		list.remove(titleMap);
		List<Seller> SellerList= new ArrayList<Seller>();
		for (int i = 0; i < list.size(); i++) {
			Seller seller = new Seller();
			int n=i+1;
			HashMap<String, String> map= list.get(i);
			
			try {
				if(map.get("商家账户（登录ID，不用填写if2c_）")!=null&&map.get("商家账户（登录ID，不用填写if2c_）")!=""){
					seller.setAccountName(map.get("商家账户（登录ID，不用填写if2c_）").trim());
				}
				if(map.get("商家账户（登录ID，不用填写if2c_）")==null||map.get("商家账户（登录ID，不用填写if2c_）")==""){
					throw new Exception("第" + n + "个商家账户为空");
				}
				if(map.get("公司名称")!=null&&map.get("公司名称")!=""){
					seller.setCompanyName(map.get("公司名称").trim());
				}
				if(map.get("公司名称")==null||map.get("公司名称")==""){
					throw new Exception("第" + n + "个公司名称为空");
				}
				if(map.get("联系人")!=null&&map.get("联系人")!=""){
					seller.setContactPerson(map.get("联系人").trim());
				}
				if(map.get("联系人")==null||map.get("联系人")==""){
					throw new Exception("第" + n + "个联系人为空");
				}
				if(map.get("联系人电话")!=null&&map.get("联系人电话")!=""){
//					 String regex="^\\d+\\.(\\d)+[Ee]\\+\\d+$";
					String regex="^\\d+\\.(\\d)+[Ee](\\+)?\\d+$";
					 Pattern pat = Pattern.compile(regex); 
					 Matcher m = pat.matcher(map.get("联系人电话"));
					 if(m.find()){
					seller.setContactPhone(new BigDecimal(map.get("联系人电话")).toPlainString());
					 }else{
						 seller.setContactPhone(map.get("联系人电话").trim()); 
					 }
					
				}
				if(map.get("联系人电话")==null||map.get("联系人电话")==""){
					throw new Exception("第" + n + "个联系人电话为空");
				}
				if(map.get("地址")!=null&&map.get("地址")!=""){
					seller.setAddress(map.get("地址").trim());
					
				}
				if(map.get("地址")==null||map.get("地址")==""){
					throw new Exception("第" + n + "个地址为空");
				}
				if(map.get("Email")!=null&&map.get("Email")!=""){
					seller.setEmail(map.get("Email").trim());;
					
				}
				if(map.get("Email")==null||map.get("Email")==""){
					throw new Exception("第" + n + "个公司Email为空");
				}
				if(map.get("品牌名称")!=null&&map.get("品牌名称")!=""){
					seller.setBrandName(map.get("品牌名称").trim());
					
				}
				if(map.get("品牌名称")==null||map.get("品牌名称")==""){
					throw new Exception("第" + n + "个公司品牌名称为空");
				}
				if(map.get("店铺名称")!=null&&map.get("店铺名称")!=""){
					seller.setShopName(map.get("店铺名称").trim());
					
				}
				if(map.get("店铺名称")==null||map.get("店铺名称")==""){
					throw new Exception("第" + n + "个公司店铺名称为空");
				}
//				if(map.get("二级类目ID")!=null){
//					seller.setCatLev2Id(new BigDecimal(map.get("二级类目ID")).toBigInteger().toString());
//					
//				}
//				if(map.get("二级类目ID")==null){
//					throw new Exception("第" + n + "个公司二级类目ID为空");
//				}
				if(map.get("扣点")!=null&&map.get("扣点")!=""){
					System.out.println(map.get("扣点"));
					seller.setDeduction(String.valueOf((int)(Double.valueOf(map.get("扣点"))*100)));
					
				}else{
					seller.setDeduction("0");;
				}
				SellerList.add(seller);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String error=map.get("商家账户（登录ID，不用填写if2c_）")+"的信息不全"+e.getMessage();
				e.printStackTrace();
				ReadWriteTextFile.writeErrorFile(error, getFile_path(), ERROR_FILE_NAME);
			}
		}
		
		return SellerList;
	}

	public static void main(String[] args) throws FileNotFoundException,
	IOException, SQLException {
		BatchOpenShopExaMigrationTaskV9 task=new BatchOpenShopExaMigrationTaskV9();
		
		task.run();
		System.exit(0);



	}
	

}
