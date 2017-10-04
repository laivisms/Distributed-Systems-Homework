package com.JSCS.maven.SocketClient_Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	
	private static final String HOSTIP = "127.0.0.1";
	private static final int PORT = 2018;
	private static final int TIMEOUT = 5;
	
	public static void main(String args[]){
		/*try {
		BufferedReader reader = new BufferedReader(new FileReader("Courses/mat.txt"));
	    
	        
	        String line = reader.readLine();

	        while (line != null) {
	           line = "MAT " + line.trim().replaceAll("\\t", " ");
	           addCourse(line);
	           
	           System.out.println(line);
	            line = reader.readLine();
	        }
	        reader.close();
	    } catch (IOException e) {
			e.printStackTrace();
		} */
		//System.out.print(deleteCourses("79289"));
	}
	
	public void start(){
		Scanner scanner = new Scanner(System.in);
		String input;
		String result;
		System.out.println("Please input query for the server, or \"STOP\" to exit and shutdown server:");
		while(scanner.hasNextLine()){
			input = scanner.nextLine().toLowerCase();
			if(input.equals("quit") || input.equals("stop")){
				exitServer();
				System.out.println("Client: Shutting Down Server...");
				break;
			}
			result = accessServer(input);
			System.out.println(result);
			System.out.println("\nPlease input another query for the server, or \"STOP\" to exit and shutdown server:");
			
		}
		scanner.close();
	}
	
	public static void exitServer(){
		try {
			InetAddress serverAddr = InetAddress.getByName(HOSTIP);
			
			Socket server = new Socket(serverAddr, PORT);
			
			ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
	        
	        byte[] fromClient = "quit".getBytes();
	        
	        
			out.writeObject(fromClient);
	        out.flush();
	        server.shutdownOutput();
	        server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String accessServer(String message){
		return accessServerAux(message, 0);
	}
	
	public static String accessServerAux(String message, int tryCount){
		String fromServer = "";
		try {
			Thread.sleep(10);
			InetAddress serverAddr = InetAddress.getByName(HOSTIP);
			
			Socket server = new Socket(serverAddr, PORT);
			
			ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
            out.flush();
            byte[] fromClient = message.getBytes();
            
            out.writeObject(fromClient);
            out.flush();
            server.shutdownOutput();
            
           ObjectInputStream in = new ObjectInputStream(server.getInputStream());
          
           fromServer = new String((byte[]) in.readObject());
            /*BufferedReader in = new BufferedReader(
                    new InputStreamReader(server.getInputStream()));
            String inputLine = "";
           while (inputLine != null) {
                fromServer += inputLine;
                
                inputLine = in.readLine();
                
                if(inputLine != null)
                	fromServer += "\n";
            }*/
          
            server.close();
            
            return fromServer;
           
			
		} catch (IOException e) {
			return connectionRetry(message, tryCount);
		}
		catch(InterruptedException e){
			return connectionRetry(message, tryCount);
		}
		catch(ClassNotFoundException e){
			return connectionRetry(message, tryCount);
		}
		
		 
	}
	
	private static String connectionRetry(String message, int tryCount){
		if(tryCount < TIMEOUT){
			System.out.println("Server refused to connect, retrying...");
			return accessServerAux(message, tryCount+1);
		}
		return "Error Contacting Server: Cannot contact server at: " + HOSTIP + ":" + PORT;
	}
	
	private static String getCourses(String code){
		return accessServer("GET " + code);
	}
	
	private static String deleteCourses(String CRN){
		return accessServer("DELETE " + CRN);
	}
}
