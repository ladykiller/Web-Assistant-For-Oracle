package com.fitweber.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.fitweber.pojo.ExecelElement;
import com.fitweber.pojo.Localtion;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
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
            WritableWorkbook book  =  Workbook.createWorkbook( new  File( localtion+execelName+" .xls " ));
            //  生成名为“第一页”的工作表，参数0表示这是第一页 
            WritableSheet sheet  =  book.createSheet( " 第一页 " ,  0 );
            //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
            //  以及单元格内容为test 
            Localtion l;
            for(ExecelElement e:content){
            	l =e.getLocaltion();
            	Label label  =   new  Label( l.getX() ,  l.getY() ,  e.getContent() );
            	sheet.addCell(label);
            }
            //  写入数据并关闭文件 
            book.write();
           book.close();

       }   catch  (Exception e)  {
           e.printStackTrace();
       } 
		return true;
	}
	
	public static ArrayList<ExecelElement> readExecel(String execelName,ArrayList<Localtion> localtionList){
		ArrayList<ExecelElement> result = new ArrayList<ExecelElement>();
		String content = null;
        try   {
            Workbook book  =  Workbook.getWorkbook( new  File(execelName));
             //  获得第一个工作表对象
            Sheet sheet  =  book.getSheet(0);
            Range [] ranges = sheet.getMergedCells();
            HashMap<String,ArrayList<Range>> rangeList = new HashMap<String,ArrayList<Range>>();
            int i,j;

            HashSet<Integer> rowNums = new HashSet<Integer>();
            
            for(Range r:ranges){
            	rowNums.add((Integer)r.getTopLeft().getRow());
            }
            int rowsLen = rowNums.size();
            Object[] rowObjs =rowNums.toArray();
            Integer[] rows = new Integer[rowsLen];
            for(i=0;i<rowsLen;i++){
            	rows[i]=(Integer)rowObjs[i];
            }
            
            int[] rowArr =new int[rowsLen];
            for(i=0;i<rowsLen;i++){
            	rowArr[i]=(int)rows[i];
            	rangeList.put(String.valueOf(rowArr[i]), new ArrayList<Range>());
            }
            rowArr = quickSort(rowArr, 0, rowsLen-1);
            
            for(Range r:ranges){
            	rangeList.get(String.valueOf(r.getTopLeft().getRow())).add(r);
            }
            
            int k,colsLen;
            StringBuffer bf = new StringBuffer();
            
            for(i=0;i<rowsLen;i++){
                ArrayList<Range> colunms = rangeList.put(String.valueOf(rowArr[i]),new ArrayList<Range>());
                colsLen =colunms.size();
                Object[] colsObj = colunms.toArray();
                Range[] cols = new Range[colsLen];
                for(j=0;j<colsLen;j++){
                	cols[j]=(Range)colsObj[j];
                }
                
                int[] colArr = new int[colsLen]; 
                for(k=0;k<colsLen;k++){
                	colArr[k]=cols[k].getBottomRight().getColumn();
                }
                colArr = quickSort(colArr,0,colsLen-1);
                HashMap<String,Range> colunmMap = new HashMap<String, Range>();
                for(k=0;k<colsLen;k++){
                	colunmMap.put(String.valueOf(cols[k].getBottomRight().getColumn()), cols[k]);
                }
                
                for(k=0;k<colsLen;k++){
                	bf.append(colunmMap.get(String.valueOf(colArr[k])).getTopLeft().getContents()+"\t");
                }
                
                System.out.println(bf.toString());
                
                bf.setLength(0);
            }
            book.close();
        }   catch  (Exception e)  {
            e.printStackTrace();
        } 
		
		return result;
	}
	
	public static int[] quickSort(int[] arr,int start,int end){
	     if(start<end){
	         int key=arr[start],i=start,j=end+1,temp;
	         while(i<j){
	             while(arr[++i]<key&&i<end);
	             while(arr[--j]>key);//--j，可以让arr[j]等于他自己或小于他的数时停下来。j--,停下来后还要减1。容易出现-1越界。 
	             if(i<j){
	                 temp=arr[i];
	                 arr[i]=arr[j];
	                 arr[j]=temp;
	             }
	         }
	         arr[start]=arr[j];
	         arr[j]=key;               
	         quickSort(arr,start,j-1);
	         quickSort(arr,j+1,end);
	     }
	     return arr;
	}
}
