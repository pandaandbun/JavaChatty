package Client;

import Client.ClientGUI;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.*;

public class Client {

	private DataOutputStream output;
	private DataInputStream input;
	private Socket connection;
	private ClientGUI test = new ClientGUI();
	private TextArea taBox;

	// constructor
	public Client(TextArea taBox) {
		this.taBox = taBox;
		try {
			connectToServer();
			setupStreams();
			new Thread(() -> {
				try {
					whileChatting();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();

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
		input = new DataInputStream(connection.getInputStream());
		output = new DataOutputStream(connection.getOutputStream());
		output.flush();
	}

	// while chatting with server
	public void whileChatting() throws IOException {
		String message = "";
		do {
			message = (String) input.readUTF();
			if (message.equals("END")) {
				closeConnection();
			}
			taBox.appendText(message);
		} while (!message.equals("END"));
	}

	// Close connection
	private void closeConnection() {
		try {
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
