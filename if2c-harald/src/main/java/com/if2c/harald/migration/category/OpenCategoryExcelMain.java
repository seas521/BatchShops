package com.if2c.harald.migration.category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlException;

import com.if2c.harald.migration.category.model.Category;
import com.if2c.harald.migration.category.model.CategoryRoot;
import com.if2c.harald.migration.category.model.ExtendedAttribute;
import com.if2c.harald.migration.category.model.GoodsAttribute;
import com.if2c.harald.migration.category.model.ObjectFactory;

public class OpenCategoryExcelMain {
	private static final String type="export";
	private static final String  CATEGORY_EXCEL = "category/"+type+"/category_bag.xlsx";
	private static final String OUTPUT_XML = "category/"+type+"/category_bag.xml";
	public static long EXTEND_START_ID = 2005666l;
	public static long ATTRIBUTE_START_ID = 2000083l;
	public static Long categoryId = 245l;
	public static List<String> cs2 = new ArrayList<String>();
	public static List<String> cs1 = new ArrayList<String>();

	public void loadXls(String filePath) {
		try {
			InputStream input = new FileInputStream("D://test.xls");
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			// Iterate over each row in the sheet
			Iterator rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				System.out.println("Row #" + row.getRowNum());
				// Iterate over each cell in the row and print out the cell"s
				// content
				Iterator cells = row.cellIterator();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();
					System.out.println("Cell #" + cell.getCellNum());
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						System.out.println(cell.getNumericCellValue());
						break;
					case HSSFCell.CELL_TYPE_STRING:
						System.out.println(cell.getStringCellValue());
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN:
						System.out.println(cell.getBooleanCellValue());
						break;
					case HSSFCell.CELL_TYPE_FORMULA:
						System.out.println(cell.getCellFormula());
						break;
					default:
						System.out.println("unsuported sell type");
						break;
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ��ȡxlsx�ı�
	 * 
	 * @param filePath
	 */
	public void loadXlsxText(String filePath) {
		File inputFile = new File("D://test.xlsx");
		try {
			POITextExtractor extractor = ExtractorFactory
					.createExtractor(inputFile);
			System.out.println(extractor.getText());
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OpenXML4JException e) {
			e.printStackTrace();
		} catch (XmlException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡoffice 2007 xlsx
	 * 
	 * @param filePath
	 */
	public List<CategoryString> loadXlsx(String filePath, int index, int num) {
		// ���� XSSFWorkbook ����strPath �����ļ�·��
		XSSFWorkbook xwb = null;
		try {
			xwb = new XSSFWorkbook(CATEGORY_EXCEL);
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�����");
			e.printStackTrace();
		}
		// ��ȡ��һ�±������
		XSSFSheet sheet = xwb.getSheetAt(index);
		xwb.getSheetAt(1);
		// ���� row��cell
		XSSFRow row;
		String cell = null;
		String first = null;
		String second = null;
		String third = null;
		String foth = null;
		String attr = null;
		String attrValue = null;
		// ѭ���������е�����
		// System.out.println(sheet.getPhysicalNumberOfRows());
		// System.out.println(row.getFirstCellNum());
		// sheet.getPhysicalNumberOfRows()sheet.getFirstRowNum()+1
		List<CategoryString> css = new ArrayList<CategoryString>();
		for (int i = sheet.getFirstRowNum() + 1; i < sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			CategoryString cs = new CategoryString();
			for (int j = row.getFirstCellNum(); j < num; j++) {
				if (row.getCell(j) == null
						|| "".equals(row.getCell(j).toString())
						|| "无".equals(row.getCell(j).toString())) {
					cell = "null";
				} else {
					cell = row.getCell(j).toString().trim().replace("\n", "");
				}
				// cell = row.getCell(j).toString().trim().replace("\n",
				// "").replace("&amp;", "&");
				if (isMergedRegion(sheet, i, j)) {

					if (getMergedRegionValue(sheet, i, j) == null
							|| "".equals(getMergedRegionValue(sheet, i, j))
							|| "无".equals(getMergedRegionValue(sheet, i, j))) {
						cell = "null";
					} else {
						cell = getMergedRegionValue(sheet, i, j).toString()
								.trim().replace("\n", "");
					}

				}
				/*
				 * if(j == row.getFirstCellNum() && !(cell==null ||
				 * "".equals(cell)|| "无".equals(cell))){ first=cell.toString();
				 * } if(j == 1 && !(cell==null || "".equals(cell) ||
				 * "无".equals(cell))){ second=cell.toString(); } if(j == 2 &&
				 * !(cell==null || "".equals(cell)|| "无".equals(cell))){
				 * third=cell.toString(); }
				 * 
				 * if(j == 3 && !(cell==null || "".equals(cell)||
				 * "无".equals(cell))){ foth=cell.toString(); }
				 * 
				 * if(cell==null || "".equals(cell)|| "无".equals(cell)){
				 * 
				 * cell="null";
				 * 
				 * if(j == row.getFirstCellNum() && !(
				 * "".equals(row.getCell(1).toString().trim()) &&
				 * "".equals(row.getCell(2).toString().trim()) &&
				 * "".equals(row.getCell(3).toString().trim())) ){ cell=first; }
				 * if(j == 1 && !( "".equals(row.getCell(2).toString().trim())
				 * && "".equals(row.getCell(3).toString().trim()))){
				 * cell=second; } if(j == 2 && !(
				 * "".equals(row.getCell(3).toString().trim()) )){ cell=third; }
				 * if(j == 3 ){ if(isMergedRegion(sheet,i,j)
				 * &&getMergedRegionValue(sheet,i,j)!=null){ cell=foth; }
				 * 
				 * }
				 * 
				 * }
				 */

				cs.put(cell);
				/*
				 * if(j == 3 ){ if("null".equals(cs.getFirst()) &&
				 * "null".equals(cs.getSecond()) && "null".equals(cs.getThird()
				 * ) && "null".equals(cs.getFouth())){ //XSSFRow row1 =
				 * sheet.getRow(i-1); if(isMergedRegion(sheet,i,j-1)){ for (int
				 * j2 = css.size()-1; j2 > 0; j2--) {
				 * if(!"null".equals(css.get(j2).getThird())){
				 * cs.setThird(css.get(j2).getThird()); break; } }
				 * //cs.setThird(row1.getCell(2).toString().trim().replace("\n",
				 * "")); }else if(isMergedRegion(sheet,i,j-2)){ for (int j2 =
				 * css.size()-1; j2 > 0; j2--) {
				 * if(!"null".equals(css.get(j2).getSecond())){
				 * cs.setSecond(css.get(j2).getSecond()); break; } }
				 * //cs.setSecond(css.get(css.size()-1).getSecond());
				 * //cs.setThird(row1.getCell(1).toString().trim().replace("\n",
				 * "")); }else if(isMergedRegion(sheet,i,j-3)){ for (int j2 =
				 * css.size()-1; j2 > 0; j2--) {
				 * if(!"null".equals(css.get(j2).getFirst())){
				 * cs.setFirst(css.get(j2).getFirst()); break; } }
				 * cs.setFirst(css.get(css.size()-1).getFirst());
				 * //cs.setThird(row1.getCell(0).toString().trim().replace("\n",
				 * "")); } }
				 * 
				 * }
				 */
				// System.out.print(cell + "\t");
			}
			css.add(cs);
			// System.out.println(i);
			// System.out.println(i + ":" + cs.toString());
			/*
			 * if (index == 1 && i>1300 && i<1600) { System.out.println(i + ":"
			 * + cs.toString()); } else { //System.out.println(i); }
			 */
		}
		return css;
	}

	public static void changeCsc(List<CategoryString> css) {
		for (int i = 0; i < css.size(); i++) {
			css.get(i).clear();
		}
		/*
		 * List<CategoryString> newcss = new ArrayList<CategoryString>(); for
		 * (CategoryString cs :css) { if (!(cs.getFirst() == null
		 * &&cs.getSecond() == null && cs.getThird() == null && cs.getFouth() ==
		 * null && cs.getAttribute() == null && cs.getAttributeValue() == null))
		 * { newcss.add(cs); } } css=newcss;
		 */
		for (int i = 0; i < css.size(); i++) {
			if (css.get(i).getFirst() == null
					&& (css.get(i).getSecond() != null
							|| css.get(i).getThird() != null || css.get(i)
							.getFouth() != null)) {
				System.out.println("类目空缺1：" + i);
			} else if (css.get(i).getSecond() == null
					&& (css.get(i).getThird() != null || css.get(i).getFouth() != null)) {
				System.out.println("类目空缺2：" + i);
			} else if (css.get(i).getThird() == null
					&& (css.get(i).getFouth() != null)) {
				System.out.println("类目空缺3：" + i);
			}
			if (css.get(i).getAttribute() == null
					&& css.get(i).getAttributeValue() != null) {
				System.out.println("属性不对：" + (i + 2));
			}
			if (css.get(i).getFirst() == null && css.get(i).getSecond() == null
					&& css.get(i).getThird() == null
					&& css.get(i).getFouth() == null
					&& css.get(i).getAttribute() == null
					&& css.get(i).getAttributeValue() == null) {
				System.out.println("都是空：" + i);
				css.remove(css.get(i));
			}
		}

		/*
		 * for (int i = 0; i < css.size(); i++) {
		 * if(css.get(i).getFirst()==null){
		 * System.out.println(css.get(i).toString()); }
		 * 
		 * }
		 */
		for (int i = 0; i < css.size(); i++) {
			if (css.get(i).getFirst() == null) {
				if (css.get(i).getSecond() != null
						|| css.get(i).getThird() != null
						|| css.get(i).getFouth() != null) {
					css.get(i).setFirst(css.get(i - 1).getFirst());
				}
			}
			if (css.get(i).getSecond() == null) {
				if (css.get(i).getThird() != null
						|| css.get(i).getFouth() != null) {
					css.get(i).setSecond(css.get(i - 1).getSecond());
				}
			}
			if (css.get(i).getThird() == null) {
				if (css.get(i).getFouth() != null) {
					css.get(i).setThird(css.get(i - 1).getThird());
				}
			}
			// System.out.println(css.get(i).toString());
		}
	}

	public static List<Category> getCages(List<CategoryString> css) {

		changeCsc(css);
		for (int i = 0; i < css.size(); i++) {
			if (css.get(i).getFirst() == null) {
				// System.out.println(i+":"+css.get(i).toString());
			}

		}
		List<Category> firstLevel = new ArrayList<Category>();
		for (int i = 0; i < css.size(); i++) {
			boolean have = false;
			for (int j = 0; j < firstLevel.size(); j++) {
				try {
					if (firstLevel.get(j).getName()
							.equals(css.get(i).getFirst())) {
						have = true;
					}
				} catch (Exception e) {
					// System.out.println(i+"+"+j);
					throw e;
				}
			}
			if (!have) {
				Category c = new Category();
				c.setName(css.get(i).getFirst());
				c.setLevel((byte) 1);
				c.setId(categoryId++);
				firstLevel.add(c);
			}
		}

		insertInFather(firstLevel, css, 2);
		for (Category c2 : firstLevel) {
			insertInFather(c2.getCategory(), css, 3, c2.getName());
		}
		for (Category c2 : firstLevel) {
			for (Category c3 : c2.getCategory()) {
				insertInFather(c3.getCategory(), css, 4, c3.getName(),
						c2.getName());
			}
		}
		return firstLevel;
	}

	public static void setCs(List<CategoryString> css, List<String> cslist) {
		for (CategoryString c : css) {
			if (!cslist.contains(c.getFirst())) {
				cslist.add(c.getFirst());
			}
			if (!cslist.contains(c.getSecond())) {
				cslist.add(c.getSecond());
			}
			if (!cslist.contains(c.getThird())) {
				cslist.add(c.getThird());
			}
			if (!cslist.contains(c.getFouth())) {
				cslist.add(c.getFouth());
			}
		}
	}

	public static boolean compareCs() {
		boolean flag = true;
		System.out.println("1的size 为：" + cs1.size());
		System.out.println("2的size 为：" + cs2.size());
		for (String css1 : cs1) {
			if (!cs2.contains(css1)) {
				System.out.println("sheet1有sheet2没有：" + css1);
				flag = false;
			}
		}
		for (String css2 : cs2) {
			if (!cs1.contains(css2)) {
				System.out.println("sheet2有sheet1没有：" + css2);
				flag = false;
			}
		}
		return flag;
	}

	public static void setGoodsAttribute(List<Category> categorys,
			List<Category> attrs) {
		// level1
		for (Category at1 : attrs) {
			if (at1.getExtendedAttribute().size() > 0) {
				for (Category c1 : categorys) {
					if (c1.getName().equals(at1.getName())) {
						for (ExtendedAttribute e : at1.getExtendedAttribute()) {
							GoodsAttribute g = new GoodsAttribute();
							g.setName(e.getName());
							g.setGoodsAttributeValue(e
									.getExtendedAttributeValue());
							g.setId(ATTRIBUTE_START_ID++);
							g.setHasImage(e.isIsCheck());
							c1.getGoodsAttribute().add(g);
							// System.out.println(g.getId()+":"+g.getName());
						}

					}

				}
			}
		}
		// level2
		for (Category at1 : attrs) {
			for (Category at2 : at1.getCategory()) {
				if (at2.getExtendedAttribute().size() > 0) {
					for (Category c1 : categorys) {
						for (Category c2 : c1.getCategory()) {
							if (c2.getName().equals(at2.getName())
									&& c1.getName().endsWith(at1.getName())) {
								for (ExtendedAttribute e : at2
										.getExtendedAttribute()) {
									GoodsAttribute g = new GoodsAttribute();
									g.setName(e.getName());
									g.setGoodsAttributeValue(e
											.getExtendedAttributeValue());
									g.setId(ATTRIBUTE_START_ID++);
									g.setHasImage(e.isIsCheck());
									c2.getGoodsAttribute().add(g);
									// System.out.println(g.getId()+":"+g.getName());
								}
							}
						}

					}
				}
			}
		}
		// level3
		for (Category at1 : attrs) {
			for (Category at2 : at1.getCategory()) {
				for (Category at3 : at2.getCategory()) {
					if (at3.getExtendedAttribute().size() > 0) {
						for (Category c1 : categorys) {
							for (Category c2 : c1.getCategory()) {
								for (Category c3 : c2.getCategory()) {
									if (c3.getName().equals(at3.getName())
											&& c2.getName().endsWith(
													at2.getName())
											&& c1.getName().endsWith(
													at1.getName())) {
										for (ExtendedAttribute e : at3
												.getExtendedAttribute()) {
											GoodsAttribute g = new GoodsAttribute();
											g.setName(e.getName());
											g.setGoodsAttributeValue(e
													.getExtendedAttributeValue());
											g.setId(ATTRIBUTE_START_ID++);
											g.setHasImage(e.isIsCheck());
											c3.getGoodsAttribute().add(g);
											// System.out.println(g.getId()+":"+g.getName());
										}

									}
								}
							}
						}
					}
				}
			}
		}
		// level4
		for (Category at1 : attrs) {
			for (Category at2 : at1.getCategory()) {
				for (Category at3 : at2.getCategory()) {
					for (Category at4 : at3.getCategory()) {
						if (at4.getExtendedAttribute().size() > 0) {
							for (Category c1 : categorys) {
								for (Category c2 : c1.getCategory()) {
									for (Category c3 : c2.getCategory()) {
										for (Category c4 : c3.getCategory()) {
											if (c4.getName().equals(
													at4.getName())
													&& c3.getName().endsWith(
															at3.getName())
													&& c2.getName().endsWith(
															at2.getName())
													&& c1.getName().endsWith(
															at1.getName())) {
												for (ExtendedAttribute e : at4
														.getExtendedAttribute()) {
													GoodsAttribute g = new GoodsAttribute();
													g.setName(e.getName());
													g.setGoodsAttributeValue(e
															.getExtendedAttributeValue());
													g.setId(ATTRIBUTE_START_ID++);
													g.setHasImage(e.isIsCheck());
													c4.getGoodsAttribute().add(
															g);
													// System.out.println(g.getId()+":"+g.getName());
												}

											}
										}
									}
								}

							}
						}
					}
				}
			}
		}

	}

	public static void insertAttribute(List<Category> categorys,
			List<CategoryString> css) {

		List<CategoryString> categoryLevel1 = new ArrayList<CategoryString>();
		List<CategoryString> categoryLevel2 = new ArrayList<CategoryString>();
		List<CategoryString> categoryLevel3 = new ArrayList<CategoryString>();
		List<CategoryString> categoryLevel4 = new ArrayList<CategoryString>();
		for (CategoryString cs : css) {
			if (cs.getAttribute() != null) {
				if (cs.getFouth() != null) {
					categoryLevel4.add(cs);
				} else if (cs.getThird() != null) {
					categoryLevel3.add(cs);
				} else if (cs.getSecond() != null) {
					categoryLevel2.add(cs);
				} else if (cs.getFirst() != null) {
					categoryLevel1.add(cs);
				}
			}
		}
		for (Category c : categorys) {
			for (CategoryString c1 : categoryLevel1) {
				if (c1.getFirst().equals(c.getName())) {
					ExtendedAttribute e = new ExtendedAttribute();
					e.setName(c1.getAttribute());
					e.setExtendedAttributeValue(c1.getAttributeValue());
					e.setId(EXTEND_START_ID++);
					e.setIsCheck(c1.isCheck());
					c.getExtendedAttribute().add(e);
				}
			}
		}
		for (Category c : categorys) {
			for (Category c2 : c.getCategory()) {
				for (CategoryString c1 : categoryLevel2) {
					if (c1.getSecond().equals(c2.getName())
							&& c.getName().equals(c1.getFirst())) {
						ExtendedAttribute e = new ExtendedAttribute();
						e.setName(c1.getAttribute());
						e.setExtendedAttributeValue(c1.getAttributeValue());
						e.setId(EXTEND_START_ID++);
						e.setIsCheck(c1.isCheck());
						c2.getExtendedAttribute().add(e);
					}
				}
			}
		}
		for (Category c : categorys) {
			for (Category c2 : c.getCategory()) {
				for (Category c3 : c2.getCategory()) {
					for (CategoryString c1 : categoryLevel3) {
						if (c1.getThird().equals(c3.getName())
								&& c2.getName().equals(c1.getSecond())
								&& c.getName().equals(c1.getFirst())) {
							ExtendedAttribute e = new ExtendedAttribute();
							e.setName(c1.getAttribute());
							e.setExtendedAttributeValue(c1.getAttributeValue());
							e.setId(EXTEND_START_ID++);
							e.setIsCheck(c1.isCheck());
							c3.getExtendedAttribute().add(e);
						}
					}
				}
			}
		}
		for (Category c : categorys) {
			for (Category c2 : c.getCategory()) {
				for (Category c3 : c2.getCategory()) {
					for (Category c4 : c3.getCategory()) {
						for (CategoryString c1 : categoryLevel4) {

							if (c1.getFouth().equals(c4.getName())
									&& c3.getName().equals(c1.getThird())
									&& c2.getName().equals(c1.getSecond())
									&& c.getName().equals(c1.getFirst())) {
								ExtendedAttribute e = new ExtendedAttribute();
								e.setName(c1.getAttribute());
								e.setExtendedAttributeValue(c1
										.getAttributeValue());
								e.setId(EXTEND_START_ID++);
								e.setIsCheck(c1.isCheck());
								c4.getExtendedAttribute().add(e);
							}
						}
					}
				}
			}
		}

	}

	public static void insertInFather(List<Category> firstCategory,
			List<CategoryString> css, int level, String... names) {
		for (Category c : firstCategory) {
			for (CategoryString cs : css) {
				if (c.getName().equals(cs.getCheck(level))) {

					if ("Down&Parkas(羽绒服/棉服)".equals(cs.getNowLevel(level))) {
						System.out.print(1);
					}
					if (level == 4) {
						if (!names[1].equals(cs.getCheck(2))
								|| !names[0].equals(cs.getCheck(3))) {
							continue;
						}
					}
					if (level == 3) {
						if (!names[0].equals(cs.getCheck(2))) {
							continue;
						}
					}

					boolean have = false;
					for (Category c1 : c.getCategory()) {
						if (c1.getName().equals(cs.getNowLevel(level))) {
							have = true;
						}
					}
					if (!have && cs.getNowLevel(level) != null) {
						Category ca = new Category();
						ca.setName(cs.getNowLevel(level));
						ca.setLevel((byte) level);

						c.getCategory().add(ca);
					}
				}
			}
		}
		for (Category c : firstCategory) {
			int b = 1;
			for (int i = 0; i < c.getCategory().size(); i++) {
				c.getCategory().get(i)
						.setId(Long.valueOf(c.getId() + getNext(b++)));
				// System.out.println(c.getCategory().get(i).getId()+":"+c.getCategory().get(i).getName());
			}
		}
	}

	public static String getNext(int a) {
		if (a < 10) {
			return "00" + a;
		} else if (a >= 10 && a < 100) {
			return "0" + a;
		} else if (a >= 100 && a < 1000) {
			return "" + a;
		}
		return null;

	}

	/*
	 * 合并单元格处理--加入list
	 * 
	 * @param sheet
	 * 
	 * @return
	 */
	public void getCombineCell(HSSFSheet sheet, List<CellRangeAddress> list) {
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		// 遍历合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格加入list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			list.add(ca);
		}
	}

	public boolean isMergedRegion(XSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	public String getMergedRegionValue(XSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					if (sheet.getRow(firstRow).getCell(column) == null
							|| "".equals(sheet.getRow(firstRow).getCell(column)
									.toString())) {
						return null;
					}
					return sheet.getRow(firstRow).getCell(column).toString();
				}
			}
		}
		return null;

	}

	/**
	 * 判断单元格是否为合并单元格
	 * 
	 * @param listCombineCell
	 *            存放合并单元格的list
	 * @param cell
	 *            需要判断的单元格
	 * @param sheet
	 *            sheet
	 * @return
	 */
	public static Boolean isCombineCell(List<CellRangeAddress> listCombineCell,
			HSSFCell cell, HSSFSheet sheet) {
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getColumnIndex() <= lastC
					&& cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		OpenCategoryExcelMain readExcel = new OpenCategoryExcelMain();
		List<CategoryString> css = readExcel.loadXlsx("", 0, 7);
		setCs(css, cs1);
		List<Category> firstLevel = getCages(css);
		insertAttribute(firstLevel, css);
		List<CategoryString> css2 = readExcel.loadXlsx("", 1, 6);
		setCs(css2, cs2);
		List<Category> goodsLevel = getCages(css2);
		insertAttribute(goodsLevel, css2);
		setGoodsAttribute(firstLevel, goodsLevel);

		if (!compareCs()) {
			return;
		}
		CategoryRoot root = new CategoryRoot();
		root.getCategory().addAll(firstLevel);
		OutputStream f;
		f = new FileOutputStream(OUTPUT_XML);
		JAXBUtils.marshal(ObjectFactory.class, root, f);

	}
}