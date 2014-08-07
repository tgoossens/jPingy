package com.googlecode.jpingy;

import java.util.List;

import com.googlecode.jpingy.exceptions.BadLingerTimeException;
import com.googlecode.jpingy.exceptions.GeneralFailureException;
import com.googlecode.jpingy.exceptions.HostUnreachableException;
import com.googlecode.jpingy.exceptions.InvalidHostException;
import com.googlecode.jpingy.exceptions.TimeToLiveExpiredException;

public enum Backend {
	UNIX {
		@Override
		public PingResult getResult(List<String> output)
				throws HostUnreachableException, GeneralFailureException, InvalidHostException, TimeToLiveExpiredException, BadLingerTimeException {
			return new UnixPingResult(output);
		}
	},
	WINDOWS {
		@Override
		public PingResult getResult(List<String> output)
				throws HostUnreachableException, GeneralFailureException, InvalidHostException, TimeToLiveExpiredException, BadLingerTimeException {
			return new WindowsPingResult(output);
		}
	};

	public abstract PingResult getResult(List<String> output)
			throws HostUnreachableException, GeneralFailureException, InvalidHostException, TimeToLiveExpiredException, BadLingerTimeException;
}
