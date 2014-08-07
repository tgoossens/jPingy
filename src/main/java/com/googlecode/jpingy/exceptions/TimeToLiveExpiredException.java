package com.googlecode.jpingy.exceptions;


public class TimeToLiveExpiredException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6325685283180402248L;


	/**
	 * The TTL value determines the maximum amount of time an IP packet may live in the network without reaching its destination. It is effectively a bound on the number of routers an IP packet may pass through before being discarded. This message indicates that the TTL expired in transit.
	 * 
	 * Increase the TTL value using the ttl parameter with the ping command. 
	 */
	public TimeToLiveExpiredException() {
		this("TTL expired in transit.");
	}

	
	/**
	 * The TTL value determines the maximum amount of time an IP packet may live in the network without reaching its destination. It is effectively a bound on the number of routers an IP packet may pass through before being discarded. This message indicates that the TTL expired in transit.
	 * 
	 * Increase the TTL value using the ttl parameter with the ping command. 
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public TimeToLiveExpiredException(String message) {
		super(message);
	}

	
	/**
	 * The TTL value determines the maximum amount of time an IP packet may live in the network without reaching its destination. It is effectively a bound on the number of routers an IP packet may pass through before being discarded. This message indicates that the TTL expired in transit.
	 * 
	 * Increase the TTL value using the ttl parameter with the ping command. 
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public TimeToLiveExpiredException(Throwable cause) {
		super("TTL expired in transit.", cause);
	}
	
	
	/**
	 * The TTL value determines the maximum amount of time an IP packet may live in the network without reaching its destination. It is effectively a bound on the number of routers an IP packet may pass through before being discarded. This message indicates that the TTL expired in transit.
	 * 
	 * Increase the TTL value using the ttl parameter with the ping command. 
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public TimeToLiveExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
}
