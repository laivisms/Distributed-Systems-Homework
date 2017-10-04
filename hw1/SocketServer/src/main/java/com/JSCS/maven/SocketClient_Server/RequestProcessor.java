package com.JSCS.maven.SocketClient_Server;


/**
 * The RequestProcessor Class, part of the SocketClient-Server package.
 * 
 * 		This class handles connected clients, retrieving the clients information, deciding what to do with it, then writing
 * an appropriate answer back into the socket. When instantiated, this class creates a Database, which it then accesses in order to 
 * process requests from clients.
 * 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestProcessor {
	
	//private static final int TIMEOUT = 10;
	private static final String HELP_MESSAGE = 
			 "Accepted Requests:\n" +
			 "GET Request (Format: \"GET 3-letter-major-code\"): Retrieves information on all courses with designated class code.\n"+
			 "ADD Request (Format: \"ADD 3-letter-major-code CRN course-title\") Adds the designated course and information to the list of courses.\n" + 
			 "DELETE Request (Format: \"DELETE CRN\"): Deletes the course associated with the designated CRN from the list of courses.\n" + 
			 "HELP Request (Format: \"HELP\"): Opens this menu.\n" +
			 "QUIT Request (Format: \"QUIT\" or \"STOP\"): Safely shuts down the server and this client.\n";
			
	private Database db;
	
	public RequestProcessor(String FilePath){// initializes the database, which moves all entries from persistent storage into memory
		db = new Database(FilePath);
	}
	/**
	 * Reads the information from the socket, calls parseMessage on the resulting string, then returns the answer
	 * @param clientSocket The socket from which the information is read, and to which the answer is written.
	 * @throws StopServerException 
	 */
	protected void processRequest(Socket clientSocket) throws StopServerException{
		ObjectOutputStream out;
		try {        
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            byte[] result = null;     
	            
	        String fullMessage = new String((byte[]) in.readObject());// get the message from the client

            try {
				result = parseMessage(fullMessage).getBytes();//feed the message to the parser, getting the resulting message back
			} catch (StopServerException e) { //if it was a quit request
	            
				clientSocket.close();
	        
	            throw new StopServerException();
			}	
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();//flush immediately so connected stream detects header
            out.writeObject(result);//write the result to the stream
            out.flush();
            
            clientSocket.close();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * Determines what type of request the message is asking, extracts the appropriate information from the request, asks the 
	 * Database to perform the corresponding action, feeding it the relevant information, then returns the answer given by the Database
	 * @param message The request from the client
	 * @return The answer, appropriate to the request
	 * @throws StopServerException 
	 */
	private String parseMessage(String message) throws StopServerException{
		String result = ErrorMessages.GENERAL;// default to general error message
		
		if(message.length() >= 3){
			String request = message.substring(0,3);
			
			if(request.toUpperCase().equals("GET")){
				if(message.length() == 7){// get messages can only be length 7
					String code = message.substring(4, 7).toUpperCase();
					result = db.getCode(code);
				}
				else{
					result = ErrorMessages.GET_WO_CODE;
				}
			}
			
			else if(request.toUpperCase().equals("ADD")){
				if(message.length() > 8){// add messages have to be at least length 9, otherwise there is no CRN or title
					String code = message.substring(4, 7).toUpperCase();
					
					int startCRN = message.indexOf(" ", message.indexOf(" ") + 1) + 1;//beginning of the CRN
					int endCRN = message.indexOf(" ", startCRN + 1);//end of the CRN, determined by the second whitespace
					if(endCRN >= message.length() || endCRN == -1){// there was no second whitespace, or the message only had CRN, no title
						result = ErrorMessages.MISSING_TITLE;
					} else {
						String CRN = message.substring(startCRN, endCRN);
						String title = message.substring(endCRN + 1, message.length());
						
						result = db.addCourse(code, CRN, title);
					}
				}
				else{//message too short
					result = ErrorMessages.ADD_WO_CRN_TITLE;
					
				}
			}
		
			else if(message.length() > 5 && message.substring(0,6).toUpperCase().equals("DELETE")){
				if(message.length() > 7) {
					String CRN = message.substring(7, message.length());
					result = db.deleteCRN(CRN);
				}
				else{//no CRN provided
					result = "Error: Please input a valid CRN after the DELETE request. Type HELP for more info.";
					
				}
			}
			
			else if(message.toUpperCase().equals("HELP")){
				result = HELP_MESSAGE;
			}
			else if(message.toUpperCase().equals("STOP") || message.toUpperCase().equals("QUIT")){
				throw new StopServerException();
			}
			
		} 
		
		return result;
		
	}
}
