package com.fitweber.pojo;

import java.io.Serializable;

public class QuerySqlModel implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8587917033218838150L;
	
	private String scriptFileName;
	private String scriptContent;
	private String scriptKeyword;
	
	public QuerySqlModel(){
		
	}
	
	public QuerySqlModel(String scriptFileName,String scriptContent,String scriptKeyword){
		this.scriptFileName =scriptFileName;
		this.scriptContent = scriptContent;
		this.scriptKeyword = scriptKeyword;
	}
	
	
	/**
	 * @return the scriptFileName
	 */
	public String getScriptFileName() {
		return scriptFileName;
	}
	/**
	 * @param scriptFileName the scriptFileName to set
	 */
	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}
	/**
	 * @return the scriptContent
	 */
	public String getScriptContent() {
		return scriptContent;
	}
	/**
	 * @param scriptContent the scriptContent to set
	 */
	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
	}
	/**
	 * @return the scriptKeyword
	 */
	public String getScriptKeyword() {
		return scriptKeyword;
	}
	/**
	 * @param scriptKeyword the scriptKeyword to set
	 */
	public void setScriptKeyword(String scriptKeyword) {
		this.scriptKeyword = scriptKeyword;
	}
	
}
