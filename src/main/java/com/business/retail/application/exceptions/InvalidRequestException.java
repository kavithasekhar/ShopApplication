package com.business.retail.application.exceptions;

/**
 * Exception thrown for any bad input requests 
 * 
 * @author Kavitha 
 *
 */
public class InvalidRequestException extends ApplicationException {
	public InvalidRequestException(String message) {
		super(message);
	}
}
