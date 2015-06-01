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
import com.if2c.harald.beans.Giftcard;
import com.if2c.harald.beans.Seller;

public class ImportDateTask extends ImageMigrationTask {

	public ImportDateTask() throws FileNotFoundException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static final String FILE_PATH = "E:/111.csv";// 帐号密码保存路径
	List<String[]> list = null;
	public List<String[]> getList() {
		return list;
	}
	public void setList(List<String[]> list) {
		this.list = list;
	}
	
	public void run() throws SQLException {
		conn = getConnection();
		List<Giftcard> GiftcardsList;
		try {
			GiftcardsList = read();
			if (GiftcardsList != null) {
				insert(conn, GiftcardsList);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}
	

	private void insert(Connection conn, List<Giftcard> GiftcardsList2)
			throws SQLException {
		if(GiftcardsList2!=null && GiftcardsList2.size()>0){
			String insertSql = "INSERT INTO `freight_rate` (`logistics_area_id`,  `first_weight`,  `continue_weight`,  `first_freight`,  `continue_freight`,  `currency`,  `min_weight`,  `max_weight`,  `fixed_price`) VALUES (?,'1','1',?,?,'RMB',0,0,0)";
			String selectSql = "SELECT id FROM logistics_area WHERE logistics_id=? AND name=?";

			PreparedStatement psInsert = conn.prepareStatement(insertSql);
			PreparedStatement psSelect = conn.prepareStatement(selectSql);
			
			for(Giftcard giftcard:GiftcardsList2){
				//System.out.println(giftcard.getLogisticsId());
				psSelect.setInt(1, 8);
				
				
				psSelect.setString(2, giftcard.getLogisticsId());
				
				ResultSet rs = null;
				rs=psSelect.executeQuery();
				while (rs.next()) {
					//System.out.println(rs.getInt("id"));
					giftcard.setId(rs.getInt("id"));
					psInsert.setInt(1, giftcard.getId());
					psInsert.setString(2, giftcard.getPrice());
					psInsert.setString(3, giftcard.getPrice());
					psInsert.execute();
				}
				
				
			}
			psInsert.close();
			psSelect.close();
		}
	}
	public List<Giftcard> read() throws IOException, SQLException {
		CSVReader reader = getExportSellerCSVReader();
		list = reader.readAll();
		//System.out.println(list.size());
		List<Giftcard> GiftcardList = new ArrayList<Giftcard>();

		for (int i = 0; i < list.size(); i++) {
			Giftcard giftcard = new Giftcard();

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}

			try {
				giftcard.setLogisticsId(array[0]);
				if(i==0){
					giftcard.setLogisticsId(giftcard.getLogisticsId().substring(1));
				}
				giftcard.setPrice(array[1]);
				GiftcardList.add(giftcard);
				}
			 catch (Exception e) {
				e.printStackTrace();
				GiftcardList = null;
				break;
				// System.out.println("========================" + i);
			}

		}
		return GiftcardList;
	}
	
	
	
	private CSVReader getExportSellerCSVReader() throws FileNotFoundException {
		return new CSVReader(new FileReader(FILE_PATH));
	}
	public static void main(String[] args) throws Exception {
		ImportDateTask task = new ImportDateTask();
         task.run();
         System.exit(0);
        }
	
}
