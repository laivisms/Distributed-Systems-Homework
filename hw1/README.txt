	The SocketClient-Server program. Creates a Server which accepts certain requests, and 
returns or edits information it has stored on courses for Yeshiva University. Valid requests are:


		GET Request (Format: \"GET 3-letter-major-code\"): Retrieves information on 
			all courses with designated class code.
		ADD Request (Format: \"ADD 3-letter-major-code CRN course-title\") Adds 
			the designated course and information to the list of courses.
		DELETE Request (Format: \"DELETE CRN\"): Deletes the course associated 
			with the designated CRN from the list of courses.
		HELP Request (Format: \"HELP\"): Opens this menu.
		QUIT Request (Format: \"QUIT\" or \"STOP\"): Safely shuts down the server and 
			this client.	

	The program is started by calling the main method of the Initializer class. This then starts 
two new threads, one containing an instance of Server, and one containing an instance of Client.
The Server then initializes a RequestProcessor(which in turn initializes the Database, loading
all the persitent information into memory) and blocks, waiting for a connection. When a connection is 
recieved, it passes the created socket to the RequestProcessor, which reads the message from the socket,
determines what the client wants, and accesses the Database's API to retrieve/edit information. Possible
Syntax error messages are stored in the ErrorMessages class. The RequestProcessor wtites its response
into the socket, flushes the socket, and closes the socket, thus ending the transaction.

	Persistent information is stored in JSON format, as follows:

		[{"code":"3-letter-major-code","CRN":"12345","title":"course-title"}, {"code":"3-letter-major-code","CRN":"12346","title":"course-title2"}, ...]

	Description: a list of 3-tuples describing the course, all writted to one line.

A preset list of courses is provided in the "backups" folder, called "classes.txt". The program will
default to this path for storage if one is not provided by the user in the command-line arguments.
If a path is provided, program will store all saves in the specified folder, under the specified
name, creating new numbered files.

Backups are made after every ADD and DELETE operation.

The program relies on Maven dependencies. In order to run, use the system variable "filePath" to pass
the location of the file to the program. Example, using test:

	mvn test -DfilePath="path/to/the/file.txt"

or, using the provided course file:

	mvn test -DfilePath="backups/classes.txt"

