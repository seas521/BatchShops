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



import com.if2c.harald.beans.Seller;
import com.if2c.harald.beans.SellerFile;
/**
 * 出口商家创建建帐户、子账号以及开店
 *  出口商家创建建帐户、子账号以及开店
 * [针对克拉玛依商家  开店二级类目多个（规则例：[201001,201009][208007]）]
 * 
 * @author 
 *         Created at 2014年6月20日
 */
public class NumberOfNullImg extends ImageMigrationTask {

	public NumberOfNullImg() throws FileNotFoundException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
//	List<String[]> list = null;
	public static final String EXPORT_1FILE ="E:/temp/批量开户表ok2.xlsx";
//	public static final String EXPORT_FILE = "E:/克拉玛依义乌开户/克拉玛依开户表3279-刘晓.xlsx";
	public static final String FILE_PATH = "E:/temp/义乌开店帐号密码备份";// 帐号密码保存路径
	public static final String FILE_NAME = "新建账号密码batch0822";// 文件名
	public static final String ERROR_FILE_NAME = "开店失败商家batch0822";// 开店失败商家文件名
	public static final String ERROR_IMG_NAME = "资质文件夹为空的商家";//没有资质文件的商家
	public static final String ERROR_IMG="上传失败的图片";//上传失败的图片
	public static final String IMG_PATH="E:/temp/资质/";
	
	public static final String[] SELLER_FILE={"营业执照" , "组织机构代码", "税务登记证" ,"法人身份证"};

//	public List<String[]> getList() {
//		return list;
//	}
//	public void setList(List<String[]> list) {
//		this.list = list;
//	}

	public static String getExportFile() {
		return EXPORT_1FILE;
	}

	public void run() throws SQLException {
		List<Seller> SellerList;
				try {
					SellerList = reader(new File(EXPORT_1FILE));
					if (SellerList != null) {
					createSellerBrandShop(SellerList);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
	}

	private void createSellerBrandShop(List<Seller> sellerList)
			throws SQLException, IOException {
		for (Seller seller : sellerList) {
			
				//seller_file商家注册信息
				List<SellerFile> fileList=new ArrayList<SellerFile>();
				fileList =sellerFileList(seller.getAccountName());
				if(fileList.size()!=0){
					
				}else{
					String errorMessage="账号  " + seller.getAccountName()+ " 资质文件夹为空";
					ReadWriteTextFile.writeErrorFile(errorMessage, FILE_PATH, ERROR_IMG_NAME);
					
				}
				
			}
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
				if(map.get("商家账户（登录ID，不用填写if2c_）")!=null){
					seller.setAccountName(map.get("商家账户（登录ID，不用填写if2c_）"));
				}
				if(map.get("商家账户（登录ID，不用填写if2c_）")==null){
					throw new Exception("第" + n + "个商家账户为空");
				}
				if(map.get("公司名称")!=null){
					seller.setCompanyName(map.get("公司名称"));
				}
				if(map.get("公司名称")==null){
					throw new Exception("第" + n + "个公司名称为空");
				}
				if(map.get("联系人")!=null){
					seller.setContactPerson(map.get("联系人"));
				}
				if(map.get("联系人")==null){
					throw new Exception("第" + n + "个联系人为空");
				}
				if(map.get("联系人电话")!=null){
//					 String regex="^\\d+\\.(\\d)+[Ee]\\+\\d+$";
					String regex="^\\d+\\.(\\d)+[Ee](\\+)?\\d+$";
					 Pattern pat = Pattern.compile(regex); 
					 Matcher m = pat.matcher(map.get("联系人电话"));
					 if(m.find()){
					seller.setContactPhone(new BigDecimal(map.get("联系人电话")).toPlainString());
					 }else{
						 seller.setContactPhone(map.get("联系人电话")); 
					 }
					
				}
				if(map.get("联系人电话")==null){
					throw new Exception("第" + n + "个联系人电话为空");
				}
				if(map.get("地址")!=null){
					seller.setAddress(map.get("地址"));
					
				}
				if(map.get("地址")==null){
					throw new Exception("第" + n + "个地址为空");
				}
				if(map.get("Email")!=null){
					seller.setEmail(map.get("Email"));;
					
				}
				if(map.get("Email")==null){
					throw new Exception("第" + n + "个公司Email为空");
				}
				if(map.get("品牌名称")!=null){
					seller.setBrandName(map.get("品牌名称"));
					
				}
				if(map.get("品牌名称")==null){
					throw new Exception("第" + n + "个公司品牌名称为空");
				}
				if(map.get("店铺名称")!=null){
					seller.setShopName(map.get("店铺名称"));
					
				}
				if(map.get("店铺名称")==null){
					throw new Exception("第" + n + "个公司店铺名称为空");
				}
//				if(map.get("二级类目ID")!=null){
//					seller.setCatLev2Id(new BigDecimal(map.get("二级类目ID")).toBigInteger().toString());
//					
//				}
//				if(map.get("二级类目ID")==null){
//					throw new Exception("第" + n + "个公司二级类目ID为空");
//				}
				if(map.get("扣点")!=null){
					seller.setDeduction(String.valueOf((int)(Double.valueOf(map.get("扣点"))*100)));
					
				}
				if(map.get("扣点")==null){
					seller.setDeduction("0");;
				}
				SellerList.add(seller);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String error=map.get("商家账户（登录ID，不用填写if2c_）")+"的信息不全"+e.getMessage();
				e.printStackTrace();
				ReadWriteTextFile.writeErrorFile(error, FILE_PATH, ERROR_FILE_NAME);
			}
		}
		
		return SellerList;
	}

	public void SendEmail(){
		
		
	}
	public List<SellerFile> sellerFileList(String sellerName){
		List<SellerFile> fileList=new ArrayList<SellerFile>();
		
		
		
			for(int i=0;i<4;i++){
				if(new File(IMG_PATH+sellerName+"/"+SELLER_FILE[i]+".png").exists()){
			
					
					SellerFile sellImg=new SellerFile();
					sellImg.setType(i+1);
					sellImg.setFile("true");
					fileList.add(sellImg);
					
					
				}else if(new File(IMG_PATH+sellerName+"/"+SELLER_FILE[i]+".JPG").exists()){
	
					
						System.out.println("图片存在");
						SellerFile sellImg=new SellerFile();
						sellImg.setType(i+1);

						sellImg.setFile("true");
				
						fileList.add(sellImg);
					
				}else{
					System.out.println("商家"+sellerName+"的"+SELLER_FILE[i]+"不存在");
				}
				
			}
		
		return fileList;
		
	}
	public static void main(String[] args) throws FileNotFoundException,
	IOException, SQLException {
		NumberOfNullImg task=new NumberOfNullImg();
		
		task.run();
		System.exit(0);
//		String[] s= {"1.3231313e10","313133.0","012-31313"};
//		String sellerName="139875216511@qq.com";
//		System.out.println(new File("E:/示例/资质/139875216511@qq.com/"+SELLER_FILE[1]+".png").exists());
//		String s= "1.3983961E10";
//		String regex="^\\d+\\.(\\d)+[Ee]\\+\\d+$";
//		 String regex="^\\d+\\.(\\d)+[Ee](\\+)?\\d+$";
//		String s="0.10";
//		String s="0.00";
//		
//		System.out.println(String.valueOf((int)(Double.valueOf(s)*100)));
//		 Pattern pat = Pattern.compile(regex); 
//		 Matcher m = pat.matcher(s);
//		 if(m.find()){
//		System.out.println((new BigDecimal(s).toPlainString()));
//		 }else{
//			System.out.println(s);
//		 }
	}
	

}
