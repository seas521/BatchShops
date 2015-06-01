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
public class BatchUpdateShop extends ImageMigrationTask {

	public BatchUpdateShop() throws FileNotFoundException,
			IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	List<String[]> list = null;
//	public static final String EXPORT_FILE ="/var/exagoods_file/重庆企业/欧亚批量开户表.xlsx";
//	public static final String FILE_PATH = "/var/exagoods_file/重庆企业/克拉玛依重庆开店帐号密码备份";// 帐号密码保存路径
	public static final String FILE_NAME = "新建账号密码batch0823";// 文件名
	public static final String ERROR_FILE_NAME = "开店失败商家batch0823";// 开店失败商家文件名
	public static final String ERROR_IMG_NAME = "资质文件夹为空的商家batch0823";//没有资质文件的商家
	public static final String ERROR_IMG="上传失败的图片";//上传失败的图片
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
			
		} finally {
			conn.close();
		}

	}

	private void createSellerBrandShop(Connection conn, List<Seller> sellerList)
			throws SQLException, IOException {

	
		
		
		String updateSellerSql="update seller SET city =2 WHERE id=?;";
		String updateShopSql="UPDATE shop s SET s.operator_id=195 WHERE s.seller_id=?";
		PreparedStatement psUpdateSeller=conn.prepareStatement(updateSellerSql);
		PreparedStatement psUpdateShop=conn.prepareStatement(updateShopSql);
		
		conn.setAutoCommit(false);
		int count = sellerList.size();
		for (Seller seller : sellerList) {
			try {
				// seller
				
				seller.setAccountName("exa_" + seller.getAccountName());
				
				int sid = 0;
				String sellerId = "SELECT id FROM seller where accountName=?";
				PreparedStatement psSellerId = conn.prepareStatement(sellerId);
				psSellerId.setString(1, seller.getAccountName());
				
				ResultSet rs = null;
				rs = psSellerId.executeQuery();
				while (rs.next()) {
					sid = rs.getInt("id");
				}

				psSellerId.close();
				
				if(sid!=0){
					psUpdateSeller.setInt(1, sid);
					psUpdateSeller.execute();
					psUpdateShop.setInt(1, sid);
					psUpdateShop.execute();
				}else{
					throw new Exception( seller.getAccountName()+ "的商家账户为空");
					
				}
				
				conn.commit();
				System.out.println(seller.getAccountName() );
				
			} catch (Exception e) {
				count--;
				conn.rollback();
				String errorMessage=e.getMessage();
				ReadWriteTextFile.writeErrorFile(errorMessage, getFile_path(), ERROR_FILE_NAME);
				/*System.err.println(seller.getAccountName() + " 失败"
						+ e.getMessage());*/

			}
		}
		System.out.print("执行成功 " + count + " 条");
		psUpdateSeller.close();
		psUpdateShop.close();
	
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
		BatchUpdateShop task=new BatchUpdateShop();
		
		task.run();
		System.exit(0);


	}
	

}
