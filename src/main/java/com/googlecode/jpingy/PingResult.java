/*
 Copyright (c) 2012 Thomas Goossens

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.googlecode.jpingy;

import java.util.List;

import com.googlecode.jpingy.exceptions.BadLingerTimeException;
import com.googlecode.jpingy.exceptions.GeneralFailureException;
import com.googlecode.jpingy.exceptions.HostUnreachableException;
import com.googlecode.jpingy.exceptions.InvalidHostException;
import com.googlecode.jpingy.exceptions.TimeToLiveExpiredException;

/**
 * 
 * @author Thomas Goossens
 * @version 0.2
 * 
 */
public abstract class PingResult {

	protected abstract void parsePingOutput(List<String> pingOutput)
			throws HostUnreachableException, GeneralFailureException, InvalidHostException, TimeToLiveExpiredException, BadLingerTimeException;

	protected abstract int parsePayload();

	protected abstract int matchTransmitted();

	protected abstract int matchReceived();

	protected abstract int matchLost();

	protected abstract int matchTime();

	protected abstract float matchRttMin();

	protected abstract float matchRttAvg();

	protected abstract float matchRttMax();

	protected abstract float matchRttMdev();

	protected abstract String matchIP();

	protected abstract int matchTTL();

	public abstract List<PingRequest> getRequests();

	private final String address;
	private final int transmitted;
	private final int ttl;
	private final long time;
	private final int received;
	private final int lost;
	private final int payload;
	private final float rtt_min;
	private final float rtt_avg;
	private final float rtt_max;
	private final float rtt_mdev;

	private List<String> lines;

	/**
	 * Ping result
	 * 
	 * @param pingOutput
	 *            output from ping process
	 * @throws HostUnreachableException
	 * @throws GeneralFailureException 
	 * @throws InvalidHostException 
	 * @throws BadLingerTimeException 
	 */
	protected PingResult(List<String> pingOutput)
			throws HostUnreachableException, GeneralFailureException, InvalidHostException, TimeToLiveExpiredException, BadLingerTimeException {
		this.lines = pingOutput;
		parsePingOutput(pingOutput);

		transmitted = matchTransmitted();
		received = matchReceived();
		time = matchTime();
		lost = matchLost();

		rtt_min = matchRttMin();
		rtt_avg = matchRttAvg();
		rtt_max = matchRttMax();
		rtt_mdev = matchRttMdev();

		ttl = matchTTL();

		address = matchIP();

		payload = parsePayload();
	}

	/**
	 * Gets the IP sddress
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Gets the time to live for a packet
	 * 
	 * @return the ttl
	 */
	public int getTimeToLive() {
		return ttl;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Gets the number of packets sent
	 * 
	 * @return the transmitted
	 */
	public int getPacketsTransmitted() {
		return transmitted;
	}

	/**
	 * Gets the number of received packets
	 * 
	 * @return the received
	 */
	public int getPacketsReceived() {
		return received;
	}

	/**
	 * Gets the number of lost packets
	 * 
	 * @return the lost
	 */
	public int getPacketsLost() {
		return lost;
	}

	/**
	 * Gets the number of bytes sent in a packet
	 * 
	 * @return the payload
	 */
	public int getPayload() {
		return payload;
	}

	/**
	 * Gets the minimum (min) round trip time in milliseconds
	 * 
	 * @return the rtt_min
	 */
	public float getRoundTripTimeMinimum() {
		return rtt_min;
	}

	/**
	 * Gets the average (avg) round trip time in milliseconds
	 * 
	 * @return the rtt_avg
	 */
	public float getRoundTripTimeAverage() {
		return rtt_avg;
	}

	/**
	 * Gets the maximum (max) round trip time in milliseconds
	 * 
	 * @return the rtt_max
	 */
	public float getRoundTripTimeMaximum() {
		return rtt_max;
	}

	/**
	 * Gets the standard deviation (stddev) round trip time in milliseconds
	 * 
	 * @return the standard deviation
	 */
	public float getRoundTripTimeStandardDeviation() {
		return rtt_mdev;
	}

	/**
	 * Gets the ping output
	 * 
	 * @return ping output
	 */
	public List<String> getLines() {
		return lines;
	}

	@Override
	public String toString() {
		return "PingResult [address=" + address + ", transmitted="
				+ transmitted + ", ttl=" + ttl + ", time=" + time
				+ ", received=" + received + ", lost=" + lost + ", payload="
				+ payload + ", rtt_min=" + rtt_min + ", rtt_avg=" + rtt_avg
				+ ", rtt_max=" + rtt_max + ", rtt_mdev=" + rtt_mdev + "]";
	}
}