package com.if2c.harald.migration.i18n;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.if2c.harald.migration.ImageMigrationTask;
import com.if2c.translate.TranslateConnector;
import com.if2c.translate.Translator;
import com.taobao.common.tfs.DefaultTfsManager;

public class I18nIntoRedis extends ImageMigrationTask implements
		TranslateConnector {

	private JedisPool pool;

	public I18nIntoRedis() throws FileNotFoundException, IOException {
		super();

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(10);
		config.setMaxIdle(60);
		config.setMinIdle(20);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setNumTestsPerEvictionRun(10);
		String redisUrl=props.getProperty("redis.url");
		String redisPort=props.getProperty("redis.port");
		String url=props.getProperty("database.url");
		String username=props.getProperty("database.username");
		String password=props.getProperty("database.password");
		pool = new JedisPool(config, redisUrl, Integer.valueOf(redisPort));

		I18Handler tc;
		try {
			tc = new I18Handler(url,
					username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		List<RedisBean> list = tc.getDataList();
		System.out.println(list.size());
		for (RedisBean bean : list) {
			Translator trans = new Translator(this, StringUtils.substringAfterLast(bean.getTable(), "_"));
			Map<String, String> info = new HashMap<String, String>();
			//如果表名中包含value，说明这个表中只有value列，没有name列
			if (StringUtils.contains(bean.getTable(), "value")) {
				info.put("value", bean.getTranslation());
			} else {
				info.put("name", bean.getTranslation());
			}
			trans.save(StringUtils.substringBeforeLast(bean.getTable(), "_"), bean.getId(), info);
		}
	}

	@Override
	public Jedis getJedis() {
		return pool.getResource();
	}

	@Override
	public DefaultTfsManager getTfsManager() {
		return getTFSManager();
	}

	@Override
	public Connection getDBConnection() {
		return getConnection();
	}

	@Override
	public void closeJedis(Jedis jedis) {
		pool.returnResource(jedis);
	}

	@Override
	public void closeTfsManager(DefaultTfsManager tfsManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeConnection(Connection con) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		new I18nIntoRedis();
	}

}
