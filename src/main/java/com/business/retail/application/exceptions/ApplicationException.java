package com.business.retail.application.exceptions;

/**
 * Generic Application Exception
 * 
 * @author Kavitha
 *
 */
public class ApplicationException extends RuntimeException {
	String message;
	
	public ApplicationException(String message) {
		this.message = message;
	}
	
	public String toString() {
		return message;
	}

}
