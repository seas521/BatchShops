package com.if2c.harald.job;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.if2c.common.bean.SaleAfterModel;
import com.if2c.harald.Enums.CancelType;
import com.if2c.harald.db.Config;
import com.if2c.harald.db.DBConf;
import com.if2c.harald.db.DatasourceElement;
import com.if2c.harald.router.HaraldClient;
@DisallowConcurrentExecution
public abstract class JobBase implements Job {
	private static ConcurrentHashMap<String, DruidDataSource> conPoolMap = new ConcurrentHashMap<String, DruidDataSource>();
	private static DruidDataSource dataSource;
	private Logger logger = LoggerFactory.getLogger(JobBase.class);
	private Integer repeatCount;
	private String startDate;
	private Integer interval;
	private String cronExpression;

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public void info(String msg) {
		logger.info(msg);
	}

	public void error(String msg, Throwable exception) {
		logger.error(msg, exception);
	}

	private String getClassName() {
		return getClass().getName();
	}

	public String sendSMS(String phoneNum, String content)
			throws ClientProtocolException, IOException {
		String url = "http://sdkhttp.eucp.b2m.cn/sdkproxy/sendsms.action?cdkey=0SDK-EAA-0130-NDXQR&password=102579&phone="
				+ phoneNum + "&message=" + content;
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = HttpClientBuilder.create().build();
		String return_message = "";
		HttpResponse httpResponse = httpClient.execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			return_message = EntityUtils.toString(httpResponse.getEntity());
		}
		return return_message;
	}

	private DruidDataSource getConnectionPool() {
		if (dataSource == null) {
			dataSource = new DruidDataSource();
			Config conf = Config.getConf();
			if (conf == null) {
				AnnotatedElement element = this.getClass();
				try {
					if (element.isAnnotationPresent(DBConf.class)) {

						DBConf dbConf = element.getAnnotation(DBConf.class);
						BeanUtils.copyProperties(dataSource, dbConf);
						dataSource.setUrl(dbConf.url());
						dataSource.setUsername(dbConf.userName());
						dataSource.setPassword(dbConf.password());
					}
					conPoolMap.put(getClassName(), dataSource);
				} catch (Exception exception) {
					logger.error(exception.getMessage());
				}
			} else {
				DatasourceElement de = Config.getConf().getDatasourceElement()
						.get(0);
				dataSource.setUrl(de.getProMap().get("url"));
				dataSource.setUsername(de.getProMap().get("userName"));
				dataSource.setPassword(de.getProMap().get("password"));
			}

		}
		return dataSource;
	}

	public Connection getConnection() throws SQLException {
		return getConnectionPool().getConnection();
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		UseCustomAnnotation.readAnnotation(this.getClass());
		info(getClassName() + "--->上次执行时间: " + context.getPreviousFireTime());
		info(getClassName() + "--->当前打执行时间: " + context.getFireTime());
		info(getClassName() + "--->下次执行时间: " + context.getNextFireTime());
		info(getClassName() + "--->开始执行");
		run();
		info(getClassName() + "--->执行完毕");
	}

	public abstract void run();

	protected void opt(String sql, int status) throws SQLException {
		opt(sql, status, CancelType.CANCELGOODS.getSaleCode(), null);
	}

	protected void opt(String sql, int status, SaleAfterModel.SaleAfterOptEnum optEnum)
			throws SQLException {
		opt(sql, status, CancelType.CANCELGOODS.getSaleCode(), optEnum);
	}

	protected void opt(String sql, int status, int saleAfterType)
			throws SQLException {
		opt(sql, status, saleAfterType, null);
	}

	protected final void opt(String sql, int status, int saleAfterType,
	        SaleAfterModel.SaleAfterOptEnum optEnum) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = this.getConnection();
		if (con == null) {
			error("获得的Connection==null", new NullPointerException());
			return;
		}
		Statement stat = con.createStatement();
		con.setAutoCommit(false);
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			con.setAutoCommit(false);
			while (rs.next()) {
				int id = rs.getInt("id");
				Long orderId = rs.getLong("order_id");
				SaleAfterModel model=new SaleAfterModel();
				model.setSaleAfterInfoId(id);
				model.setOrderId(orderId);
				model.setSaleAfterOpt(optEnum);
				model.setAuditingStatus(status);
				model.setSaleAfterType(saleAfterType);
				model.setAction("saleAfterOpt");
				HaraldClient.getInstance().saleAfterOpt(model);
			}
		} catch (Exception e) {
			error(e.getMessage(), e);
			con.rollback();
		} finally {
			con.commit();
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					error(e.getMessage(), e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
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

	public void closeConnection(Connection conn) {
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

	public void closePs(PreparedStatement ps) {
		if (null != ps) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps = null;
		}
	}

	public void closeRs(ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
}
