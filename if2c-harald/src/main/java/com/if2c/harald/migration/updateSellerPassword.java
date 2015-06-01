package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.if2c.harald.beans.Seller;
import com.if2c.harald.tools.Security;
import com.if2c.harald.db.Config;

/**
 * 修改商家密码,不发送邮件
 * 
 * @author zhw <br>
 *         Created at 2014年6月23日
 */
public class updateSellerPassword extends ImageMigrationTask {

	public updateSellerPassword() throws FileNotFoundException,
			IOException {
		super();
	}


//	public static final String FILE_PATH = "E:/updatePasswordBak";// 帐号密码保存路径
	public static final String FILE_NAME = "updateBatch_0826";// 文件名


	private List<String[]> list;

//	public static final String EXPORT_FILE = "E:/批量导入/updateSellerPassword_1026.csv";


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
		System.out.println(list.size()-1);

		for (int i = 0; i < list.size(); i++) {
			Seller seller = new Seller();

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}
			if (!array[0].isEmpty()) {
				seller.setAccountName(array[0].trim());
			}
			if(!array[1].isEmpty()){
				seller.setPassword(array[1].trim());
			}
			SellerList.add(seller);

		}
		return SellerList;
	}

	private CSVReader getExportSellerCSVReader() throws FileNotFoundException {
		return new CSVReader(new FileReader(getExport_file()));
	}

	private void updatePassword(Connection conn, List<Seller> sellerList) throws SQLException
			 {
		String sql = "UPDATE seller SET password=? WHERE accountName=?";

		PreparedStatement ps = conn.prepareStatement(sql);



		conn.setAutoCommit(false);

		for (Seller seller : sellerList) {

//			String password = Security.getRandomPassword();
//			seller.setPassword(password);
			
			try {
				ps.setString(1, seller.getPassword());
				ps.setString(2, seller.getAccountName());
				ps.execute();


//			psEmail.setString(1, seller.getAccountName());
//			ResultSet rs = null;
//			String email = null;
//			String contactPerson = null;
//			rs = psEmail.executeQuery();
//			while (rs.next()) {
//				email = rs.getString("email");
//				contactPerson = rs.getString("contactPerson");
//			}
//			seller.setEmail(email);

//			if (seller.getEmail() == null) {
//				System.out.println("账号 " + seller.getAccountName()
//						+ " 不存在或没有邮箱信息");
//				continue;
//			}
					ReadWriteTextFile.writeFile(seller, getFile_path(), FILE_NAME, "",
							"", "", "");
					System.out.println(seller.getAccountName() + ","
							+ seller.getPassword());

				conn.commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();

	
			}



		}

		ps.close();


	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		updateSellerPassword task = new updateSellerPassword();
		task.run();
		System.exit(0);
//		List<Seller> list=task.read();
//		System.out.println(list.size());
	}

}
