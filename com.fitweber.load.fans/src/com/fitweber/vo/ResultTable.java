package com.fitweber.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4854094592719720205L;
	ArrayList<String> coulumnArray;
	
	public ArrayList<String> getCoulumnArray() {
		return coulumnArray;
	}
	public void setCoulumnArray(ArrayList<String> coulumnArray) {
		this.coulumnArray = coulumnArray;
	}

	
}
