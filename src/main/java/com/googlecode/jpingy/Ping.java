/*
 Copyright (c) 2012 Thomas Goossens

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.googlecode.jpingy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jpingy.exceptions.BadLingerTimeException;
import com.googlecode.jpingy.exceptions.GeneralFailureException;
import com.googlecode.jpingy.exceptions.HostUnreachableException;
import com.googlecode.jpingy.exceptions.InvalidHostException;
import com.googlecode.jpingy.exceptions.TimeToLiveExpiredException;

/**
 * Sends ICMP ECHO_REQUEST packets to network hosts.
 * 
 * @author Thomas Goossens
 * @version 0.2
 * 
 */
public class Ping {

	/**
	 * Sends ICMP ECHO_REQUEST packets to network hosts.
	 * @param ping Ping arguments
	 * @return com.googlecode.jpingy.PingResult
	 * @throws HostUnreachableException
	 * @throws GeneralFailureException
	 * @throws InvalidHostException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws TimeToLiveExpiredException
	 * @throws BadLingerTimeException
	 */
	public static PingResult ping(PingArguments ping)
			throws HostUnreachableException, GeneralFailureException,
			InvalidHostException, IllegalArgumentException, IOException, TimeToLiveExpiredException, BadLingerTimeException {

		Process p = null;

		p = Runtime.getRuntime().exec(ping.getCommand());
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = stdInput.readLine()) != null) {
			lines.add(line);
			if (ping.getLogLevel().equals(LogLevel.VERBOSE))
				System.out.println(line);
		}
		while ((line = stdError.readLine()) != null) {
			lines.add(line);
			if (ping.getLogLevel().equals(LogLevel.VERBOSE))
				System.out.println(line);
		}
	
		p.destroy();

		return ping.getBackend().getResult(lines);

	}
}
