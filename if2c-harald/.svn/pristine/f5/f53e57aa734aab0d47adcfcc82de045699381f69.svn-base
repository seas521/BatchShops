package com.if2c.harald.migration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.if2c.harald.tools.DateUtils;
import com.if2c.harald.tools.ExcelHelper;

public class ImportCommentsTask extends ImageMigrationTask {
	private static final String COMMENT_FILE = "D:\\www\\箱包评价.xlsx";
	private static final String Order_id = "1908";
	public List<String> users = null;

	public ImportCommentsTask() throws FileNotFoundException, IOException {
		super();
	}

	public void run() throws Exception {
		List<Comment> comments = readComments();
		genRandomTime(comments);
		getAllUsers();
		genRandomUser(comments);

		for (Comment comment : comments) {
			System.out.println(comment);
		}
		addComments(comments);
	}

	private void addComments(List<Comment> comments) {
		String sql = "INSERT INTO `goods_comment` (`user_id`, `series_id`, `goods_id`, `comment`, `publish_time`, `is_quintessence`, `order_id`, `reply_to`, `star`, `favor`, `append_to`) VALUES (?,?, ?, ?, ?, 0, ?, NULL, 5, 0, NULL);";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (Comment comment : comments) {
				ps.setString(1, comment.user);
				ps.setString(2, comment.spu);
				ps.setString(3, comment.sku);
				ps.setString(4, comment.content);
				ps.setString(5, comment.date);
				ps.setString(6, Order_id);
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
			
			String sql1="UPDATE `goods_series` SET `comment_num`=`comment_num`+1 WHERE  `id`=?";
			ps = conn.prepareStatement(sql1);
			for (Comment comment : comments) {
				ps.setString(1, comment.spu);
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getAllUsers() {
		String sql = "select id from users where reg_date < '2014-01-25'";
		PreparedStatement ps = null;
		List<String> idList = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				idList.add(id);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		users = idList;
	}

	private void genRandomUser(List<Comment> comments) {
		Random random = new Random();
		for (Comment comment : comments) {
			int index = random.nextInt(users.size() - 1);
			String ramdonUser = users.get(index);
			comment.user = ramdonUser;
		}
	}

	// generate the date from "from time" to now
	// 10：00-23：00
	private Date genRandomDate(Date fromDate) {
		Calendar from = Calendar.getInstance();
		from.setTimeInMillis(fromDate.getTime() + 1000 * 60 * 60 * 24 * 2);

		Calendar now = Calendar.getInstance();

		Calendar result = from;
		Random random = new Random();
		// gen date
		{
			int dayRange = (int) ((now.getTimeInMillis() - from
					.getTimeInMillis()) / (1000 * 60 * 60 * 24));

			int days = random.nextInt(dayRange) % (dayRange - 0 + 1) + 0;
			result.set(Calendar.DAY_OF_YEAR, result.get(Calendar.DAY_OF_YEAR)
					+ days);
		}
		// gen time 10：00-23：00
		{
			int hour = random.nextInt(23) % (23 - 10 + 1) + 10;
			int minute = random.nextInt(60) % (60 - 0 + 1) + 0;
			int second = random.nextInt(60) % (60 - 0 + 1) + 0;
			result.set(Calendar.HOUR_OF_DAY, hour);
			result.set(Calendar.MINUTE, minute);
			result.set(Calendar.SECOND, second);
		}
		return result.getTime();
	}

	private void genRandomTime(List<Comment> comments) {
		String sql = "select series_id,on_sale_time from goods where id=?";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Comment comment : comments) {
			try {
				ps.setString(1, comment.sku);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					comment.spu = rs.getString("series_id");
					comment.goodsCreateDate = rs.getString("on_sale_time");
				}
				Date randomDate = genRandomDate(DateUtils
						.strToDatehhmmss(comment.goodsCreateDate));
				comment.date = DateUtils.dateToStr(randomDate,
						DateUtils.datePatternWithHMS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Comment> readComments() {
		List<String[]> list = ExcelHelper.readExcelContent(COMMENT_FILE);
		List<Comment> comments = new ArrayList<Comment>();

		for (int i = 1; i < list.size(); i++) {
			String[] array = list.get(i);
			Comment com = new Comment();
			com.sku = array[0];
			com.content = array[1];
			com.date = array[2];
			com.user = array[3];
			comments.add(com);
		}
		return comments;
	}

	public static void main(String[] args) throws Exception {
		ImportCommentsTask task = new ImportCommentsTask();
		task.run();
	}

	class Comment {
		String spu;
		String sku;
		String content;
		String date;
		String user;
		String goodsCreateDate;

		@Override
		public String toString() {
			return "Comment [spu=" + spu + ", sku=" + sku + ", content="
					+ content + ", date=" + date + ", goodsCreateDate="
					+ goodsCreateDate + ", user=" + user + "]";
		}
	}
}
