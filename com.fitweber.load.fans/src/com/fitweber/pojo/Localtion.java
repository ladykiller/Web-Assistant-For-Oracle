package com.fitweber.pojo;

import java.io.Serializable;
/**
 * 
 * @author 
 *
 */
public class Localtion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9107048413580646307L;
	
	private int x;
	private int y;
	private int z;
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

}
