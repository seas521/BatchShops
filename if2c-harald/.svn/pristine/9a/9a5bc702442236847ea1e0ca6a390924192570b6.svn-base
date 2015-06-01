package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.if2c.harald.beans.FrightRate;

public class ImportFreightRate extends ImageMigrationTask {

	public ImportFreightRate() throws FileNotFoundException, IOException {
		super();
	}
	List<String[]> list = null;
	public static final String EXPORT_FILE = "E:/批量导入/frightRate.csv";
	public List<String[]> getList() {
		return list;
	}
	public void setList(List<String[]> list) {
		this.list = list;
	}
	
	public static String getExportFile() {
		return EXPORT_FILE;
	}
	public void run() throws SQLException{
		conn = getConnection();
		List<FrightRate> frightRate;
		try {
			frightRate=read();
			if(frightRate!=null){
				importFright(conn,frightRate);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings("null")
	private void importFright(Connection conn, List<FrightRate> frightRateList) throws SQLException {
		String insertSql = "INSERT INTO `freight_rate` (`logistics_area_id`,  `first_weight`,  `continue_weight`,  `first_freight`,  `continue_freight`,  `currency`,  `min_weight`,  `max_weight`,  `fixed_price`) VALUES (?,'0.5','0.5',?,?,'RMB',0,0,0)";
		String selectSql = "SELECT id FROM logistics_area WHERE logistics_id=? AND name=?";

		PreparedStatement psInsert = conn.prepareStatement(insertSql);
		PreparedStatement psSelect = conn.prepareStatement(selectSql);
		
		for(FrightRate frightRate:frightRateList){
			psSelect.setInt(1, 9);
			
			
			psSelect.setString(2, frightRate.getAreaName());
			
			ResultSet rs = null;
			rs=psSelect.executeQuery();
			while (rs.next()) {
				frightRate.setLogisticsId(rs.getInt("id"));
				psInsert.setInt(1, frightRate.getLogisticsId());
				psInsert.setString(2, frightRate.getFirstFreight());
				psInsert.setString(3, frightRate.getContinueFreight());
				psInsert.execute();
			}
			
			
		}
		psInsert.close();
		psSelect.close();
		
	}
	private List<FrightRate> read() throws IOException {
		CSVReader reader = null;
		try {
			reader = getExportSellerCSVReader();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		list = reader.readAll();
		System.out.println(list.size());
		List<FrightRate> frightRateList = new ArrayList<FrightRate>();
		
		for (int i = 0; i < list.size(); i++) {
			FrightRate frightRate = new FrightRate();

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}
			try {
				if (!array[0].isEmpty() && array[0] != null) {
					if (!array[0].isEmpty()) {
						frightRate.setAreaName(array[0]);
						if(i==0){
							frightRate.setAreaName(frightRate.getAreaName().substring(1));
						}
					}
					if (!array[1].isEmpty()) {
						frightRate.setFirstFreight(array[1]);
					}
					if (!array[2].isEmpty()) {
						frightRate.setContinueFreight(array[2]);
					}
					frightRateList.add(frightRate);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				frightRateList = null;
				break;
			}

		}
		
		return frightRateList;
	}

	private CSVReader getExportSellerCSVReader() throws FileNotFoundException {
		return new CSVReader(new FileReader(EXPORT_FILE));
	}
	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		ImportFreightRate task = new ImportFreightRate();
		task.run();
		System.exit(0);
	}

}
