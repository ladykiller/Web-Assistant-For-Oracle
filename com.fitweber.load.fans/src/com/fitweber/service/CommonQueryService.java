package com.fitweber.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
import net.sf.json.JSONObject;

import com.fitweber.dao.CommonQueryDao;
import com.fitweber.util.CommonUtils;
import com.fitweber.vo.CommonParam;
import com.fitweber.vo.CommonQueryReq;
import com.fitweber.vo.CommonQueryResp;
import com.fitweber.vo.CommonSQL;

public class CommonQueryService {
	
	private CommonQueryDao commonQueryDao;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String commonQueryByParam(String requestData) throws IOException{
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
		requestMap.put("sql", sql.toString());
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
						backupSql.append("'"+(String) map.get(columns.get(j))+"',");
					}else if("class java.sql.Timestamp".equals(columnType)){
						backupSql.append("'"+CommonUtils.formatDate((java.sql.Timestamp) map.get(columns.get(j)))+"',");
					}
				}
			}
			backupSql.append(");\n");
		}
		String backupContent = backupSql.toString().replace(",)", ")");
		CommonUtils.saveFile(null, "backup.sql", backupContent);
		
		CommonQueryResp resp = new CommonQueryResp();
		resp.setTotalNum(resultList.size());
		resp.setResultList(resultList);
		resp.setColumns(columns);
		String resultMessage = JSONObject.fromObject(resp).toString();
		CommonUtils.saveFile(null, "query.log", resultMessage);
		return resultMessage;
	}

	public String commonQueryBySQL(String requestData) throws IOException {
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
						backupSql.append("'"+(String) map.get(columns.get(j))+"',");
					}else if("class java.sql.Timestamp".equals(columnType)){
						backupSql.append("'"+CommonUtils.formatDate((java.sql.Timestamp) map.get(columns.get(j)))+"',");
					}
				}
			}
			backupSql.append(");\n");
		}
		String backupContent = backupSql.toString().replace(",)", ")");
		CommonUtils.saveFile(null, "backup.sql", backupContent);
		CommonQueryResp resp = new CommonQueryResp();
		resp.setTotalNum(resultSize);
		resp.setResultList(resultList);
		resp.setColumns(columns);
		String resultMessage = JSONObject.fromObject(resp).toString();
		CommonUtils.saveFile(null, "query.log", resultMessage);
		return resultMessage;
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