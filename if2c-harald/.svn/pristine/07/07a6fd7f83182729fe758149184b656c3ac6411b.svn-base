package com.if2c.harald.job;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.if2c.harald.db.Config;

public class SendSmsTestJob extends JobBase{
	public void run(){
		HttpClient httpClient= new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://221.179.180.158:9007/QxtSms/QxtFirewall?OperID=haixuan&OperPass=haixuan123&SendTime=&ValidTime=&AppendID=&DesMobile=13241338086&Content=%A1%BE%BA%A3%D1%A1%CD%F8%A1%BF%D7%F0%BE%B4%B5%C4%BB%E1%D4%B1%C4%FA%BA%C3%A3%AC%BA%A3%D1%A1%CD%F8%BD%AB%D3%DA2014-9-9%BD%F8%D0%D0%B4%F3%B4%D9%A3%AC%BD%EC%CA%B1%C8%AB%B3%A13%D5%DB%C6%F0%A3%AC%BE%B4%C7%EB%B9%D8%D7%A2%A1%A3&ContentType=8");  
		//System.out.println("executing request " + httpget.getURI());  
		try {
			httpClient.execute(httpget);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  	
	}
	public static void main(String[] args){
		SendSmsTestJob sendSmsTestJob=new SendSmsTestJob();
		sendSmsTestJob.run();
		System.exit(0);
		
	}
	
	
}
