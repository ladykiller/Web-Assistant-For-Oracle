package com.fitweber.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.apache.catalina.tribes.util.UUIDGenerator;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.fitweber.pojo.ExecelElement;
import com.fitweber.pojo.Localtion;

public class CommonUtils {
	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static String generateUUID() {
		StringBuffer buf = new StringBuffer();
		for (String s : (java.util.UUID.randomUUID().toString().split("-"))) {
			buf.append(s);
		}
		return buf.toString();
	}

	public static String generateXLH() {
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * Java保存文件工具类
	 * 
	 * @param savePalce
	 *            文件夹路径
	 * @param fileName
	 *            文件路径
	 * @param context
	 *            保存内容
	 * @throws IOException
	 */
	public static void saveFile(String savePalce, String fileName,
			String context) throws IOException {
		if (savePalce != null) {
			File dir = new File(savePalce);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		if (fileName != null) {
			String[] parts = fileName.split("/");
			int partslen = parts.length, i;
			StringBuffer dirName = new StringBuffer();
			for (i = 0; i < partslen - 1; i++) {
				dirName.append(parts[i] + "/");
			}
			File dir = new File(dirName.toString());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file, false);
			if (context != null) {
				out.write(context.getBytes("utf-8"));
			}
			out.close();
		}
	}

	public static String formatDate(Timestamp date) {
		java.text.Format format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static void writeExecel(String execelName, int row, int column,
			String[] content) throws RowsExceededException, WriteException {
		try {
			OutputStream os = new FileOutputStream(execelName);// 输出的Excel文件URL
			WritableWorkbook book = Workbook.createWorkbook(os);
			WritableSheet sheet = book.createSheet("sheet1", 0);// 创建可写工作表
			int i, contentLen = content.length;
			for (i = 0; i < contentLen; i++) {
				Label label = new Label(column, i, content[i]);
				sheet.addCell(label);
			}
			book.write();
			book.close();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean createExecel(String execelName, String localtion,
			ArrayList<ExecelElement> content) {
		try {
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(new File(localtion
					+ execelName + " .xls "));
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet(" 第一页 ", 0);
			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			// 以及单元格内容为test
			Localtion l;
			for (ExecelElement e : content) {
				l = e.getLocaltion();
				Label label = new Label(l.getX(), l.getY(), e.getContent());
				sheet.addCell(label);
			}
			// 写入数据并关闭文件
			book.write();
			book.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static ArrayList<String> readExecel(String execelName) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Workbook book = Workbook.getWorkbook(new File(execelName));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			Range[] ranges = sheet.getMergedCells();
			HashMap<String, ArrayList<Range>> rangeList = new HashMap<String, ArrayList<Range>>();
			int i, j;

			HashSet<Integer> rowNums = new HashSet<Integer>();

			for (Range r : ranges) {
				rowNums.add((Integer) r.getTopLeft().getRow());
			}
			int rowsLen = rowNums.size();
			Object[] rowObjs = rowNums.toArray();
			Integer[] rows = new Integer[rowsLen];
			for (i = 0; i < rowsLen; i++) {
				rows[i] = (Integer) rowObjs[i];
			}

			int[] rowArr = new int[rowsLen];
			for (i = 0; i < rowsLen; i++) {
				rowArr[i] = (int) rows[i];
				rangeList
						.put(String.valueOf(rowArr[i]), new ArrayList<Range>());
			}
			rowArr = quickSort(rowArr, 0, rowsLen - 1);

			for (Range r : ranges) {
				rangeList.get(String.valueOf(r.getTopLeft().getRow())).add(r);
			}

			int k, colsLen;
			StringBuffer bf = new StringBuffer();

			for (i = 0; i < rowsLen; i++) {
				ArrayList<Range> colunms = rangeList.put(
						String.valueOf(rowArr[i]), new ArrayList<Range>());
				colsLen = colunms.size();
				Object[] colsObj = colunms.toArray();
				Range[] cols = new Range[colsLen];
				for (j = 0; j < colsLen; j++) {
					cols[j] = (Range) colsObj[j];
				}

				int[] colArr = new int[colsLen];
				for (k = 0; k < colsLen; k++) {
					colArr[k] = cols[k].getBottomRight().getColumn();
				}
				colArr = quickSort(colArr, 0, colsLen - 1);
				HashMap<String, Range> colunmMap = new HashMap<String, Range>();
				for (k = 0; k < colsLen; k++) {
					colunmMap.put(String.valueOf(cols[k].getBottomRight()
							.getColumn()), cols[k]);
				}

				for (k = 0; k < colsLen; k++) {
					bf.append(colunmMap.get(String.valueOf(colArr[k]))
							.getTopLeft().getContents()
							+ "\t");
				}

				System.out.println(bf.toString());
				result.add(bf.toString());
				bf.setLength(0);
			}
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<String> readExecelSimple(String execelName) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Workbook book = Workbook.getWorkbook(new File(execelName));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int rowNum = sheet.getRows(), i, j, rowLen;
			String temp, pre = "", sub = "";
			StringBuffer bf = new StringBuffer();
			Cell[] row;
			for (i = 1; i < rowNum; i++) {
				row = sheet.getRow(i);
				if (row[1].getContents() != null
						&& !"".equals(row[1].getContents())) {
					rowLen = row.length;
					for (j = 0; j < rowLen; j++) {
						temp = row[j].getContents();
						if (temp != null && !"".equals(temp)) {
							if (j == 6) {
								sub = "https://nfsvn.foresee.com.cn/svn/GT3-NF/trunk/engineering"
										+ row[j].getContents();
							}
							if (j == 9) {
								pre = "svn update -r" + row[j].getContents()
										+ " ";
							}
						}
					}
					bf.append(pre + sub + "\n");
				}
			}
			saveFile(null, "C:\\Users\\Administrator\\Desktop\\updateSvn.bat",
					bf.toString());
			System.out.println(bf.toString());
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void creatFlzlSql(String execelName) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Workbook book = Workbook.getWorkbook(new File(execelName));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int rowNum = sheet.getRows(), i, j, rowLen;
			String temp, pre = "", sub = "";
			StringBuffer bf = new StringBuffer();
			Cell[] row;
			for (i = 1; i < rowNum; i++) {
				row = sheet.getRow(i);
				if (row[1].getContents() != null
						&& !"".equals(row[1].getContents())) {
					rowLen = row.length;
					for (j = 0; j < rowLen; j++) {
						temp = row[j].getContents();
						if (temp != null && !"".equals(temp)) {
							if (j == 4) {
								pre = "INSERT INTO NF_SSSL.NF_XT_SSSQSX_FLZLDZB (NFSSSQFLZLUUID,SSSQSX_BM,DZBZDSZL_DM,FLZL_BM,YXBZ,TBRQ) VALUES ('"
										+ UUID.randomUUID().toString()
												.replace("-", "")
										+ "','"
										+ row[j].getContents()
										+ "','"+row[j].getContents()+"','";
							}
							if (j == 3) {
								sub = row[j].getContents()
										+ "','Y',sysdate);\n";
							}
						}
					}
					bf.append(pre + sub);
				}
			}
			saveFile(null, "C:\\Users\\Administrator\\Desktop\\insertsql.sql",
					bf.toString());
			System.out.println(bf.toString());
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[] createFlzl(String execelName) {
		ArrayList<String> result = new ArrayList<String>();
		StringBuffer bf = new StringBuffer();
		try {
			Workbook book = Workbook.getWorkbook(new File(execelName));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int rowNum = sheet.getRows(), i, j, rowLen;
			String temp, pre = "select fbzl_dm,fbzl_mc from NF_DM_GY_FBZL FSZL where fbzl_mc in('";
			// StringBuffer bf1 = new StringBuffer();
			Cell[] row;
			for (i = 0; i < rowNum; i++) {
				row = sheet.getRow(i);
				if (row[1].getContents() != null
						&& !"".equals(row[1].getContents())) {
					rowLen = row.length;
					for (j = 0; j < rowLen; j++) {
						temp = row[j].getContents();
						if (temp != null && !"".equals(temp)) {
							if (j == 2) {
								bf.append(pre + row[j].getContents());
							}
						}
					}
					bf.append("');");
					/*
					 * if(i==35||i==50||i==71||i==82||i==86||i==98||i==108||i==118
					 * ||i==121||i==124||i==139||i==140||i==150||i==151){
					 * bf.append(pre+bf1.toString()+
					 * ")\n select fbzl_dm,fbzl_mc from NF_DM_GY_FBZL FSZL where fbzl_mc in("
					 * ); bf1.setLength(0); }
					 */
				}
			}
			// saveFile(null,
			// "C:\\Users\\Administrator\\Desktop\\updateSvn.bat",
			// bf.toString());
			System.out.println(bf.toString());
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (bf.toString()).split(";");
	}

	public static int[] quickSort(int[] arr, int start, int end) {
		if (start < end) {
			int key = arr[start], i = start, j = end + 1, temp;
			while (i < j) {
				while (arr[++i] < key && i < end)
					;
				while (arr[--j] > key)
					;// --j，可以让arr[j]等于他自己或小于他的数时停下来。j--,停下来后还要减1。容易出现-1越界。
				if (i < j) {
					temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
			arr[start] = arr[j];
			arr[j] = key;
			quickSort(arr, start, j - 1);
			quickSort(arr, j + 1, end);
		}
		return arr;
	}
}
