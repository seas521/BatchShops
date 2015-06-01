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
 * 修改商家密码并发邮件通知
 * 
 * @author zhw <br>
 *         Created at 2014年5月22日
 */
public class UpdateSellerPasswordToEmail extends ImageMigrationTask {

	public UpdateSellerPasswordToEmail() throws FileNotFoundException,
			IOException {
		super();
	}

	public static final String FILE_PATH = "E:/修改商家密码备份";// 帐号密码保存路径
	public static final String FILE_NAME = "修改密码batchtest";// 文件名

	private List<String[]> list;
	public static final String EXPORT_FILE = "E:/批量导入/exist.csv";

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

		for (int i = 0; i < list.size(); i++) {
			Seller seller = new Seller();

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}
			if (!array[0].isEmpty()) {
				seller.setAccountName(array[0]);
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
		String sql = "UPDATE seller SET password=? WHERE accountName=?";
		// String emailQueueSql=
		// "INSERT INTO email_queue (`status`,  `subject`, `to`, `from`, `body`) VALUES(?,?,?,?,?)";
		String emailSql = "SELECT email,contactPerson FROM seller WHERE accountName=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		PreparedStatement psEmail = conn.prepareStatement(emailSql);
		// PreparedStatement psEmailQueue =
		// conn.prepareStatement(emailQueueSql);

		conn.setAutoCommit(false);
		for (Seller seller : sellerList) {
			String password = Security.getRandomPassword();
			seller.setPassword(password);
			ps.setString(1, seller.getPassword());
			ps.setString(2, seller.getAccountName());
			ps.execute();

			psEmail.setString(1, seller.getAccountName());
			ResultSet rs = null;
			String email = null;
			String contactPerson = null;
			rs = psEmail.executeQuery();
			while (rs.next()) {
				email = rs.getString("email");
				contactPerson = rs.getString("contactPerson");
			}
			seller.setEmail(email);
			/* psEmailQueue.execute(); */

			if (seller.getEmail() == null) {
				System.out.println("账号 " + seller.getAccountName()
						+ " 不存在或没有邮箱信息");
				continue;
			}
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("user", seller.getAccountName());
			dataMap.put("password", seller.getPassword());
			dataMap.put("contactPerson", contactPerson);
			boolean emailOk = false;
			emailOk = notifyByEmail(email, dataMap, conn);
			if (emailOk) {
				ReadWriteTextFile.writeFile(seller, FILE_PATH, FILE_NAME, "",
						"", "", "");
				System.out.println(seller.getAccountName() + ","
						+ seller.getPassword());
			} else {
				System.out.println("账号 " + seller.getAccountName() + " 密码修改失败");
			}

			conn.commit();
		}
	}

	private boolean notifyByEmail(String email, Map<String, String> dataMap,
			Connection conn) {
		PreparedStatement ps = null;
		if (email != null && !email.isEmpty()) {
			String insertsql = "INSERT INTO `email_queue` (`subject`, `to`, `from`, `body`) VALUES ('义乌全球销平台商家后台账号密码', ?, 'Noreply-service@exagoods.com', '"
					+ Config.getTemplate("batchChangePasswordToEmail.html",
							dataMap) + "');";
			try {
				ps = conn.prepareStatement(insertsql);
				ps.setString(1, email);
				ps.execute();

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		UpdateSellerPasswordToEmail task = new UpdateSellerPasswordToEmail();
		task.run();
		System.exit(0);
	}

}
