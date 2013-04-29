package com.fitweber.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import com.fitweber.dao.CommonQueryDao;
import com.fitweber.pojo.QuerySqlModel;
import com.fitweber.util.CommonUtils;
import com.fitweber.util.ZipUtils;
import com.fitweber.vo.CommonParam;
import com.fitweber.vo.CommonQueryReq;
import com.fitweber.vo.CommonQueryResp;
import com.fitweber.vo.CommonSQL;

public class CommonQueryService {
	
	private CommonQueryDao commonQueryDao;
	
	/**
	 * 参数查询 支持分页 返回所有字段
	 * @param requestData
	 * @return
	 * @throws IOException
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String commonQueryByParam(String requestData) throws IOException, SQLException{
		HashMap<String,String> requestMap = new HashMap<String, String>();
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("paramArray", CommonParam.class);
		JSONObject jsonObject =JSONObject.fromObject(requestData);
		CommonQueryReq commonQueryReq = (CommonQueryReq) JSONObject.toBean(jsonObject,CommonQueryReq.class,classMap);
		
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(commonQueryReq.getTableName());
		sql.append(" WHERE 1=1 ");
		ArrayList<CommonParam> paramArray = commonQueryReq.getParamArray();
		for(CommonParam cp:paramArray){
			sql.append(cp.getParamLogic()+" ");
			sql.append(cp.getParamName()+" = ");
			sql.append("'"+cp.getParamValue()+"' ");
		}
		requestMap.put("BEIGNROW",String.valueOf((commonQueryReq.getPageNum()-1)*commonQueryReq.getPageSize()));
		requestMap.put("ENDROW",String.valueOf(commonQueryReq.getPageNum()*commonQueryReq.getPageSize()));
		requestMap.put("sql", sql.toString());
		ArrayList<String> columns = new ArrayList<String>();
		List<Map> resultList = commonQueryDao.commonQueryByPage(requestMap);
		if(resultList!=null&&resultList.size()>0){
			Map map = resultList.get(0);
			Iterator it = map.keySet().iterator();
			columns.add("RN");
			while(it.hasNext()){
				String str = (String) it.next();
				if(!"RN".equals(str)){
					columns.add(str);
				}
			}
		}
		
		StringBuffer backupSql = new StringBuffer();
		Map map;
		int i,j,columnSize=columns.size(),resultSize=resultList.size();
		backupSql.append("INSERT INTO "+commonQueryReq.getTableName()+" (");
		for(i=0;i<columnSize;i++){
			backupSql.append(columns.get(i)+",");
		}
		backupSql.append(") VALUES (");
		String columnsSQL = backupSql.toString().replace(",)", ")");
		backupSql.setLength(0);
		String columnType = "";
		for(i=0;i<resultSize;i++){
			backupSql.append(columnsSQL);
			map = (Map)resultList.get(i);
			for(j=0;j<columnSize;j++){
				Object o = map.get(columns.get(j));
				if(o!=null){
					columnType = o.getClass().toString();
					if("class java.lang.String".equals(columnType)){
						backupSql.append("'"+(String) o+"',");
					}else if("class java.sql.Timestamp".equals(columnType)){
						backupSql.append("'"+CommonUtils.formatDate((java.sql.Timestamp) o)+"',");
					}else if("class oracle.sql.CLOB".equals(columnType)){
						backupSql.append("'"+((oracle.sql.CLOB)o).getSubString(1, (int)((oracle.sql.CLOB)o).length())+"',");
					}
				}
			}
			backupSql.append(");\n");
		}
		String backupContent = backupSql.toString().replace(",)", ")");
		CommonUtils.saveFile(null, "backup.sql", backupContent,false);
		
		CommonQueryResp resp = new CommonQueryResp();
		resp.setTotalNum(commonQueryDao.commonQueryCount(requestMap));
		resp.setResultList(resultList);
		resp.setColumns(columns);
		String resultMessage = JSONObject.fromObject(resp).toString();
		CommonUtils.saveFile(null, "query.log", resultMessage,false);
		return resultMessage;
	}

	/**
	 * 自定义SQL语句查询 返回所有结果
	 * @param requestData
	 * @return
	 * @throws IOException
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String commonQueryBySQL(String requestData) throws IOException, SQLException {
		HashMap<String,String> requestMap = new HashMap<String, String>();
		JSONObject jsonObject =JSONObject.fromObject(requestData);
		CommonSQL commonSQL = (CommonSQL) JSONObject.toBean(jsonObject,CommonSQL.class);
		requestMap.put("sql", commonSQL.getSql());
		ArrayList<String> columns = new ArrayList<String>();
		List<Map> resultList = commonQueryDao.commonQuery(requestMap);
		if(resultList!=null&&resultList.size()>0){
			Map map = resultList.get(0);
			Iterator it = map.keySet().iterator();
			while(it.hasNext()){
				String str = (String) it.next();
				columns.add(str);
			}
		}
		
		StringBuffer backupSql = new StringBuffer();
		Map map;
		int i,j,columnSize=columns.size(),resultSize=resultList.size();
		String sql = commonSQL.getSql().toUpperCase();
		Pattern patternTableName1 = Pattern.compile("FROM (.*?)WHERE");
		Pattern patternTableName2 = Pattern.compile("FROM (.*?)$");
		Matcher matcher =patternTableName1.matcher(sql);
		if(matcher.find()){
			backupSql.append("INSERT INTO "+matcher.group(1)+" (");
		}else{
			matcher =patternTableName2.matcher(sql);
			if(matcher.find()){
				backupSql.append("INSERT INTO "+matcher.group(1)+" (");
			}else{
				backupSql.append("INSERT INTO  NoTable (");
			}
		}
		for(i=0;i<columnSize;i++){
			backupSql.append(columns.get(i)+",");
		}
		backupSql.append(") VALUES (");
		String columnsSQL = backupSql.toString().replace(",)", ")");
		backupSql.setLength(0);
		String columnType = "";
		for(i=0;i<resultSize;i++){
			backupSql.append(columnsSQL);
			map = (Map)resultList.get(i);
			for(j=0;j<columnSize;j++){
				Object o = map.get(columns.get(j));
				if(o!=null){
					columnType = o.getClass().toString();
					if("class java.lang.String".equals(columnType)){
						backupSql.append("'"+(String) o+"',");
					}else if("class java.sql.Timestamp".equals(columnType)){
						backupSql.append("'"+CommonUtils.formatDate((java.sql.Timestamp) o)+"',");
					}else if("class oracle.sql.CLOB".equals(columnType)){
						backupSql.append("'"+((oracle.sql.CLOB)o).getSubString(1, (int)((oracle.sql.CLOB)o).length())+"',");
					}
				}
			}
			backupSql.append(");\n");
		}
		String backupContent = backupSql.toString().replace(",)", ")");
		CommonUtils.saveFile(null, "backup.sql", backupContent,false);
		CommonQueryResp resp = new CommonQueryResp();
		resp.setTotalNum(resultSize);
		resp.setResultList(resultList);
		resp.setColumns(columns);
		String resultMessage = JSONObject.fromObject(resp).toString();
		CommonUtils.saveFile(null, "query.log", resultMessage,false);
		return resultMessage;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String commonQueryByExcel(ArrayList<QuerySqlModel> querySqlList,String downloadPath){
		
		HashMap<String,String> requestMap = new HashMap<String, String>();
		ArrayList<String> columns = new ArrayList<String>();
		StringBuffer backupSql = new StringBuffer();
		try {
			int totalNum,pagecount,rest,i;
			List<Map> resultList;
			CommonUtils.delFolder(downloadPath+"sources/");
			for(QuerySqlModel q :querySqlList){
				requestMap.put("sql", q.getScriptContent());
				totalNum = commonQueryDao.commonQueryCount(requestMap);
				if(totalNum>2000){
					pagecount=totalNum/2000;
					rest=totalNum%2000;
					for(i=0;i<pagecount;i++){
						requestMap.put("BEIGNROW",String.valueOf(i*2000));
						requestMap.put("ENDROW",String.valueOf((i+1)*2000-1));
						resultList = commonQueryDao.commonQueryByPage(requestMap);
						
						if(columns.size()==0){
							if(resultList!=null&&resultList.size()>0){
								Map newmap = resultList.get(0);
								Iterator it = newmap.keySet().iterator();
								while(it.hasNext()){
									String str = (String) it.next();
									columns.add(str);
								}
							}
						}
						saveData(columns, resultList, backupSql, q, downloadPath);
						backupSql.setLength(0);
					}
					requestMap.put("BEIGNROW",String.valueOf(pagecount*2000));
					requestMap.put("ENDROW",String.valueOf(pagecount*2000+rest));
					resultList = commonQueryDao.commonQueryByPage(requestMap);
					saveData(columns, resultList, backupSql, q, downloadPath);
					backupSql.setLength(0);
				}else{
					resultList = commonQueryDao.commonQuery(requestMap);
					if(resultList!=null&&resultList.size()>0){
						Map newmap = resultList.get(0);
						Iterator it = newmap.keySet().iterator();
						while(it.hasNext()){
							String str = (String) it.next();
							columns.add(str);
						}
					}
					saveData(columns, resultList, backupSql, q, downloadPath);
					backupSql.setLength(0);
				}
				columns.clear();
			}
			ZipUtils zipUtils = new ZipUtils(downloadPath+"/sql.zip");
			zipUtils.compress(downloadPath+"sources/");
		} catch (Exception e) {
			e.printStackTrace();
			return "文件异常，请检查文件格式和内容";
		}
		return "执行成功";
	}
	
	@SuppressWarnings("rawtypes")
	public void saveData(ArrayList<String> columns,List<Map> resultList,StringBuffer backupSql,QuerySqlModel q,String downloadPath) throws IOException, SQLException{
		int columnSize=columns.size(),resultSize=resultList.size();
		String sql = q.getScriptContent().toUpperCase();
		Map map;
		Object o;
		int i,j;
		Pattern patternTableName1 = Pattern.compile("FROM (.*?)WHERE");
		Pattern patternTableName2 = Pattern.compile("FROM (.*?)$");
		Matcher matcher =patternTableName1.matcher(sql);
		if(matcher.find()){
			backupSql.append("INSERT INTO "+matcher.group(1)+" (");
		}else{
			matcher =patternTableName2.matcher(sql);
			if(matcher.find()){
				backupSql.append("INSERT INTO "+matcher.group(1)+" (");
			}else{
				backupSql.append("INSERT INTO  NoTable (");
			}
		}
		for(i=0;i<columnSize;i++){
			backupSql.append(columns.get(i)+",");
		}
		backupSql.append(") VALUES (");
		String columnsSQL = backupSql.toString().replace(",)", ")");
		backupSql.setLength(0);
		for(i=0;i<resultSize;i++){
			backupSql.append(columnsSQL);
			map = (Map)resultList.get(i);
			o = map.get(columns.get(0));
			if(o!=null){
				backupSql.append(getValueString(o));
			}else{
				backupSql.append("''");
			}
			for(j=1;j<columnSize;j++){
				o = map.get(columns.get(j));
				if(o!=null){
					backupSql.append(","+getValueString(o));
				}else{
					backupSql.append(",''");
				}
			}
			backupSql.append(");\n");
		}
		CommonUtils.saveFile(null, downloadPath+"sources/"+q.getScriptFileName()+".sql", backupSql.toString(),true);
/*						CommonQueryResp resp = new CommonQueryResp();
		resp.setTotalNum(resultSize);
		resp.setResultList(resultList);
		resp.setColumns(columns);
		String resultMessage = JSONObject.fromObject(resp).toString();
		CommonUtils.saveFile(null, downloadPath+"query.log", resultMessage,false);*/
	}
	
	public String getValueString(Object o) throws SQLException{
		String columnType = o.getClass().toString();
		if("class java.lang.String".equals(columnType)){
			return "'"+(String) o+"'";
		}else if("class java.math.BigDecimal".equals(columnType)){
			return "'"+((java.math.BigDecimal) o).toString()+"'";
		}
		else if("class java.sql.Timestamp".equals(columnType)){
			return "to_date('"+ CommonUtils.formatDate((java.sql.Timestamp) o)+"', 'yyyy-mm-dd')";
		}else if("class oracle.sql.CLOB".equals(columnType)){
			return "'"+((oracle.sql.CLOB)o).getSubString(1, (int)((oracle.sql.CLOB)o).length())+"'";
		}
		return "''";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String createFLZL(String[] sqls){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		StringBuffer bf = new StringBuffer();
		int listLen;
		for(String s:sqls){
			requestMap.put("sql", s);
			List<Map> resultList = commonQueryDao.commonQuery(requestMap);
			listLen=resultList.size();
			if(listLen>0){
				bf.append((String)resultList.get(0).get("FBZL_DM"));
/*				for(Map m:resultList){
				}*/
			}
			bf.append("    \n");
		}
		System.out.println(bf.toString());
		return bf.toString();
	}
	
	public String uploadFile(){
		return "succeese";
	}
	
	
	@SuppressWarnings("rawtypes")
	public String getAllTableName(){
		List resultList = commonQueryDao.getAllTableName();
		Object[] resultArray = resultList.toArray();
		StringBuffer resultMessage=new StringBuffer();
		resultMessage.append("[");
		for(Object str:resultArray){
			resultMessage.append("\"" +str.toString()+ "\",");
		}
		resultMessage.append("]");
		return resultMessage.toString().replace(",]", "]");
	}
	
	@SuppressWarnings("rawtypes")
	public String getColumns(String tableName){
		List resultList = commonQueryDao.getColumns(tableName);
		Object[] resultArray = resultList.toArray();
		StringBuffer resultMessage=new StringBuffer();
		resultMessage.append("[");
		for(Object str:resultArray){
			resultMessage.append("\"" +str.toString()+ "\",");
		}
		resultMessage.append("]");
		return resultMessage.toString().replace(",]", "]");
	}

	public CommonQueryDao getCommonQueryDao() {
		return commonQueryDao;
	}

	public void setCommonQueryDao(CommonQueryDao commonQueryDao) {
		this.commonQueryDao = commonQueryDao;
	}
		
}
