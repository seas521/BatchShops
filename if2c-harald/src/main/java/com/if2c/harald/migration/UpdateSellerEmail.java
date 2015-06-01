package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.if2c.harald.beans.Seller;
import com.if2c.harald.tools.Security;

public class UpdateSellerEmail extends ImageMigrationTask {

	public UpdateSellerEmail() throws FileNotFoundException,
			IOException {
		super();
	}

	private List<String[]> list;
	public static final String EXPORT_FILE = "E:/批量导入/updateEmailPasswordTest.csv";

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
			SellerList = read();
			if (SellerList != null) {
				updatePassword(conn, SellerList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}

	private List<Seller> read() throws IOException {
		CSVReader reader = getExportSellerCSVReader();
		list = reader.readAll();
		List<Seller> SellerList = new ArrayList<Seller>();
		System.out.println(list.size());

		for (int i = 1; i < list.size(); i++) {
			Seller seller = new Seller();

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}
			if (!array[0].isEmpty()) {
				seller.setAccountName(array[0]);
			}
			if (!array[2].isEmpty()) {
				seller.setEmail(array[2]);
			}
			SellerList.add(seller);

		}
		return SellerList;
	}

	private CSVReader getExportSellerCSVReader() throws FileNotFoundException {
		return new CSVReader(new FileReader(EXPORT_FILE));
	}

	private void updatePassword(Connection conn, List<Seller> sellerList)
			throws SQLException, IOException {
		String sql = "UPDATE seller SET email=? WHERE accountName=?";
		PreparedStatement ps = conn.prepareStatement(sql);

		conn.setAutoCommit(false);
		for (Seller seller : sellerList) {
			/*String password = Security.getRandomPassword();
			seller.setPassword(password);*/
			ps.setString(1, seller.getEmail());
			ps.setString(2, seller.getAccountName());
			ps.execute();
			/*String email = null;
			seller.setEmail(email);*/
			conn.commit();
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		UpdateSellerEmail task = new UpdateSellerEmail();
		task.run();
		System.exit(0);
	}

}
