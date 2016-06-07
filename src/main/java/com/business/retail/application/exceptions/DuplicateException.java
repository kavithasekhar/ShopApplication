package com.business.retail.application.exceptions;

/**
 * This exception is especially thrown when duplicate shops are added due to
 * consequence of POST non-idempotent nature
 * 
 * @author Kavitha
 *
 */
public class DuplicateException extends ApplicationException {
	public DuplicateException(String message) {
		super(message);
	}
}
