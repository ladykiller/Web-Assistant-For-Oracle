package com.fitweber.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author 海杰
 *
 */
public interface CommonQueryDao {
	@SuppressWarnings("rawtypes")
	public List commonQuery(HashMap<String,String> requestMap);
	public int commonQueryCount(HashMap<String,String> requestMap);
	@SuppressWarnings("rawtypes")
	public List getAllTableName();
	@SuppressWarnings("rawtypes")
	public List getColumns(String tableName);
}
