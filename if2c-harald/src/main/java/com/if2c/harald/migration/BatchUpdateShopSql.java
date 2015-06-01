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
public class BatchUpdateShopSql extends ImageMigrationTask {

	public BatchUpdateShopSql() throws FileNotFoundException,
			IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	List<String[]> list = null;
//	public static final String EXPORT_FILE ="/var/exagoods_file/重庆企业/欧亚批量开户表.xlsx";
//	public static final String FILE_PATH = "/var/exagoods_file/重庆企业/克拉玛依重庆开店帐号密码备份";// 帐号密码保存路径
	public static final String FILE_NAME = "新建账号密码batch0823";// 文件名
	public static final String ERROR_FILE_NAME = "寻找商家失败";// 开店失败商家文件名
	public static final String ERROR_IMG_NAME = "资质文件夹为空的商家batch0823";//没有资质文件的商家
	public static final String UPDATE_SQL="更改五区SQL";//上传失败的图片
//	public static final String IMG_PATH="/var/exagoods_file/重庆企业/资质";

	public List<String[]> getList() {
		return list;
	}
	public void setList(List<String[]> list) {
		this.list = list;
	}

	public void run() throws SQLException {
		conn = getConnection();
		List<Seller> SellerList;
		
		try {
			
			try {
				SellerList = read();
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
	private List<Seller> read() throws IOException {
		CSVReader reader = getExportSellerCSVReader();
		list = reader.readAll();
		List<Seller> SellerList = new ArrayList<Seller>();
		System.out.println(list.size());

		for (int i = 0; i < list.size(); i++) {
			Seller seller = new Seller();

			String[] array = list.get(i);
			if (array.length == 0|| array[0].contains(":") ) {
				continue;
			}
			if (!array[0].isEmpty()) {
				seller.setAccountName(array[0].trim());
			}
//			if(!array[1].isEmpty()){
//				seller.setPassword(array[1]);
//			}
			SellerList.add(seller);

		}
		return SellerList;
	}
	private CSVReader getExportSellerCSVReader() throws FileNotFoundException {
		return new CSVReader(new FileReader(getExport_file()));
	}
	private void createSellerBrandShop(Connection conn, List<Seller> sellerList)
			throws SQLException, IOException {

	
		
		
//		String updateSellerSql="update seller SET city =1 WHERE id=";
		String updateShopSql="UPDATE shop s SET s.operator_id=182 WHERE s.seller_id=";
//		String updateSellerPWDSql="update seller SET password ='exagoods123' WHERE id=";
//		PreparedStatement psUpdateSeller=conn.prepareStatement(updateSellerSql);
//		PreparedStatement psUpdateShop=conn.prepareStatement(updateShopSql);
		
		conn.setAutoCommit(false);
		int count = sellerList.size();
		for (Seller seller : sellerList) {
			try {
				// seller
				seller.setAccountName(seller.getAccountName());
				int sid = 0;
				String sellerId = "SELECT id FROM seller where accountName=?";
				PreparedStatement psSellerId = conn.prepareStatement(sellerId);
				psSellerId.setString(1,seller.getAccountName());
				
				ResultSet rs = null;
				rs = psSellerId.executeQuery();
				while (rs.next()) {
					sid = rs.getInt("id");
				}

				psSellerId.close();
				
				if(sid!=0){
//					ReadWriteTextFile.writeErrorFile(updateSellerSql+sid+";", getFile_path(), UPDATE_SQL);
					ReadWriteTextFile.writeErrorFile(updateShopSql+sid+";", getFile_path(), UPDATE_SQL);
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
		
	
	}
	


	
	public static void main(String[] args) throws FileNotFoundException,
	IOException, SQLException {
		BatchUpdateShopSql task=new BatchUpdateShopSql();
		
		task.run();
		System.exit(0);


	}
	

}
