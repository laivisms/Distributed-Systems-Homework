package com.JSCS.maven.SocketClient_Server;

import org.junit.Assert;
import org.junit.Test;

public class BreakingTests {
	
	@Test
	public void noServerTest(){
		Client client = new Client();
		System.out.println("Now testing attempting to connect to the server when the server is down...\n");
		String result = client.accessServer("get com") + "\n";
		System.out.println(result);
		Assert.assertTrue(result != null);
		
		System.out.println("Client successfully reported to user that it could not connect to the server.\n");
	}
}
