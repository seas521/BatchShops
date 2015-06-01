package com.if2c.harald.tools;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

	public static void main(String[] args) {
		/*String excelFilePath="D:\\1234.xlsx";
		List<String[]> list = ExcelHelper.readExcelContent(excelFilePath);
		System.out.println(list);*/
	}

	public static List<String[]> readExcelContent(String excelFilePath) {
		List<String[]> list = new ArrayList<String[]>();
		XSSFWorkbook wordbook = null;
		try {
			wordbook = new XSSFWorkbook(new FileInputStream(excelFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (wordbook == null) {
			return null;
		}
		XSSFSheet sheet = wordbook.getSheetAt(0);
		XSSFRow row = null;
		XSSFCell cell = null;
		// 最大列数
		int max = getMaxCellNum(sheet);
		String[] str = null;
		String info = "";
		// 行数
		int num = sheet.getLastRowNum();
		for (int i = 0; i <= num; i++) {
			str=new String[max];
			row = sheet.getRow(i);
			// 每行的列数
			// int cellNum=row.getLastCellNum();
			for (int j = 0; j < max; j++) {
				cell = row.getCell(j);
				if(cell==null||"".equals(cell)){
					info="";
				}else{
					cell.setCellType(cell.CELL_TYPE_STRING);
					if (cell.getCellType() == 1) {
						info = cell.getRichStringCellValue().getString();
					} else if (cell.getCellType() == 0) {
						BigDecimal num1 = new BigDecimal(String.valueOf(cell
								.getNumericCellValue()));
						info = String.valueOf(num1);
					}
				}
				str[j] = info;
			}
			list.add(str);
		}
		
		return list;
	}

	// 获得excel表里列数的最大值
	private static int getMaxCellNum(XSSFSheet sheet) {
		XSSFRow row = null;
		List<Integer> list = new ArrayList<Integer>();
		// 行数
		int num = sheet.getLastRowNum();
		for (int i = 0; i <= num; i++) {
			row = sheet.getRow(i);
			// 每行的列数
			int cellNum = row.getLastCellNum();
			list.add(cellNum);
		}
		// 将list集合中的数据，按从小到大排序
		Collections.sort(list);
		int max = list.get(list.size() - 1);
		return max;
	}
}
