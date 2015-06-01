package com.if2c.harald.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.if2c.harald.beans.SellerFile;

	/**
	 * 出口商家创建建帐户、子账号以及开店
	 *  出口商家创建建帐户、子账号以及开店
	 * [针对克拉玛依商家  开店二级类目多个（规则例：[201001,201009][208007]）]
	 * 
	 * @author 
	 *         Created at 2014年6月20日
	 */
public class BatchOpenShopUploadImg extends ImageMigrationTask {

	public BatchOpenShopUploadImg() throws FileNotFoundException,
				IOException {
			super();
			// TODO Auto-generated constructor stub
		}
		List<String[]> list = null;
//		public static final String EXPORT_FILE ="/var/exagoods_file/重庆企业/欧亚批量开户表.xlsx";
//		public static final String FILE_PATH = "/var/exagoods_file/重庆企业/克拉玛依重庆开店帐号密码备份";// 帐号密码保存路径
		public static final String FILE_NAME = "新建账号密码batch0823";// 文件名
		public static final String ERROR_FILE_NAME = "开店失败商家batch0823";// 开店失败商家文件名
		public static final String ERROR_IMG_NAME = "资质文件夹为空的商家batch0823";//没有资质文件的商家
		public static final String ERROR_IMG="上传失败的图片";//上传失败的图片
//		public static final String IMG_PATH="/var/exagoods_file/重庆企业/资质/";
		
		public static final String[] SELLER_FILE={"营业执照" , "组织机构代码", "税务登记证" ,"法人身份证"};

		public List<String[]> getList() {
			return list;
		}
		public void setList(List<String[]> list) {
			this.list = list;
		}

//		public static String getExportFile() {
//			return EXPORT_FILE;
//		}

		public void run() throws SQLException {
			conn = getConnection();
			List<String> sellerName;
			
			try {
				
				try {
					sellerName = reader(new File(getImg_path()));
					if (sellerName != null) {
					createSellerBrandShop(conn, sellerName);
				}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

			} finally {
				conn.close();
			}

		}
		private void createSellerBrandShop(Connection conn, List<String> sellerName)
				throws SQLException, IOException {

			// create seller
			String createSellerFileSQL="INSERT INTO `seller_file` ( seller_id , file, type , is_del )VALUES( ?,?,?,1) ;";

			PreparedStatement psSellerFile = conn
					.prepareStatement(createSellerFileSQL);

			conn.setAutoCommit(false);
			int count = sellerName.size();
			for (String sellername : sellerName) {
				
					// seller
					String accountName="exa_"+sellername.trim();

					int sid = 0;
					String sellerId = "SELECT id FROM seller where accountName=?";
					PreparedStatement psSellerId = conn.prepareStatement(sellerId);
					psSellerId.setString(1, accountName);
					ResultSet rs = null;
					rs = psSellerId.executeQuery();
					while (rs.next()) {
						sid = rs.getInt("id");
					}
					psSellerId.close();
					if(sid!=0){
					//seller_file商家注册信息
						String count1="SELECT count(*) FROM `seller_file` where seller_id=? AND type=?";
						PreparedStatement psCountImg=conn.prepareStatement(count1);
						List<SellerFile> fileList=new ArrayList<SellerFile>();
						fileList =sellerFileList(sellername);
						if(fileList.size()!=0){
							for(SellerFile s:fileList){
								int countImg=0;
								psCountImg.setInt(1,sid);
								psCountImg.setShort(2,(short) s.getType());
								ResultSet rsNum=psCountImg.executeQuery();
								while(rsNum.next()){
									countImg=rsNum.getInt(1);
								}
								rsNum.close();
							
								if(countImg==0){
									if(s.getFile()!=null){
										psSellerFile.setInt(1, sid);
										psSellerFile.setString(2, s.getFile());
										psSellerFile.setShort(3, (short) s.getType());
										psSellerFile.execute();
									}else{
										String errorMessage="账号  " + accountName+" 的"+ SELLER_FILE[s.getType()-1]+"信息上传失败";
										ReadWriteTextFile.writeErrorFile(errorMessage, getFile_path(), ERROR_IMG);
									}
								}else{
									String errorMessage="账号  " + accountName+" 的"+ SELLER_FILE[s.getType()-1]+"img exists";
									ReadWriteTextFile.writeErrorFile(errorMessage, getFile_path(), ERROR_IMG);
								}
							}
						}else{
							String errorMessage="账号  " + accountName+ " 资质文件夹为空";
							ReadWriteTextFile.writeErrorFile(errorMessage, getFile_path(), ERROR_IMG_NAME);
						
						}
					
						psCountImg.close();
					}else{
						String errorMessage="账号  " + accountName+ "not exists ";
						ReadWriteTextFile.writeErrorFile(errorMessage, getFile_path(), ERROR_IMG_NAME);
					}
					conn.commit();
					
				
				
			}
			System.out.print("执行成功 " + count + " 条");
			psSellerFile.close();

		}
		public List<String> reader(File file) throws Exception {
			String s[]=file.list();
			List<String> list=Arrays.asList(s);
			
			return list;
		}

		public void SendEmail(){
			
			
		}
		public List<SellerFile> sellerFileList(String sellerName){
			List<SellerFile> fileList=new ArrayList<SellerFile>();
			String ImgType=null;
			try {
				TestMigrationTask tmt= new TestMigrationTask();
				for(int i=0;i<4;i++){
					if(new File(getImg_path()+"/"+sellerName+"/"+SELLER_FILE[i]+".png").exists()){
						ImgType=".png";
						try {
							System.out.println("图片存在");
							SellerFile sellImg=new SellerFile();
							sellImg.setType(i+1);
							String imgKey=tmt.uplodeImg(getImg_path()+"/"+sellerName+"/"+SELLER_FILE[i]+ImgType);
							sellImg.setFile(imgKey);
							System.out.println(imgKey);
							fileList.add(sellImg);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("图片服务器异常");
							e.printStackTrace();
						}
						
					}else if(new File(getImg_path()+"/"+sellerName+"/"+SELLER_FILE[i]+".JPG").exists()){
						ImgType=".JPG";
						try {
							System.out.println("图片存在");
							SellerFile sellImg=new SellerFile();
							sellImg.setType(i+1);
							String imgKey=tmt.uplodeImg(getImg_path()+"/"+sellerName+"/"+SELLER_FILE[i]+ImgType);
							sellImg.setFile(imgKey);
							System.out.println(imgKey);
							fileList.add(sellImg);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("图片服务器异常");
							e.printStackTrace();
						}
					}else if(new File(getImg_path()+"/"+sellerName+"/"+SELLER_FILE[i]+".jpg").exists()){
						ImgType=".jpg";
						try {
							System.out.println("图片存在");
							SellerFile sellImg=new SellerFile();
							sellImg.setType(i+1);
							String imgKey=tmt.uplodeImg(getImg_path()+"/"+sellerName+"/"+SELLER_FILE[i]+ImgType);
							sellImg.setFile(imgKey);
							System.out.println(imgKey);
							fileList.add(sellImg);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("图片服务器异常");
							e.printStackTrace();
						}
					}
					else{
						System.out.println("商家"+sellerName+"的"+SELLER_FILE[i]+"不存在");
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return fileList;
			
		}
		public static void main(String[] args) throws Exception {
			BatchOpenShopUploadImg task=new BatchOpenShopUploadImg();
			
			task.run();
			System.exit(0);
//			System.out.println(task.getImg_path());
//			System.out.println(task.getImg_path());
//			List<String> seller =task.reader(new File(task.getImg_path()));;
//			System.out.println(seller.size());
//			String s="lidingjun ";
//			String s1="dadasd ";
//			String s2="dadasd";
//			String s3="dadasd　";
//			String s4="dadada ";
//			System.out.println("a"+s.trim()+"a");
//			System.out.println("a"+s1.trim()+"a");
//			System.out.println("a"+s2.trim()+"a");
//			System.out.println("a"+s3.trim()+"a");
//			System.out.println("a"+s4.trim()+"a");

		}
		

	}


