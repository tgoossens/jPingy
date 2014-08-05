package com.googlecode.jpingy.exceptions;


public class HostUnreachableException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6325685283180402248L;


	/**
	 * Destination host unreachable.
	 */
	public HostUnreachableException() {
		this("Destination host unreachable.");
	}

	
	/**
	 * Destination host unreachable.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public HostUnreachableException(String message) {
		super(message);
	}

	
	/**
	 * Destination host unreachable.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public HostUnreachableException(Throwable cause) {
		super("Destination host unreachable.", cause);
	}
	
	
	/**
	 * Destination host unreachable.
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public HostUnreachableException(String message, Throwable cause) {
		super(message, cause);
	}
}
