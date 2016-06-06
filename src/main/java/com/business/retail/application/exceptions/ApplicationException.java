package com.business.retail.application.exceptions;

public class ApplicationException extends RuntimeException {
	String message;
	
	public ApplicationException(String message) {
		this.message = message;
	}
	
	public String toString() {
		return message;
	}

}
