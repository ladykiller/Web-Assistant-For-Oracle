package com.fitweber.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class CommonQueryReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -736026317521072800L;

	private String tableName;
	private int pageNum;
	private int pageSize;
	private ArrayList<CommonParam> paramArray;
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
	public ArrayList<CommonParam> getParamArray() {
		return paramArray;
	}
	public void setParamArray(ArrayList<CommonParam> paramArray) {
		this.paramArray = paramArray;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
