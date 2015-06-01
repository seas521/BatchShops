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

import com.if2c.harald.beans.Seller;
/**
 * 修改子账号密码，不存在子账号则新建并初始化密码
 * 
 * @author zhw <br>
 *         Created at 2014年6月30日
 */
public class updateSubaccount extends ImageMigrationTask {

	public updateSubaccount() throws FileNotFoundException,
			IOException {
		super();
	}

	private List<String[]> list;
	public static final String EXPORT_FILE = "E:/批量导入/updateSubaccount.csv";

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
				updateSubaccountPasswordOrCreate(conn, SellerList);
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

		for (int i = 1; i < list.size(); i++) {
			Seller seller = new Seller();

			String[] array = list.get(i);
			if (array.length == 0) {
				continue;
			}
			if (!array[0].isEmpty()) {
				seller.setAccountName(array[0].trim());
			}
			SellerList.add(seller);

		}
		return SellerList;
	}

	private CSVReader getExportSellerCSVReader() throws FileNotFoundException {
		return new CSVReader(new FileReader(EXPORT_FILE));
	}

	private void updateSubaccountPasswordOrCreate(Connection conn, List<Seller> sellerList)
			throws SQLException, IOException {
		String sql = "UPDATE seller_subaccount SET password=? WHERE seller_id=? AND type=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		String createSubAccountSQL = "INSERT INTO `seller_subaccount` (`name`,  `password`,  `type`,  `seller_id`,  `email`,  `note`) VALUES (?,?,?,?,?,'')";
		PreparedStatement psInsert = conn.prepareStatement(createSubAccountSQL);
		String forSellerId="SELECT id,email FROM seller where accountName=?";
		PreparedStatement psForSellerId = conn.prepareStatement(forSellerId);
		String exist="SELECT count(1) FROM seller_subaccount WHERE seller_id=?";
		PreparedStatement existPs = conn.prepareStatement(exist);
        int count=0;
        int failCount=0;
        int noSubCount=0;
        int size=0;
		conn.setAutoCommit(false);
		for (Seller seller : sellerList) {
			int id=0;
			psForSellerId.setString(1, seller.getAccountName());
			ResultSet rs = null;
			rs = psForSellerId.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
				seller.setEmail(rs.getString("email"));
			}if(id==0){
				System.out.print("账号 "+seller.getAccountName()+" 不存在！\n");
				failCount++;
				continue;
			}
			existPs.setInt(1, id);
			ResultSet result = null;
			result=existPs.executeQuery();
			if(result.next()){
				size=result.getInt(1);
			}if(size==0){
				System.out.print("账号 "+seller.getAccountName()+" 没有子账号！\n");
				noSubCount++;
				
				seller.setSubAccountName(seller.getAccountName()+":fy");
				psInsert.setString(1, seller.getSubAccountName());
				psInsert.setString(2, "fy123456");
				psInsert.setInt(3, 1);
				psInsert.setInt(4, id);
				psInsert.setString(5, seller.getEmail());
				psInsert.execute();
				
				seller.setSubAccountName(seller.getAccountName()+":pb");
				psInsert.setString(1, seller.getSubAccountName());
				psInsert.setString(2, "pb123456");
				psInsert.setInt(3, 2);
				psInsert.setInt(4, id);
				psInsert.setString(5, seller.getEmail());
				psInsert.execute();
				
				/*System.out.print("账号 "+seller.getAccountName()+" 没有子账号！\n");
				noSubCount++;*/
				continue;
				
			}
			
			
			System.out.print("账号 "+seller.getAccountName()+" 修改成功！\n");
			count++;
			ps.setString(1, "pb123456");
			ps.setInt(2, id);
			ps.setInt(3, 2);
			ps.execute();
			ps.setString(1, "fy123456");
			ps.setInt(2, id);
			ps.setInt(3, 1);
			ps.execute();
		}
		conn.commit();
		ps.close();
		psForSellerId.close();
		System.out.print("修改成功"+count+"个账号\n");
		System.out.print(failCount+"个账号不存在\n");
		System.out.print(noSubCount+"个账号没有子账号,已经新建并初始密码\n");
		
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		updateSubaccount task = new updateSubaccount();
		task.run();
		System.exit(0);
	}

}
