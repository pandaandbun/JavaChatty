package Client;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.*;

public class Client {

	// Input and Output
	private DataOutputStream output;
	private DataInputStream input;

	// Socket Connection
	private Socket connection;

	// Chat Box
	private TextArea taBox;

	// constructor
	public Client() {
		// this.taBox = taBox;
		try {
			// Client - Server Connection Set Up
			connectToServer();
			setupStreams();

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void setUpdatechat(TextArea taBox) {
		this.taBox = taBox;
	}

	public void startListener() {
		// Creating a new thread to listen
		new Thread(() -> {
			try {
				listener();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public DataInputStream getInput() {
		return input;
	}

	// Connect to server
	private void connectToServer() throws IOException {
		connection = new Socket("127.0.0.1", 8080);
	}

	// Set up streams
	private void setupStreams() throws IOException {
		input = new DataInputStream(connection.getInputStream());
		output = new DataOutputStream(connection.getOutputStream());
		output.flush();
	}

	// Client Listening for messages from the server
	public void listener() throws IOException {
		String message = "";
		do {
			message = (String) input.readUTF();
			if (!message.equals("END")) {
				taBox.appendText(message);
			}
		} while (!message.equals("END"));
	}

	// Close connection
	public void closeConnection() {
		try {
			output.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Send message to server
	public void sendMessage(String message) {
		try {
			output.writeUTF(message);
			output.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
