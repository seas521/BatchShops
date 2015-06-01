package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeletePreview extends JobBase {

	public DeletePreview() {
	}

	@Override
	public void run() {
		Connection con = null;
		Statement stat = null;
		try {
			con = this.getConnection();
			stat = con.createStatement();
			con.setAutoCommit(false);
			String sql1 = "delete from goods_inventory  where  goods_id in (select id from goods g where status=-1 )";
			String sql2 = "delete from goods_goods_attr_value_relation  where  goods_id in (select id from goods g where status=-1 )";
			String sql3 = "delete from goods  where status=-1";
			String sql4 = "delete from goods_series_image   where  series_id in (select id from goods_series  gs where  status=-1)";
			String sql5 = "delete from goods_extends_attr_value_relation   where  goods_series_id in (select id from goods_series  gs where  status=-1)";
			String sql6 = "delete from goods_series where status=-1";
			try {
				stat.execute(sql1);
				stat.execute(sql2);
				stat.execute(sql3);
				stat.execute(sql4);
				stat.execute(sql5);
				stat.execute(sql6);
				con.commit();
			} catch (Exception e) {
				error(e.getMessage(), e);
				con.rollback();
			}
		} catch (SQLException e) {
			error(e.getMessage(), e);
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
		}
	}

}
