package com.fitweber.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonQueryResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3526633958770960700L;

	private int totalNum;
	private ArrayList<String> columns;
	private List<Map> resultList;
	
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public ArrayList<String> getColumns() {
		return columns;
	}
	public void setColumns(ArrayList<String> columns) {
		this.columns = columns;
	}
	public List<Map> getResultList() {
		return resultList;
	}
	public void setResultList(List<Map> resultList) {
		this.resultList = resultList;
	}

}
