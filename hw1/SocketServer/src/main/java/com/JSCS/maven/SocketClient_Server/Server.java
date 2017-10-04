package com.JSCS.maven.SocketClient_Server;

/**
 * The Server Class, part of the SocketClient-Server Package.
 * 
 *    This listens for connections on PORTNUMBER port, then feeds the resulting socket into an instance of RequestsProcessor. It then proceeds
 *    to wait for more connections, once RequestProcessor has completed its tasks.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static final int PORTNUMBER = 2018;
	private static final int CONNECTION_LIMIT = 100;//server will close after this many connections processed
	private static final String OUTPUT_FILE = "backups/classes.txt";
	private RequestProcessor rp;
	private String filePath;
	
	
	
	public Server(String filePath){//if file provided
		this.filePath = filePath;
		rp = new RequestProcessor(filePath);
	}
	
	public Server(){//if no file provided, default to backups/classes.txt
		filePath = OUTPUT_FILE;
		rp = new RequestProcessor(filePath);
	}
	
	public void start(){
		
		try {
			int a = 0;
			ServerSocket serverSocket = new ServerSocket(PORTNUMBER);
			
			while (a<CONNECTION_LIMIT){
			
				Socket clientSocket = serverSocket.accept();
				rp.processRequest(clientSocket);
				a++;
			}
			
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error connecting to client, breaking connection.");;
		} catch (StopServerException e) {
			
			System.out.println("Server: Shutting Down Server...");
		}
	}

}
