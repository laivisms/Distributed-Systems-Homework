package com.JSCS.maven.SocketClient_Server;

public class ErrorMessages {
	public static final String GENERAL = "Syntax Error. Accepted requests are: GET, ADD, DELETE, QUIT, or STOP. Type HELP for more info.";
	public static final String GET_WO_CODE = "Please input a valid, 3 letter class code after GET. Type HELP for more info.";
	public static final String ADD_WO_CRN_TITLE = "Error: please input a CRN and course title after the class code, seperated by a space. Type HELP for more info.";
	public static final String MISSING_TITLE = "Error: Course title required. Type \"HELP\" for more info.";
	public static final String INVALID_CODE = "Error: code must be one or more letters long. Type HELP for more info.";
	public static final String INVALID_CRN = "Error: CRN must be one or more digits long. Type HELP for more info.";
	public static final String INVALID_TITLE = "Error: title must be one or more characters long, not including whitespace.  Type HELP for more info.";
}
