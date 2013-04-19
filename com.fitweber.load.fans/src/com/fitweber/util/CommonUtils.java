package com.fitweber.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.fitweber.pojo.ExecelElement;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
	
	public static boolean createExecel(String execelName,String localtion,ArrayList<ExecelElement> content){
        try   {
            //  打开文件 
            WritableWorkbook book  =  Workbook.createWorkbook( new  File( " test.xls " ));
            //  生成名为“第一页”的工作表，参数0表示这是第一页 
            WritableSheet sheet  =  book.createSheet( " 第一页 " ,  0 );
            //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
            //  以及单元格内容为test 
            Label label  =   new  Label( 0 ,  0 ,  " test " );

            //  将定义好的单元格添加到工作表中 
            sheet.addCell(label);

            /* 
            * 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
             */ 
           jxl.write.Number number  =   new  jxl.write.Number( 1 ,  0 ,  555.12541 );
           sheet.addCell(number);

            //  写入数据并关闭文件 
            book.write();
           book.close();

       }   catch  (Exception e)  {
           e.printStackTrace();
       } 
		return true;
	}
}
