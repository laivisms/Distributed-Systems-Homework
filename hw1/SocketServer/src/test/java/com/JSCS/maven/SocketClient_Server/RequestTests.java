package com.JSCS.maven.SocketClient_Server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RequestTests {
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(35);
	private static Client client;
	private static Thread server;
	private static String filePath;
	
	
	@BeforeClass
    public static void setFile(){
		//System.setProperty("filePath", "backups/classes.txt");
        filePath = System.getProperty("filePath");
    }
	
	@BeforeClass
	public static void instantiate(){
		
		client = new Client();
		if(filePath == null){
			filePath = "backups/classes.txt";
		}
		server = new Thread(runnableCreator(filePath));
		server.start();
	}
	
	
	public static Runnable runnableCreator(final String path){
		return new Runnable() {
			public void run(){
				Server server = new Server(path);
			    
				server.start();
			}
		};
	}
	
	
	@Test
	public void getTest(){
		/**scheduler = Executors.newScheduledThreadPool(35);
		Client client = new Client();
		scheduler.execute(runnableCreator("backups/classes.txt"));
		**/
		System.out.println("Now testing a GET request on the server...\n");
		
		String result = client.accessServer("GET COM");
		Database db = new Database(filePath);
		String dbResult = db.getCode("COM");
		Assert.assertTrue(dbResult.trim().equals(result.trim()));
		
		System.out.println("Server successfully returned all classes in the \"COM\" major, as specified in the file.\n");
	}
	
	@Test
	public void addTest(){
		System.out.println("Now testing an ADD request on the server...\n");
		String addedClass = "ADD COM 12345 testClass";
		client.accessServer(addedClass);
		String result= client.accessServer("GET COM");
		Assert.assertTrue(client.accessServer("GET COM").contains("12345 testClass"));
		
		System.out.println("Server successfully ADDed a new course.\n");
	}
	@Test
	public void deleteTest(){
		
		System.out.println("Now testing a DELETE request on the server...\n");
		
		String addedClass = "ADD COM 12345 testClass";
		
		client.accessServer(addedClass);
		Assert.assertTrue(client.accessServer("GET COM").contains("12345 testClass"));
		client.accessServer("DELETE 12345");
		Assert.assertFalse(client.accessServer("GET COM").contains("12345 testClass"));
		
		System.out.println("Server successfully ADDed a new course \"COM 12345 testClass\", then DELETEd the course.");
	}
	
	
	@Test
	public void badGetTest(){
		
		System.out.println("Now testing bad GET requests...\n");
		System.out.println(client.accessServer("GET POM"));
		Assert.assertTrue(client.accessServer("GET POM").getClass() == String.class);
		System.out.println(client.accessServer("GET"));
		Assert.assertTrue(client.accessServer("GET").getClass() == String.class);
		System.out.println(client.accessServer("GET aegergrsdfdg"));
		Assert.assertTrue(client.accessServer("GET aegergrsdfdg").getClass() == String.class);
		System.out.println(client.accessServer("gEt 123"));
		Assert.assertTrue(client.accessServer("gEt 123").getClass() == String.class);
		
		
		System.out.println("Server successfully handled bad GET requests and responded accordingly.\n");
	}
	
	@Test
	public void badAddTest(){
		
		System.out.println("Now testing bad ADD requests...\n");
		
		Assert.assertTrue(client.accessServer("ADD").getClass() == String.class);
		System.out.println(client.accessServer("ADD"));
		Assert.assertTrue(client.accessServer("ADD COM").getClass() == String.class);
		System.out.println(client.accessServer("ADD COM"));
		Assert.assertTrue(client.accessServer("ADD COM 12345").getClass() == String.class);
		System.out.println(client.accessServer("ADD COM 12345"));
		Assert.assertTrue(client.accessServer("aDd commmmm").getClass() == String.class);
		System.out.println(client.accessServer("aDd commmmm"));
		Assert.assertTrue(client.accessServer("add com sdukfhskf").getClass() == String.class);
		System.out.println(client.accessServer("add com sdukfhskf"));
		
		System.out.println("Server successfully handled bad ADD requests and responded accordingly.\n");
	}
	@Test
	public void badDeleteTest(){
		System.out.println("Now testing bad DELETE requests...\n");
		
		Assert.assertTrue(client.accessServer("delete").getClass() == String.class);
		System.out.println(client.accessServer("delete"));
		Assert.assertTrue(client.accessServer("dElEte ").getClass() == String.class);
		System.out.println(client.accessServer("dElEte "));
		Assert.assertTrue(client.accessServer("delete dkfhbasdk").getClass() == String.class);
		System.out.println(client.accessServer("delete dkfhbasdk"));
		Assert.assertTrue(client.accessServer("delete skfbs dkfbs sdm").getClass() == String.class);
		System.out.println(client.accessServer("delete skfbs dkfbs sdm"));
		Assert.assertTrue(client.accessServer("delete 1234567788888888888888888").getClass() == String.class);
		System.out.println(client.accessServer("delete 1234567788888888888888888"));
		
		System.out.println("Server successfully handled bad DELETE requests and responded accordingly.\n");
	}

}
