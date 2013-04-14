package com.fitweber.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CommonUtils {
	/**
	 * 生成UUID
	 * @return
	 */
	public static String generateUUID(){
		StringBuffer buf = new StringBuffer();
		for(String s :(java.util.UUID.randomUUID().toString().split("-"))){
			buf.append(s);
		}
		return buf.toString();
	}
	
	public static String generateXLH(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	/**
	 * Java保存文件工具类
	 * @param savePalce 文件夹路径
	 * @param fileName 文件路径
	 * @param context 保存内容
	 * @throws IOException
	 */
	public static void saveFile(String savePalce,String fileName,String context) throws IOException{
		if(savePalce!=null){
			File dir = new File(savePalce);
			if(!dir.exists()){dir.mkdirs();}
		}
		if(fileName!=null){
			String[] parts = fileName.split("/");
			int partslen = parts.length,i;
			StringBuffer dirName = new StringBuffer();
			for(i=0;i<partslen-1;i++){
				dirName.append(parts[i]+"/");
			}
			File dir = new File(dirName.toString());
			if(!dir.exists()) {dir.mkdirs();}
			File file = new File(fileName);
			if(!file.exists()) {file.createNewFile();}
			FileOutputStream out=new FileOutputStream(file,false);
			if(context!=null){
				out.write(context.getBytes("utf-8"));
			}
			out.close();
		}
	}
	
	public static String formatDate(Timestamp date){
		java.text.Format format=new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date); 
	}
	
}
