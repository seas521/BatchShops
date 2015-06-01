package com.if2c.harald.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidPooledConnection;

public class MySQLDB {

	private String url;
	private String username;
	private String password;

	public MySQLDB(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				connection = DriverManager.getConnection(url, username,
						password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Couldn't find driver" + e);
		}
		return connection;
	}

	public static void main(String[] args) throws SQLException {
		com.alibaba.druid.pool.DruidDataSource datasource = new com.alibaba.druid.pool.DruidDataSource();
		datasource.setUrl("jdbc:mysql://10.0.16.115:3306/if2c");
		datasource.setUsername("root");
		datasource.setPassword("123456");
		datasource.init();
		// datasource.setFilters("stat");t
		DruidPooledConnection conn = datasource.getConnection();
		System.out.println(conn);
		conn.close();
		datasource.close();
	}
}
