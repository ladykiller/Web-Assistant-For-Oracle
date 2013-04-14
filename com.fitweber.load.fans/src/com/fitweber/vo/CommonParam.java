package com.fitweber.vo;

import java.io.Serializable;

public class CommonParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4677488857503706016L;
	private String paramName;
	private String paramValue;
	private String paramLogic;
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getParamLogic() {
		return paramLogic;
	}
	public void setParamLogic(String paramLogic) {
		this.paramLogic = paramLogic;
	}
}
