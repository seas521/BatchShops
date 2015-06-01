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

import com.if2c.harald.beans.AttributeShopAcvr;
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
public class BatchUpdateGoodAttribute extends ImageMigrationTask {

	public BatchUpdateGoodAttribute() throws FileNotFoundException,
			IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	List<String[]> list = null;
//	public static final String EXPORT_FILE ="/var/exagoods_file/重庆企业/欧亚批量开户表.xlsx";
//	public static final String FILE_PATH = "/var/exagoods_file/重庆企业/克拉玛依重庆开店帐号密码备份";// 帐号密码保存路径
	public static final String FILE_NAME = "新建账号密码";// 文件名
	public static final String ERROR_FILE_NAME = "开店失败商家";// 开店失败商家文件名
	public static final String ERROR_IMG_NAME = "资质文件夹为空的商家";//没有资质文件的商家
	public static final String ERROR_IMG="上传失败的图片";//上传失败的图片
//	public static final String IMG_PATH="/var/exagoods_file/重庆企业/资质";


	public List<String[]> getList() {
		return list;
	}
	public void setList(List<String[]> list) {
		this.list = list;
	}

	public void run() throws SQLException {
		conn = getConnection();
		List<AttributeShopAcvr> acvrList;
		
		try {
			
			try {
				acvrList = reader(new File(getExport_file()));
				if (acvrList != null) {
				createSellerBrandShop(conn, acvrList);
			}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		} finally {
			conn.close();
		}

	}

	private void createSellerBrandShop(Connection conn, List<AttributeShopAcvr> acvrList)
			throws SQLException, IOException {

		String selectName2Sql="SELECT id FROM  attribute where shop_id=0 and name =?";
		

		PreparedStatement psSleect = conn.prepareStatement(selectName2Sql);
		int seqId=1;
		conn.setAutoCommit(false);
		int count = acvrList.size();
		for (AttributeShopAcvr acvr : acvrList) {
			try {
				if(acvr.getACVR2()!=null&&acvr.getACVR2()!=""){
					String updateGoodsAcvrSQL="update goods_attribute_category_value_relation "
							+ "set attribute_category_value_relation_id="+acvr.getACVR2()+" where attribute_category_value_relation_id="+acvr.getACVR1() +";";
					ReadWriteTextFile.writeErrorFile(updateGoodsAcvrSQL, getFile_path(), ERROR_IMG);
				}else{
					if(acvr.getValue2()!=null&&acvr.getValue2()!=null){
						psSleect.setString(1,acvr.getValue2() );
						ResultSet rs = psSleect.executeQuery();
						int attrId=0;
						if(rs.next()){
							attrId = rs.getInt(1);
						}
						psSleect.close();	
					}
				}
				
				
				conn.commit();
				
				
			} catch (Exception e) {
				count--;
				conn.rollback();

			}
		}
		System.out.print("执行成功 " + count + " 条");
		psSleect.close();
	}
	public List<AttributeShopAcvr> reader(File file) throws Exception {
		ExcelReader excel = new ExcelReader(file);
		ArrayList<HashMap<String, String>> list = excel.reader();
		HashMap<String, String> titleMap = list.get(0);
		list.remove(titleMap);
		List<AttributeShopAcvr> acvrList= new ArrayList<AttributeShopAcvr>();
		for (int i = 0; i < list.size(); i++) {
			AttributeShopAcvr acvrRelation = new AttributeShopAcvr();
			int n=i+1;
			HashMap<String, String> map= list.get(i);
			try{
				
				if(map.get("类目1")!=null&&map.get("类目1")!=""){
					acvrRelation.setCategory1(map.get("类目1").trim());
				}
				if(map.get("类目1")==null||map.get("类目1")==""){
					throw new Exception("第" + n + "个商家账户为空");
				}
				if(map.get("属性名1")!=null&&map.get("属性名1")!=""){
					acvrRelation.setName1(map.get("属性名1").trim());
				}
				if(map.get("属性名1")==null||map.get("属性名1")==""){
					throw new Exception("第" + n + "属性名1");
				}
				if(map.get("ACVR1")!=null&&map.get("ACVR1")!=""){
					acvrRelation.setACVR1(map.get("ACVR1").trim());
				}
				if(map.get("ACVR1")==null||map.get("ACVR1")==""){
					throw new Exception("第" + n + "个ACVR1为空");
				}
				if(map.get("属性值1")!=null&&map.get("属性值1")!=""){
					acvrRelation.setValue1(map.get("属性值1").trim());
				}
				if(map.get("属性值1")==null||map.get("属性值1")==""){
					throw new Exception("第" + n + "个属性值1为空");
				}
				if(map.get("类目2")!=null&&map.get("类目2")!=""){
					acvrRelation.setCategory2(map.get("类目2").trim());
				}
				if(map.get("类目2")==null||map.get("类目2")==""){
					continue;
				}
				if(map.get("属性名2")!=null&&map.get("属性名2")!=""){
					acvrRelation.setName2(map.get("属性名2").trim());
				}
				if(map.get("属性名2")==null||map.get("属性名2")==""){
					continue;
				}
				
				acvrRelation.setACVR2(map.get("ACVR2").trim());
				
				if(map.get("属性值2")!=null&&map.get("属性值2")!=""){
					acvrRelation.setValue2(map.get("属性值2").trim());
				}
				if(map.get("属性值2")==null||map.get("属性值2")==""){
					continue;
				}
				
				acvrList.add(acvrRelation);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String error=map.get("商家账户（登录ID，不用填写if2c_）")+"的信息不全"+e.getMessage();
				e.printStackTrace();
				ReadWriteTextFile.writeErrorFile(error, getFile_path(), ERROR_FILE_NAME);
			}
		}
		
		return acvrList;
	}

	
	public static void main(String[] args) throws FileNotFoundException,
	IOException, SQLException {
		BatchUpdateGoodAttribute task=new BatchUpdateGoodAttribute();
		
		task.run();
		System.exit(0);


	}
	

}
