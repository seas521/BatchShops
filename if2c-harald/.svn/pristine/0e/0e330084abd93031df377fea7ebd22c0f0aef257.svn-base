package com.if2c.harald.mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.if2c.harald.db.Config;
import com.if2c.harald.job.JobBase;

/**
 * create by zhw 2014-07-04
 * 创建待发送邮件到表email_queue
 * 
 */
public class MailCreator extends JobBase {
	Connection conn = null;

	public MailCreator(Connection conn) {
		this.conn = conn;
	}

	/**
	 * by zhw
	 * 创建待发送邮件到表email_queue
	 * @param subject 主题
	 * @param to 收件人
	 * @param from  发件人
	 * @param body   内容
	 */
	public void createEmail(String subject, String to, String from,String body)
			throws SQLException {
		String insertsql = "INSERT INTO `email_queue` (`subject`, `to`, `from`, `body`) VALUES (?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(insertsql);
		try{
			conn = this.getConnection();
			if (conn != null) {
				ps.setString(1, subject);
				ps.setString(2, to);
				ps.setString(3, from);
				ps.setString(4, body);
				ps.execute();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ps = null;
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
	}
	
	/**
	 * by zhw
	 * 通过 创建待发送邮件到表email_queue
	 * @param subject 主题
	 * @param to 收件人
	 * @param from  发件人
	 * @param templateFile  模板文件
	 * @param dataMap  模板参数
	 */
	public void createEmailWithTemplate(String subject, String to,String cc, String from,String templateFile, Map<String, String> dataMap)
			throws SQLException {
		String insertsql = "INSERT INTO `email_queue` (`subject`, `to`,`cc`, `from`, `body`) VALUES (?,?, ?, ?, '"
				+ Config.getTemplate(templateFile, dataMap) + "')";
		PreparedStatement ps = conn.prepareStatement(insertsql);
		try{
			conn = this.getConnection();
			if (conn != null) {
				ps.setString(1, subject);
				ps.setString(2, to);
				ps.setString(3, cc);
				ps.setString(4, from);
				ps.execute();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ps = null;
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
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
