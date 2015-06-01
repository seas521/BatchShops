package com.if2c.harald.tools;

import java.security.MessageDigest;
import java.util.Random;

public class Security {
	private static final String charset = "0123456789"
			+ "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static long before;
	public static String md5(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String s = "f2cf2c" + input; // 将要加密的字符串
		byte[] bs = md.digest(s.getBytes()); // 进行加密运算并返回字符数组

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bs.length; i++) { // 字节数组转换成十六进制字符串，形成最终的密文
			int v = bs[i] & 0xff;
			if (v < 16) {
				sb.append(0);
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString();
	}
	
	public static String simpleMD5(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String s =   input; // 将要加密的字符串
		byte[] bs = md.digest(s.getBytes()); // 进行加密运算并返回字符数组

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bs.length; i++) { // 字节数组转换成十六进制字符串，形成最终的密文
			int v = bs[i] & 0xff;
			if (v < 16) {
				sb.append(0);
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString();
	}
	
	 /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    } 

	
	
	public static String getBASE64(String s) {
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	public static String getRandomPassword() {
		return getRandomString(8);
	}

	public static String getRandomString(int length) {
		Random rand = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}

	public static String getGiftcardPassword() {
		return getRandomString(4) + getRandomString(4) 
				+ getRandomString(4) + getRandomString(4);
	}

	public static String getRandomPictureName() {
		long now = System.currentTimeMillis();
		if(now==before){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			now = System.currentTimeMillis();
		}
		before=now;
		int length = 8;
		Random rand = new Random(now);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		sb.append(now);
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		String s = Security.md5("f2c");
		System.out.println(s);
	
		s=getBASE64("瞿兵2");
		
		System.out.println(s);
	}
}
