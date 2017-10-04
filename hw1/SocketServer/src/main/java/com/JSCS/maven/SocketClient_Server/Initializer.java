package com.JSCS.maven.SocketClient_Server;

public class Initializer {
	
	public static void main(String args[]){
		Thread server = null;
		
		if(args.length>0){
			server = new Thread(initializeServer(args[0]));
		}
		else{
			server = new Thread(initializeServer());
		}
		
		Thread client = new Thread(initializeClient());
		
		server.start();
		client.start();
		
	}
	
	
	
	private static Runnable initializeClient(){
		return new Runnable() {
			public void run(){
				
			    Client client = new Client();
			    
				client.start();
			}
		};
	}
	
	private static Runnable initializeServer(){
		return new Runnable() {
			public void run(){
				
			    Server server = new Server();
			    
				server.start();
			}
		};
	}
	
	private static Runnable initializeServer(final String path){
		return new Runnable() {
			public void run(){
				
			    Server server = new Server(path);
			    
				server.start();
			}
		};
	}
}
