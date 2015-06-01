package com.if2c.harald.job;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.if2c.harald.db.Config;
import com.if2c.harald.tools.BackupLog;
import com.if2c.harald.tools.HttpClients;

public class ClearCacheTest extends JobBase {

	@Override
	public void run() {
		/*String result=HttpClients.sendGet(Config.getConf().getHomePageHost().getHost()+"/clearcacheindex.html", "");
		System.out.println(result);*/
		System.out.print("before "+new Date());
		BackupLog.logInfo("before "+new Date());
		String result=HttpClients.sendGet(Config.getConf().getHomePageHost().getHost()+"/clearcacheindex.html", "");
		BackupLog.logInfo("after "+new Date());
		System.out.print("after "+new Date());
	}

	public static void main(String[] args) {
		ClearCacheTest cache = new ClearCacheTest();
		cache.run();
		System.exit(0);
	}

}
