package com.googlecode.jpingy.exceptions;


public class InvalidHostException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6325685283180402248L;


	/**
	 * Ping request could not find host. Please check the name and try again.
	 */
	public InvalidHostException() {
		this("Ping request could not find host. Please check the name and try again.");
	}

	
	/**
	 * Ping request could not find host. Please check the name and try again.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public InvalidHostException(String message) {
		super(message);
	}

	
	/**
	 * Ping request could not find host. Please check the name and try again.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public InvalidHostException(Throwable cause) {
		super("Ping request could not find host. Please check the name and try again.", cause);
	}
	
	
	/**
	 * Ping request could not find host. Please check the name and try again.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public InvalidHostException(String message, Throwable cause) {
		super(message, cause);
	}
}
