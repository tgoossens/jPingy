package com.googlecode.jpingy.exceptions;


public class GeneralFailureException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6325685283180402248L;


	/**
	 * General failure.
	 */
	public GeneralFailureException() {
		this("General failure.");
	}

	
	/**
	 * General failure.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public GeneralFailureException(String message) {
		super(message);
	}

	
	/**
	 * General failure.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public GeneralFailureException(Throwable cause) {
		super("General failure.", cause);
	}
	
	
	/**
	 * General failure.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public GeneralFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
