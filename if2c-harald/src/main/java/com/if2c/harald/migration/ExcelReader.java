package com.if2c.harald.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelReader {
	private File file = null;
	private InputStream fis = null;
	private Workbook excel = null;
	
	/**
	 * @param path 文件地址（带完整路径）
	 * @throws Exception
	 */
	public ExcelReader(String path) throws Exception{  
		this(new File(path));
	}
	/**
	 * @param file 文件类对象
	 * @throws Exception
	 */
	public ExcelReader(File file) throws Exception{
		this.file = file;
		String ext = this.file.getPath().substring(this.file.getPath().lastIndexOf("."));
		this.fis = new FileInputStream(this.file);
		if(ext.equalsIgnoreCase(".xls"))excel = new HSSFWorkbook(this.fis);
		else excel = new XSSFWorkbook(this.fis);
	}
	/**
	 * @param inputStream 文件流
	 * @param flag true:2003;false:2007
	 * @throws Exception
	 */
	public ExcelReader(InputStream inputStream,boolean flag) throws Exception{
		this.fis = inputStream;
		if(flag)excel = new HSSFWorkbook(this.fis);
		else excel = new XSSFWorkbook(this.fis);
	}
	public void close() throws Exception{
		this.fis.close();
	}
	public ArrayList<HashMap<String,String>> reader(){
		return reader(0);
	}
	public ArrayList<HashMap<String,String>> reader(int i){
		ArrayList<HashMap<String,String>> dataMap = new ArrayList<HashMap<String,String>>();
		try {
			HashMap<Integer,String> titleMap = this.readTitleByIndex();
			int count = excel.getNumberOfSheets();
			if(i>count||i<0)throw new Exception("超出范围");
			Sheet sheet = excel.getSheetAt(i);
			int rows = sheet.getPhysicalNumberOfRows();
			if(rows<=0)throw new Exception("无内容");
			Row row = sheet.getRow(0);
			int size = row.getLastCellNum();
			for(int row_id=0;row_id<rows;row_id++){
				row = sheet.getRow(row_id);
				if(row!=null){
					HashMap<String,String> row_data = new HashMap<String,String>();
					for(int index=0;index<size;index++){
						Cell cell = row.getCell(index);
						String value = "";
						if(cell != null){
							switch (cell.getCellType()) {  
	                            case Cell.CELL_TYPE_FORMULA : value = cell.getCellFormula();break;
	                            case Cell.CELL_TYPE_NUMERIC : value ="" +(HSSFDateUtil.isCellDateFormatted(cell)?cell.getDateCellValue():cell.getNumericCellValue());break;  
	                            case Cell.CELL_TYPE_STRING : value = "" + cell.getStringCellValue();break;
	                            case Cell.CELL_TYPE_BOOLEAN :value = "" + cell.getBooleanCellValue();break;  
	                            default:  
                            }
						}
						row_data.put(titleMap.get(index), value);
					}
					dataMap.add(row_data);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataMap;
	}
	public HashMap<String,Integer> readTitle() throws Exception{
		HashMap<String,Integer> map = null;
		Sheet sheet = excel.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		if(rows<=0)throw new Exception("无内容");
		Row row = sheet.getRow(0);
		int size = row.getLastCellNum();
		if(row!=null){
			map = new HashMap<String,Integer>();
			String[] row_data = new String[size];
			for(int index=0;index<size;index++){
				Cell cell = row.getCell(index);
				if(cell == null)throw new Exception("空标题");
				else{
					String value = "";
					switch (cell.getCellType()) {  
                        case Cell.CELL_TYPE_FORMULA : value = cell.getCellFormula();break;
                        case Cell.CELL_TYPE_NUMERIC : value ="" +(HSSFDateUtil.isCellDateFormatted(cell)?cell.getDateCellValue():cell.getNumericCellValue());break;  
                        case Cell.CELL_TYPE_STRING : value = "" + cell.getStringCellValue();break;
                        case Cell.CELL_TYPE_BOOLEAN :value = "" + cell.getBooleanCellValue();break;  
                        default:  
                    }
					row_data[index] = value;
					map.put(value, index);
				}
			}
		}
		return map;
	}
	public HashMap<Integer,String> readTitleByIndex() throws Exception{
		HashMap<String,Integer> map = readTitle();
		HashMap<Integer,String> resultmap =new HashMap<Integer, String>();
		if(map==null)return null;
		for(Entry<String, Integer> entry : map.entrySet()){
			resultmap.put(entry.getValue(), entry.getKey());
		}
		return resultmap;
	}

}
