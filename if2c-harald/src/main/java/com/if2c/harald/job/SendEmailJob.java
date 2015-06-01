package com.if2c.harald.job;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.quartz.DisallowConcurrentExecution;

import com.if2c.harald.db.Config;
import com.if2c.harald.mail.Mailer;
// 支持 annotation配制 配制如下：
// @DBConf(url = "jdbc:mysql://10.0.16.115:3306/if2c", userName = "root", password = "123456")

@DisallowConcurrentExecution
public class SendEmailJob extends JobBase {

	public boolean sendMail() {
		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		String sql = "select * from email_queue where status = 0 and HOUR(timediff(now(),create_time))<24";
		String sqltoCancel = "update email_queue set status = 2 where seq = ?";
		String sqltoSuccess = "update email_queue set status = 1 where seq = ?";
		String from;
		String to;
		String subject;
		String body;
		String cc;
		int id;
		int oneDayLong = 86400000;
		Mailer mailer = Config.getConf().getMailer();
		try {
			conn = this.getConnection();
			if (conn != null) {
				ps1 = conn.prepareStatement(sql);
				rs = ps1.executeQuery();
				ps2 = conn.prepareStatement(sqltoSuccess);
				ps3= conn.prepareStatement(sqltoCancel);
				while (rs.next()) {
					id = rs.getInt("seq");
					if ((System.currentTimeMillis() - rs.getTimestamp(
							"create_time").getTime()) < oneDayLong) {
						from = rs.getString("from");
						to = rs.getString("to");
						cc = rs.getString("cc");
						subject = rs.getString("subject");
						body = rs.getString("body");
						
						try {
							if (mailer
									.sendMail(from, to, subject, body, id, cc)) {
								ps2.setInt(1, id);
								ps2.execute();
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					} else {
						ps3.setInt(1, id);
						ps3.execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != rs) {
				closeRs(rs);
			}
			if (null != ps1) {
				closePs(ps1);
			}
			if (null != ps2) {
				closePs(ps2);
			}
			if (null != ps3) {
				closePs(ps3);
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}
		return true;
	}

	public void run() {
		sendMail();
	}
	public static void main(String[] args){
		SendEmailJob sendEmailJob=new SendEmailJob();
		sendEmailJob.run();
	}

}
