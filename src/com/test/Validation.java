/**
 * 
 */
package com.test;

/**
 * @author jean-
 *
 */
public class Validation {
	
	// Constantes de classe
	public static int LETS_PLAY 			= 1;
	public static int TO_FEW_PLAYERS 		= -1;
	public static int TO_FEW_REAL_PLAYERS 	= -2;
	
	private int code;
	
	private String reason;
	
	public Validation(int code, String reason) {
		this.code = code;
		this.reason = reason;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public String toString() {
		return "[" + this.code + "] " + this.reason;
	}
}
