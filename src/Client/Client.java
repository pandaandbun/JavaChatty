package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Client extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private DataOutputStream output;
	private DataInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	private String clientEmail = "";
	private String friendEmail = "";

	// constructor
	public Client(String host, String clientEmail, String friendEmail) {
		super("Client");
				
		serverIP = host;
		this.clientEmail = clientEmail;
		this.friendEmail = friendEmail;
		
		userText = new JTextField();
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300, 150); // Sets the window size
		setVisible(true);
	}

	// connect to server
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		} catch (EOFException eofException) {
			showMessage("\n Client terminated the connection");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	// connect to server
	private void connectToServer() throws IOException {
		showMessage("Attempting connection... \n");
		connection = new Socket(InetAddress.getByName(serverIP), 8080);
		showMessage("Connection Established! Connected to: " + connection.getInetAddress().getHostName());
	}

	// set up streams
	private void setupStreams() throws IOException {
		output = new DataOutputStream(connection.getOutputStream());
		output.flush();
		input = new DataInputStream(connection.getInputStream());
		showMessage("\n The streams are now set up! \n");
	}

	// while chatting with server
	private void whileChatting() throws IOException {
		do {
			message = (String) input.readUTF();
			showMessage("\n" + message);
		} while (!message.equals("dEND"));
	}

	// Close connection
	private void closeConnection() {
		showMessage("\n Closing the connection!");
		try {
			output.close();
			input.close();
			connection.close();
			System.exit(0);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// send message to server
	private void sendMessage(String message) {
		try {
			output.writeUTF(clientEmail + "#" + friendEmail + "#" + message);
			output.flush();
			showMessage("\n" + clientEmail + " - " + message);
		} catch (IOException ioException) {
			chatWindow.append("\n Oops! Something went wrong!");
		}
	}

	// update chat window
	private void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(message);
			}
		});
	}
}
