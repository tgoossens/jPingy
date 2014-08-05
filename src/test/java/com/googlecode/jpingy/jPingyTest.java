/**
 * 
 */
package com.googlecode.jpingy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Austin_Rappa
 *
 */
public class jPingyTest {

	@Test
	public void testPing() throws Exception {
		try {
			PingArguments arguments = new PingArguments.Builder()
				.url("www.google.com")
				.count(3)	
				.loglevel(LogLevel.VERBOSE)
				.timeout(1l)
				.build();
			PingResult results = Ping.ping(arguments);
			
			assertNotNull("Ping results cannot be null", results);
			
			double rate = 1.0d - ((double)results.getPacketsLost() / results.getPacketsTransmitted());
			
			//Debug
			System.out.println();
			System.out.println(" -sent: " + results.getPacketsTransmitted());
			System.out.println(" -lost: " + results.getPacketsLost());
			System.out.println(" -rate: " + (rate * 100) + "%");
			
			assertEquals("Ping should be 100%", 1.0d, rate, 0.1);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}
