package com.fitweber.vo;

import java.io.Serializable;

public class CommonSQL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -479375619295998136L;
	
	private String sql;
	private int pageNum;
	private int pageSize;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
