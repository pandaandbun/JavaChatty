package Client;

import java.io.*;
import java.net.*;

public class Client {

	private DataOutputStream output;
	private Socket connection;

	// constructor
	public Client() {
		try {
			connectToServer();
			setupStreams();
		} catch (EOFException eofException) {
			eofException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// connect to server
	private void connectToServer() throws IOException {
		connection = new Socket("127.0.0.1", 8080);
	}

	// set up streams
	private void setupStreams() throws IOException {
		output = new DataOutputStream(connection.getOutputStream());
		output.flush();
	}

	// Close connection
	public void closeConnection() {
		try {
			output.writeUTF("END");
			output.flush();
			output.close();
			connection.close();
			System.exit(0);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	// send message to server
	public void sendMessage(String message) {
		try {
			output.writeUTF(message);
			output.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
