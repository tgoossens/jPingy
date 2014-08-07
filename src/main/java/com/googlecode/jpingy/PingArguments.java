/*
 Copyright (c) 2012 Thomas Goossens

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.googlecode.jpingy;

import org.apache.commons.lang3.SystemUtils;


public class PingArguments {
	private String url;
	private Integer count;
	private Long timeout;
	private Integer payload_bytes;
	private Long interval;
	private Integer ttl;
	private Backend backend;
	private LogLevel loglevel = LogLevel.NORMAL;

	public PingArguments() {
		// Get the operating system
		if (SystemUtils.IS_OS_WINDOWS)
			backend = Backend.WINDOWS;
		else
			backend = Backend.UNIX;
	}

	/**
	 * @return the backend
	 */
	public Backend getBackend() {
		return backend;
	}

	/**
	 * @return the loglevel
	 */
	public LogLevel getLogLevel() {
		return loglevel;
	}

	/**
	 * @param loglevel the loglevel to set
	 */
	public void setLogLevel(LogLevel loglevel) {
		this.loglevel = loglevel;
	}

	public static class Builder {

		private PingArguments arguments;

		public Builder() {
			this.arguments = new PingArguments();
		}

		/**
		 * Destination host to ping
		 * @param url Host to ping
		 * @return com.googlecode.jpingy.PingArguments.Builder
		 */
		public Builder url(String url) {
			arguments.url = url;
			return this;
		}

		/**
		 * Number of ping requests to send
		 * @param count Number of ping requests
		 * @return com.googlecode.jpingy.PingArguments.Builder
		 */
		public Builder count(Integer count) {
			arguments.count = count;
			return this;
		}

		/**
		 * Time to wait for a response, in milliseconds. 
		 * Very important for UNIX users to recognize this value is milliseconds and not seconds.
		 * @param timeout Time in milliseconds
		 * @return com.googlecode.jpingy.PingArguments.Builder
		 */
		public Builder timeout(Long timeout) {
			arguments.timeout = timeout;
			return this;
		}

		/**
		 * Specifies the number of data bytes to be sent. Default is 32 bytes for WINDOWS and 56 bytes for UNIX.
		 * @param bytes Bytes value
		 * @return com.googlecode.jpingy.PingArguments.Builder
		 */
		public Builder bytes(Integer bytes) {
			arguments.payload_bytes = bytes;
			return this;
		}

		/**
		 * Set the IP Time-to-Live. 
		 * The TTL (Time-To-Live) value of an IP packet represents the maximum number of IP routers that the packet can go through before being thrown away.
		 * @param ttl Time-to-live value
		 * @return com.googlecode.jpingy.PingArguments.Builder
		 */
		public Builder ttl(Integer ttl) {
			arguments.ttl = ttl;
			return this;
		}
		
		/**
		 * Output log level
		 * @param loglevel Log level enum value
		 * @return com.googlecode.jpingy.PingArguments.Builder
		 */
		public Builder loglevel(LogLevel loglevel) {
			arguments.setLogLevel(loglevel);
			return this;
		}

		/**
		 * Wait interval between sending each packet in milliseconds.
		 * @param interval Time interval
		 * @return com.googlecode.jpingy.PingArguments.Builder
		 */
		public Builder interval(Long interval) {
			arguments.interval = interval;
			return this;
		}

		/**
		 * Build the ping command with the provided arguments
		 * @return com.googlecode.jpingy.PingArguments
		 */
		public PingArguments build() {
			return arguments;
		}
	}

	/**
	 * Gets the ping command
	 * @return java.lang.String
	 * @throws IllegalArgumentException
	 */
	public String getCommand() throws IllegalArgumentException {
		//Validate
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL cannot be null");
		
		//Build the command
		StringBuilder builder = new StringBuilder();
		switch (backend) {
		case UNIX:
			builder.append("ping");
			if (count != null)
				builder.append(" ").append("-c").append(" ").append(count);
			if (interval != null)
				builder.append(" ").append("-i").append("").append(interval);
			if (timeout != null)
				builder.append(" ").append("-W").append(" ").append(timeout / 1000d);
			if (ttl != null)
				builder.append(" ").append("-t").append(" ").append(ttl);
			if (payload_bytes != null)
				builder.append(" ").append("-s").append(" ").append(payload_bytes);
			builder.append(" ").append(url);
			break;
		case WINDOWS:
			builder.append("ping");
			if (count != null)
				builder.append(" ").append("-n").append(" ").append(count);
			if (timeout != null)
				builder.append(" ").append("-w").append(" ").append(timeout);
			if (ttl != null)
				builder.append(" ").append("-i").append(" ").append(ttl);
			if (payload_bytes != null)
				builder.append(" ").append("-l").append(" ").append(payload_bytes);
			builder.append(" ").append(url);		
			break;
		}
		
		//Output command
		if (loglevel.equals(LogLevel.VERBOSE))
			System.out.println(builder.toString());
			
		return builder.toString();
	}
}