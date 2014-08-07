package com.googlecode.jpingy.exceptions;


public class BadLingerTimeException extends Exception {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3990974007795995341L;


	/**
	 * Timeout value is too high.
	 */
	public BadLingerTimeException() {
		this("Timeout value is too high.");
	}

	
	/**
	 * Timeout value is too high.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public BadLingerTimeException(String message) {
		super(message);
	}

	
	/**
	 * Timeout value is too high.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public BadLingerTimeException(Throwable cause) {
		super("Timeout value is too high.", cause);
	}
	
	
	/**
	 * Timeout value is too high.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public BadLingerTimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
