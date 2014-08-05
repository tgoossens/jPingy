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
import com.googlecode.jpingy.exceptions.GeneralFailureException;
import com.googlecode.jpingy.exceptions.HostUnreachableException;
import com.googlecode.jpingy.exceptions.InvalidHostException;
import com.googlecode.jpingy.exceptions.TimeToLiveExpiredException;

/**
 * 
 * @author Thomas Goossens
 * @version 0.1a
 * 
 */
public class UnixPingResult extends PingResult {

	private String[] pack;
	private String[] rtt;

	public UnixPingResult(List<String> pingOutput)
			throws HostUnreachableException, GeneralFailureException, InvalidHostException, TimeToLiveExpiredException {
		super(pingOutput);
	}

	@Override
	protected void parsePingOutput(List<String> pingOutput)
			throws HostUnreachableException, InvalidHostException, TimeToLiveExpiredException {
		this.validatePingOutput(pingOutput);
		this.generatePackageArray(pingOutput);
		this.generateRttArray(pingOutput);
	}

	/**
	 * This identifies errors in the output and throws a specific error to notify the user
	 * @param pingOutput
	 * @throws HostUnreachableException
	 * @throws InvalidHostException
	 * @throws TimeToLiveExpiredException 
	 */
	private void validatePingOutput(List<String> pingOutput)
			throws HostUnreachableException, InvalidHostException, TimeToLiveExpiredException {
		int i = 0;
		while (i < pingOutput.size()) {
			if (pingOutput.get(i).contains("Destination Host Prohibited")) {
				throw new HostUnreachableException("Destination Host Prohibited");
			}
			else if (pingOutput.get(i).contains("unknown host")) {
				throw new InvalidHostException();
			}
			else if (pingOutput.get(i).contains("Time to live exceeded")) {
				throw new TimeToLiveExpiredException();
			}
			i++;
		}
	}

	private void generatePackageArray(List<String> pingOutput) {
		if (pack == null) {
			String packages = pingOutput.get(pingOutput.size() - 2);
			pack = packages.split(",");
		}
	}

	private void generateRttArray(List<String> pingOutput) {
		if (rtt == null) {
			String rtts = pingOutput.get(pingOutput.size() - 1);
			String[] rtt_equals = rtts.split("=");
			rtt = rtt_equals[1].split("/");
		}
	}

	@Override
	public int matchTransmitted() {
		return Integer.parseInt(pack[0].replaceAll("\\D+", ""));

	}

	@Override
	public int matchReceived() {
		return Integer.parseInt(pack[1].replaceAll("\\D+", ""));
	}

	@Override
	protected int matchLost() {
		String p = pack[2].replaceAll("\\D+", "");
		if (!p.isEmpty()) {
			float lost = Float.parseFloat(p);
			return (int) (lost * matchTransmitted());
		} else {
			return 0;
		}
	}

	@Override
	public int matchTime() {
		return Integer.parseInt(pack[3].replaceAll("\\D+", ""));

	}

	@Override
	public float matchRttMin() {
		return Float.parseFloat(rtt[0]);

	}

	@Override
	public float matchRttAvg() {
		return Float.parseFloat(rtt[1]);
	}

	@Override
	public float matchRttMax() {
		return Float.parseFloat(rtt[2]);
	}

	@Override
	public float matchRttMdev() {
		return Float.parseFloat(rtt[3].replaceAll("\\D+", ""));
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
		Pattern pattern = Pattern.compile("ttl=([0-9\\.]+)"); // match
		// ttl=decimal

		Matcher matcher = pattern.matcher(str.toString());

		matcher.find();
		MatchResult result = matcher.toMatchResult();

		return Integer.parseInt(result.group(1).replaceAll("ttl=", ""));
	}

	@Override
	protected int parsePayload() {
		// TODO Auto-generated method stub

		return Integer.parseInt(getLines().get(1).split("bytes")[0].trim());
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