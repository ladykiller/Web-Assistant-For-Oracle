package com.fitweber.pojo;

import java.io.Serializable;

public class ExecelElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6622828923796971600L;
	
	private Localtion localtion;
	private String content;
	
	public ExecelElement(){
		
	}
	
	public ExecelElement(Localtion l,String c){
		this.localtion=l;
		this.content=c;
	}
	
	public Localtion getLocaltion() {
		return localtion;
	}
	public void setLocaltion(Localtion localtion) {
		this.localtion = localtion;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
