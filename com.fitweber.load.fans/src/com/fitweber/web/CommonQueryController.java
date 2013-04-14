package com.fitweber.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fitweber.service.CommonQueryService;

/**
 * 
 * @author 海杰
 * 
 */
@Controller
@RequestMapping("/commonQuery")
public class CommonQueryController {
	@Resource(name = "commonQueryService")
	private CommonQueryService commonQueryService;

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
			HttpServletResponse response) throws ServletException, IOException {
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
			HttpServletResponse response) throws ServletException, IOException {
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
}
