/*
 Copyright (c) 2012 Thomas Goossens

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.googlecode.jpingy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.jpingy.PingRequest.PingRequestBuilder;
import com.googlecode.jpingy.exceptions.BadLingerTimeException;
import com.googlecode.jpingy.exceptions.GeneralFailureException;
import com.googlecode.jpingy.exceptions.HostUnreachableException;
import com.googlecode.jpingy.exceptions.InvalidHostException;
import com.googlecode.jpingy.exceptions.TimeToLiveExpiredException;

/**
 * 
 * @author Austin Rappa
 * @version 0.2
 * 
 */
public class WindowsPingResult extends PingResult {

	private String[] pack;
	private String[] rtt;

	public WindowsPingResult(List<String> pingOutput)
			throws HostUnreachableException, GeneralFailureException,
			InvalidHostException, TimeToLiveExpiredException, BadLingerTimeException {
		super(pingOutput);
	}

	@Override
	protected void parsePingOutput(List<String> pingOutput)
			throws HostUnreachableException, GeneralFailureException,
			InvalidHostException, TimeToLiveExpiredException {
		this.validatePingOutput(pingOutput);
		this.generatePackageArray(pingOutput);
		this.generateRttArray(pingOutput);
	}

	/**
	 * This identifies errors in the output and throws a specific error to notify the user
	 * @param pingOutput
	 * @throws HostUnreachableException
	 * @throws GeneralFailureException
	 * @throws InvalidHostException
	 * @throws TimeToLiveExpiredException 
	 */
	private void validatePingOutput(List<String> pingOutput)
			throws HostUnreachableException, GeneralFailureException,
			InvalidHostException, TimeToLiveExpiredException {
		int i = 0;
		while (i < pingOutput.size()) {
			if (pingOutput.get(i).contains("Destination host unreachable.")) {
				throw new HostUnreachableException("Destination host unreachable.");
			}
			else if (pingOutput.get(i).contains("General failure.")) {
				throw new GeneralFailureException("General failure.");
			}
			else if (pingOutput.get(i).contains("Ping request could not find host")) {
				throw new InvalidHostException(pingOutput.get(0));
			}
			else if (pingOutput.get(i).contains("TTL expired in transit.")) {
				throw new TimeToLiveExpiredException(pingOutput.get(0));
			}
			i++;
		}
	}

	private void generatePackageArray(List<String> pingOutput) {
		if (pack == null) {
			String packages = pingOutput.get(pingOutput.size() - 3);
			pack = packages.split(",");
		}
	}

	private void generateRttArray(List<String> pingOutput) {
		if (rtt == null) {
			String rtt_line = pingOutput.get(pingOutput.size() - 1).trim();
			rtt = rtt_line.split(",");
		}
	}

	@Override
	public int matchTransmitted() {
		String p = pack[0].replaceAll("\\D+", "");
		if (!p.isEmpty())
			return Integer.parseInt(p);
		else
			return 0;

	}

	@Override
	public int matchReceived() {
		String p = pack[1].replaceAll("\\D+", "");
		if (!p.isEmpty())
			return Integer.parseInt(p);
		else
			return 0;
	}

	@Override
	protected int matchLost() {
		String p = pack[2].split(" ")[3];
		if (!p.isEmpty())
			return Integer.parseInt(p);
		else
			return 0;
	}

	@Override
	public int matchTime() {
		return 0;
	}

	@Override
	public float matchRttMin() {
		String p = rtt[0].replaceAll("\\D+", "");
		if (!p.isEmpty())
			return Float.parseFloat(p);
		else
			return 0f;
	}

	@Override
	public float matchRttAvg() {
		String p = rtt[2].replaceAll("\\D+", "");
		if (!p.isEmpty())
			return Float.parseFloat(p);
		else
			return 0f;
	}

	@Override
	public float matchRttMax() {
		String p = rtt[1].replaceAll("\\D+", "");
		if (!p.isEmpty())
			return Float.parseFloat(p);
		else
			return 0f;
	}

	@Override
	public float matchRttMdev() {
		return 0f;
	}

	@Override
	public String matchIP() {
		String str = getLines().toString();
		String pattern = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		m.find();

		return m.toMatchResult().group(0);
	}

	@Override
	public int matchTTL() {
		String str = getLines().toString();

		Pattern pattern = Pattern.compile("TTL=([0-9\\.]+)"); // match
																// TTL=decimal
		Matcher matcher = pattern.matcher(str.toString());
		matcher.find();
		MatchResult result = matcher.toMatchResult();

		if (result.groupCount() > 0)
			return Integer.parseInt(result.group(1).replaceAll("TTL=", ""));
		else
			return 0;
	}

	@Override
	protected int parsePayload() {
		if (getLines().size() > 3) {
			int i = 2;
			while (i < getLines().size() - 5) {
				if (getLines().get(i).startsWith("Reply from")) {
					String bytes = getLines().get(i).split("bytes=")[1].trim();
					String payload = bytes.split(" ")[0].trim();
					return Integer.parseInt(payload);
				}
				i++;			
			}
		}
		return 0;
	}

	@Override
	public List<PingRequest> getRequests() {

		List<PingRequest> requests = new ArrayList<PingRequest>();
		for (String line : getLines()) {
			if (isPingRequest(line)) {
				PingRequest request = createPingRequest(line);
				requests.add(request);
			}
		}
		return requests;

	}

	private PingRequest createPingRequest(String line) {
		String[] split = line.split(" ");
		PingRequestBuilder builder = PingRequest.builder();

		int bytes = Integer.parseInt(split[0]);
		String from = split[3];
		String fromIP = split[4].replace("(", "").replace(")", "")
				.replace(":", "");
		int reqnr = Integer.parseInt(split[5].split("=")[1]);
		int ttl = Integer.parseInt(split[6].split("=")[1]);
		float time = Float.parseFloat(split[7].split("=")[1]);

		builder = builder.bytes(bytes).from(from).fromIP(fromIP).reqNr(reqnr)
				.ttl(ttl).time(time);

		return builder.build();

	}

	private boolean isPingRequest(String line) {
		return line.contains("bytes from");
	}
}