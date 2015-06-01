package com.if2c.harald.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;






import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.if2c.harald.beans.BrandInfo;

/**
 * 数据迁移
 * 
 * @author lh <br>
 *         Created at 2014年9月26日
 */
public class BrandMigrationTask extends ImageMigrationTask {

	public BrandMigrationTask() throws FileNotFoundException,
			IOException {
		super();
	}

	List<String[]> list = null;
	public static final String EXPORT_FILE = "D:\\品牌1925.xlsx";

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
		List<BrandInfo> List;
		try {
			List = readXls(getExportFile());
			if (List != null) {
				operate(conn, List);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}
	public void operate(Connection conn,List<BrandInfo> list){
		int count=0;
		try{
		for(BrandInfo b:list){
			String UpdateNameSql="update brand set name= ? where id=?";
			String UpdateShopBrang="update shop_brand set brand_id= ? where brand_id=?";
			String UpdateBrangStory="update brand_story set brand_id=? where brand_id=?";
			String deleteUserC="delete from user_concern  where brand_id=?";
			String UpdateGoods="update goods set brand_id=? where brand_id=?";
			String UpdateGoodsSeries="update goods_series set brand_id=? where brand_id=?";
			String UpdatePBCG="update promoting_brand_category_goods set brand_id=? where brand_id=?";
			String UpdateOPC="update operation_promoting_category set brand_id=? where brand_id=?";
			String UpdateOrders="update orders set brand_id=? where brand_id=?";
			String deleteBrand="delete from brand where id=?";
			if("删除".equals(b.getNewBrandId())){
				
			}else if(b.getBrandId().equals(b.getNewBrandId())){
				PreparedStatement ps = conn.prepareStatement(UpdateNameSql);
				ps.setString(1, b.getNewName());
				ps.setString(2, b.getBrandId());
				ps.execute();
				ps.close();
			}else if(!b.getBrandId().equals(b.getNewBrandId())){
				PreparedStatement ps1 = conn.prepareStatement(UpdateShopBrang);
				ps1.setString(1, b.getNewBrandId());
				ps1.setString(2, b.getBrandId());
				PreparedStatement ps2 = conn.prepareStatement(UpdateBrangStory);
				ps2.setString(1, b.getNewBrandId());
				ps2.setString(2, b.getBrandId());
				PreparedStatement ps3 = conn.prepareStatement(deleteUserC);
				ps3.setString(1, b.getBrandId());
				PreparedStatement ps4 = conn.prepareStatement(UpdateGoods);
				ps4.setString(1, b.getNewBrandId());
				ps4.setString(2, b.getBrandId());
				PreparedStatement ps5 = conn.prepareStatement(UpdateGoodsSeries);
				ps5.setString(1, b.getNewBrandId());
				ps5.setString(2, b.getBrandId());
				PreparedStatement ps6 = conn.prepareStatement(UpdateOrders);
				ps6.setString(1, b.getNewBrandId());
				ps6.setString(2, b.getBrandId());
				PreparedStatement ps9 = conn.prepareStatement(UpdatePBCG);
				ps9.setString(1, b.getNewBrandId());
				ps9.setString(2, b.getBrandId());
				PreparedStatement ps8 = conn.prepareStatement(UpdateOPC);
				ps8.setString(1, b.getNewBrandId());
				ps8.setString(2, b.getBrandId());
				PreparedStatement ps7 = conn.prepareStatement(deleteBrand);
				ps7.setString(1, b.getBrandId());
				
				ps1.execute();
				ps2.execute();
				ps3.execute();
				ps4.execute();
				ps5.execute();
				ps6.execute();
				ps9.execute();
				ps8.execute();
				ps7.execute();
				ps1.close();
				ps2.close();
				ps3.close();
				ps4.close();
				ps5.close();
				ps6.close();
				ps7.close();
				ps8.close();
				ps9.close();
				count++;
				System.out.println(count+":"+"原ID为"+b.getBrandId()+"改为"+b.getNewBrandId());
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(count);
		}
		System.out.print("更改个数："+count);
	}
	/**
	 * 读取excel2007
	 * @param file
	 * @return
	 */
	public List<BrandInfo> readXls(String file){
		List<BrandInfo> list=new ArrayList<BrandInfo>();
		BrandInfo bi;
		XSSFWorkbook wordbook=null;
		try
		 { 
			wordbook = new XSSFWorkbook(new FileInputStream(file));
		    } 
		catch(Exception e) { 
		      e.printStackTrace(); 
		} 
		 XSSFSheet sheet = wordbook.getSheetAt(0);
		 XSSFRow row =null;
		 XSSFCell cell=null;
		 int length1=sheet.getLastRowNum();//最大行数
		 for(int i=1;i<=length1;i++){
			 bi=new BrandInfo();
			 row=sheet.getRow(i); 
			 bi=setBean(row,cell);
			 list.add(bi);
		 }
		 return list;
	}
	public BrandInfo setBean(XSSFRow row,XSSFCell cell){
		BrandInfo bi=new BrandInfo();
		cell=row.getCell((short) 0);
		bi.setBrandId(getValue(cell));
		cell=row.getCell((short) 1);
		bi.setName((getValue(cell)));
		cell=row.getCell((short) 2);
		bi.setStatus((getValue(cell)));
		cell=row.getCell((short) 3);
		bi.setNewBrandId((getValue(cell)));
		cell=row.getCell((short) 4);
		bi.setNewName((getValue(cell)));
		return bi;
	}
	public String getValue(XSSFCell xssfCell){  
		   if(xssfCell==null||"".equals(xssfCell)){
			   return "";
		   }else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN){  
	    	   xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	    	   String cellValue = xssfCell.toString();
	           // 返回布尔类型的值  
	           return String.valueOf(cellValue);  
	       } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){  
	    	   	 xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		         String cellValue = xssfCell.toString();
	           // 返回数值类型的值  
	           return String.valueOf(cellValue);  
	       } else{  
	           // 返回字符串类型的值  
	           return String.valueOf(xssfCell.getStringCellValue());  
	       }  
	   }

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		BrandMigrationTask task = new BrandMigrationTask();
		task.run();
		System.exit(0);
	}
}
