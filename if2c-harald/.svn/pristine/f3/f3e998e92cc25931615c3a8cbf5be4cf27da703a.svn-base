package com.if2c.harald.tools;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class IDCardLinkCalculator {

	private String username;
	private int userID;

	public IDCardLinkCalculator(int userID, String username) {
		this.userID = userID;
		this.username = username;
	}

	public String getLink() {
		int path1 = userID % 100;
		int path2 = ((userID - userID % 100) / 100) % 100;

		StringBuilder sb = new StringBuilder();
		sb.append("http://img.a.if2c/pimgs/idcard/").append(path1)
				.append("/").append(path2).append("/");
		sb.append(userID).append("_")
				.append(getBASE64(username).replaceAll("/", ""));
		sb.append("_0.jpg");
		return sb.toString();
	}

	public static String getBASE64(String s) {
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String name="肖和";
		IDCardLinkCalculator cal = new IDCardLinkCalculator(2095, name);
		String link = cal.getLink();
		System.out.println(link);
	}

}
