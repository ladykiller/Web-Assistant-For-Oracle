package com.fitweber.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fitweber.pojo.QuerySqlModel;
import com.fitweber.service.CommonQueryService;
import com.fitweber.util.CommonUtils;
import com.fitweber.util.FileOperateUtil;

/**
 * 
 * @author 海杰
 * 
 */
@Controller
@RequestMapping("/commonQuery")
public class CommonQueryController implements ServletConfigAware {
	@Resource(name = "commonQueryService")
	private CommonQueryService commonQueryService;
	private ServletConfig  servletConfig;
	
	/**
	 * logger
	 */
	private static Logger logger = Logger
			.getLogger(CommonQueryController.class);

	@RequestMapping("/getTableNames.do")
	public void getTableNames(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String resultMessage = commonQueryService.getAllTableName();
		if (!logger.isDebugEnabled()) {
			logger.debug(resultMessage);
		}
		PrintWriter out = response.getWriter();
		out.write(resultMessage);
		out.close();
	}
	
	@RequestMapping("/getColumns.do")
	public void getColumns(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		String tableName = request.getParameter("tableName").toString();
		String resultMessage = commonQueryService.getColumns(tableName);
		if (!logger.isDebugEnabled()) {
			logger.debug(resultMessage);
		}
		PrintWriter out = response.getWriter();
		out.write(resultMessage);
		out.close();
	}
	
	@RequestMapping("/commonQueryByParam.do")
	public void commonQueryByParam(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SQLException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		String requestData = request.getParameter("json").toString();
		String resultMessage = commonQueryService.commonQueryByParam(requestData);
		if (!logger.isDebugEnabled()) {
			logger.debug(resultMessage);
		}
		PrintWriter out = response.getWriter();
		out.write(resultMessage);
		out.close();
	}
	
	@RequestMapping("/commonQueryBySQL.do")
	public void commonQueryBySQL(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SQLException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		String requestData = request.getParameter("json").toString();
		String resultMessage = commonQueryService.commonQueryBySQL(requestData);
		if (!logger.isDebugEnabled()) {
			logger.debug(resultMessage);
		}
		PrintWriter out = response.getWriter();
		out.write(resultMessage);
		out.close();
	}
	
	@RequestMapping("/commonQueryFLZL.do")
	public void commonQueryFLZL(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		String queryexecelPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/queryexecel/test.xls").replace("\\", "/");
		String sqldownloadPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/sqldownload/").replace("\\", "/");
		ArrayList<String> elementList = CommonUtils.readExecel(queryexecelPath);
		ArrayList<QuerySqlModel> querySqlList = new ArrayList<QuerySqlModel>();
		int sqlSize = elementList.size(),i;
		for(i=1;i<sqlSize;i++){//屏蔽表头
			String[] params = elementList.get(i).split("\t");
			querySqlList.add(new QuerySqlModel(params[0], params[1].replace(";", ""), params[2]));
		}
		String resultMessage = commonQueryService.commonQueryByExcel(querySqlList,sqldownloadPath,"附列资料");
		PrintWriter out = response.getWriter();
		out.write(resultMessage);
		out.close();
	}
	
	@RequestMapping("/createFLZL.do")
	public void createFLZL(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, RowsExceededException, WriteException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		//String queryexecelPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/queryexecel/test.xls").replace("\\", "/");
		String[] elementList = CommonUtils.createFlzl("flzl.xls");
		String resultMessage = commonQueryService.createFLZL(elementList);
		
		CommonUtils.writeExecel("flzl_1.xls",0,3,resultMessage.split("\n"));
		PrintWriter out = response.getWriter();
		out.write(resultMessage);
		out.close();
	}
	
	@RequestMapping("/commonQueryByExcel.do")
	public String commonQueryByExcel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		String queryexecelPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/queryexecel/").replace("\\", "/");
		String sqldownloadPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/sqldownload/").replace("\\", "/");
        MultipartHttpServletRequest multipartHttpservletRequest=(MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpservletRequest.getFile("execel_param");
        String originalFileName=multipartFile.getOriginalFilename();
        File file=new File(queryexecelPath);
        if(!file.exists()){
            file.mkdir();
        }
        try {
        	//String queryFilePath  = file+"/queryexecel"+originalFileName.substring(originalFileName.lastIndexOf('.'),originalFileName.length());
        	String queryFilePath  = file+"/"+originalFileName;
            FileOutputStream fileOutputStream=new FileOutputStream(queryFilePath);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            
    		ArrayList<String> elementList = CommonUtils.readExecel(queryFilePath);
    		ArrayList<QuerySqlModel> querySqlList = new ArrayList<QuerySqlModel>();
    		int sqlSize = elementList.size(),i;
    		for(i=1;i<sqlSize;i++){//屏蔽表头
    			String[] params = elementList.get(i).split("\t");
    			querySqlList.add(new QuerySqlModel(params[0], params[1].replace(";", ""), params[2]));
    		}
    		String timeStamp = CommonUtils.formatTime(new Date()).replace(":", "").replace("-", "").replace(" ", "");
    		String message = commonQueryService.commonQueryByExcel(querySqlList,sqldownloadPath,originalFileName.substring(0,originalFileName.lastIndexOf('.'))+"_"+timeStamp);
    		if(!"执行成功".equals(message)){
    			PrintWriter out = response.getWriter();
    			out.write("<html>"+message+"</html>");
    			out.close();
    		}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "downloadList";
	}

	@RequestMapping("/createXMLByExecel.do")
	public void createXMLByExecel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		String queryexecelPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/queryexecel/").replace("\\", "/");
		String sqldownloadPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/sqldownload/").replace("\\", "/");
        MultipartHttpServletRequest multipartHttpservletRequest=(MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpservletRequest.getFile("execel_param");
        String originalFileName=multipartFile.getOriginalFilename();
        File file=new File(queryexecelPath);
        if(!file.exists()){
            file.mkdir();
        }
        try {
        	String queryFilePath  = file+"/queryexecel"+originalFileName.substring(originalFileName.lastIndexOf('.'),originalFileName.length());
            FileOutputStream fileOutputStream=new FileOutputStream(queryFilePath);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            
    		ArrayList<String> elementList = CommonUtils.readExecel(queryFilePath);
    		ArrayList<QuerySqlModel> querySqlList = new ArrayList<QuerySqlModel>();
    		int sqlSize = elementList.size(),i;
    		for(i=1;i<sqlSize;i++){//屏蔽表头
    			String[] params = elementList.get(i).split("\t");
    			querySqlList.add(new QuerySqlModel(params[0], params[1].replace(";", ""), params[2]));
    		}
    		PrintWriter out = response.getWriter();
    		out.write("<html>"+commonQueryService.commonQueryByExcel(querySqlList,sqldownloadPath,"325")+"</html>");
    		out.write("<html>"+commonQueryService.commonQueryByExcel(querySqlList,sqldownloadPath,"325")+"</html>");
    		out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return "success";
	}
	
	@RequestMapping("/createDownloadList.do")
	public void createDownloadList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ path + "/commonquery/sqldownload/";
		String sqldownloadPath = (request.getSession().getServletContext().getRealPath("")+"/commonquery/sqldownload/").replace("\\", "/");
		File file = new File(sqldownloadPath);
//		if (file.exists()&&file.isDirectory()) {
//		    String[] tempList = file.list();
//		    for(String f:tempList){
//		    	System.out.println(basePath+f);
//			}
//		}
		if (file.exists()&&file.isDirectory()) {
		    String[] tempList = file.list();
		    StringBuffer buf = new StringBuffer();
		    int i,listSize = tempList.length;
		    buf.append("[");
		    for(i=1;i<listSize-1;i++){
		    	buf.append("{\"filename\":\""+tempList[i]+"\"},");
			}
		    buf.append("{\"filename\":\""+tempList[listSize-1]+"\"}]");
		    PrintWriter out = response.getWriter();
		    out.write(buf.toString());
		    out.close();
		}
	}
	
	@RequestMapping("/createDownloadProccess.do")
	public String createDownloadProccess(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
        String filename = request.getParameter("downloadfilename").toString();
        String contentType = "application/x-msdownload;";  
  
        FileOperateUtil.download(request, response, filename, contentType,  
        		filename,"commonquery\\sqldownload\\");  
        return null;
	}
	
	
	@Override
	public void setServletConfig(ServletConfig sc) {
		this.servletConfig = sc;
	}
}
