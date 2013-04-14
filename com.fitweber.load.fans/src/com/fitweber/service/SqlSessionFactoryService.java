/**
 * com.fitweber.fans SqlSessionFactoryService
 */
package com.fitweber.service;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 2012-9-6 ÉÏÎç12:43:42 SqlSessionFactoryService.java
 * 
 * @author wheatmark hajima11@163.com
 * 
 */

public class SqlSessionFactoryService {

	private static SqlSessionFactory sqlSessionFactory = null;
	private static SqlSessionFactoryService getSqlSessionFactory = null;

	public void SqlSessionFactoryService() {
		String rs = "META-INF/configration/mybatis-config.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(rs);

		} catch (IOException e) {
			e.printStackTrace();
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

	}

	public static SqlSessionFactoryService getInstance() {
		if (getSqlSessionFactory == null) {
			getSqlSessionFactory = new SqlSessionFactoryService();
		}
		return getSqlSessionFactory;
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		if(sqlSessionFactory==null){
			SqlSessionFactoryService.getInstance();
		}
		return sqlSessionFactory;
	}
}
