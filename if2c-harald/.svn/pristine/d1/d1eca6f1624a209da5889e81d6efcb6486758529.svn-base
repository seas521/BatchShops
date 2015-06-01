package com.if2c.harald.migration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;

import com.if2c.harald.beans.Seller;

public class ReadWriteTextFile {
	  static public String getContents(File aFile) {
	    StringBuilder contents = new StringBuilder();
	    
	    try {
	      BufferedReader input =  new BufferedReader(new FileReader(aFile));
	      try {
	        String line = null; //not declared within while loop
	        while (( line = input.readLine()) != null){
	          contents.append(new String(line.getBytes("utf-8")));
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    
	    return contents.toString();
	  }

	  static public void setContents(File aFile, String aContents)
	                                 throws FileNotFoundException, IOException {
	    if (aFile == null) {
	      throw new IllegalArgumentException("File should not be null.");
	    }
	    if (!aFile.exists()) {
	      throw new FileNotFoundException ("File does not exist: " + aFile);
	    }
	    if (!aFile.isFile()) {
	      throw new IllegalArgumentException("Should not be a directory: " + aFile);
	    }
	    if (!aFile.canWrite()) {
	      throw new IllegalArgumentException("File cannot be written: " + aFile);
	    }

	    Writer output = new BufferedWriter(new FileWriter(aFile,true));
	    try {
	      output.write( new String(aContents.getBytes("utf-8")) );
	      ((BufferedWriter) output).newLine(); 
	    }
	    finally {
	      output.close();
	    }
	  }

	  public static void main (String... aArguments) throws IOException {
		File dirFile=new File("E:/账号密码");
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
	    File testFile = new File(dirFile.getAbsolutePath()+"/账号密码.txt");
	    if(testFile.createNewFile()){
	    	System.out.println("文件不存在，重新创建"+testFile.getName());
	    }else{
	    	System.out.println("文件已存在!");
	    }
	  }
	  
	  public static void writeFile (Seller seller,String dir,String fileName, String pbAccount, String password1, String fyAccount, String password2) throws IOException {
			File dirFile=new File(dir);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
		    File testFile = new File(dirFile.getAbsolutePath()+"/"+fileName+".csv");
		    if(testFile.createNewFile()){
		    	/*System.out.println("文件不存在，重新创建"+testFile.getName());*/
		    }else{
		    	/*System.out.println("文件已存在!");*/
		    }
		    /*System.out.println("源文件内容: " + getContents(testFile));*/
		    setContents(testFile, seller.getAccountName()+","+seller.getPassword());
		    if(StringUtils.isNotBlank(pbAccount)){
		    	setContents(testFile, pbAccount+","+password1);
		    }
		    if(StringUtils.isNotBlank(fyAccount)){
		    	setContents(testFile, fyAccount+","+password2);
		    }
		    /*System.out.println("新文件内容: " + getContents(testFile));*/
		  }
	  
	  public static void writeErrorFile (String errorMessage,String dir,String fileName) throws IOException {
			File dirFile=new File(dir);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
		    File testFile = new File(dirFile.getAbsolutePath()+"/"+fileName+".txt");
		    if(testFile.createNewFile()){
		    	/*System.out.println("文件不存在，重新创建"+testFile.getName());*/
		    }else{
		    	/*System.out.println("文件已存在!");*/
		    }
		    /*System.out.println("源文件内容: " + getContents(testFile));*/
		    setContents(testFile, errorMessage);
		    
		  }

	 public static void writeSendMessageFile (String dir,String fileName, Long id,String mobil,String source ) throws IOException {
			File dirFile=new File(dir);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
		    File testFile = new File(dirFile.getAbsolutePath()+"/"+fileName+".csv");
		    if(testFile.createNewFile()){
		    	/*System.out.println("文件不存在，重新创建"+testFile.getName());*/
		    }else{
		    	/*System.out.println("文件已存在!");*/
		    }
		    /*System.out.println("源文件内容: " + getContents(testFile));*/
		    setContents(testFile, id+","+mobil+","+"1,"+source);
		  }

}
