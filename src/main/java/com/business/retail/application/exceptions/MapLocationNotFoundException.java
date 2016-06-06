package com.business.retail.application.exceptions;

/**
 * Exception thrown when location not found on google maps
 * 
 * @author Kavitha
 *
 */
public class MapLocationNotFoundException extends ApplicationException {
	public MapLocationNotFoundException(String message) {
		super(message);
	}
}
