package Server;

import java.io.*;
import java.net.*;

public class Server {

	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	public void startRunning () {
		try {
			
			server = new ServerSocket (8080);
			while (true) {
				try {
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eoException) {
					System.out.println("Server ended the connection!");
				} finally {
					closeSocket();
				}
			}
			
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//Wait for connection
	private void waitForConnection () throws IOException {
		System.out.println("Waiting for someone to connect...");
		connection = server.accept();
		System.out.println("Now Connected to " + connection.getInetAddress().getHostName());
	}
	
	//Send and Receive Data
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream (connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream (connection.getInputStream());
		System.out.println("Streams are now setup!");
	}
	
	//During the chat
	private void whileChatting() throws IOException{
		String message = "You are now connected!";
		sendMessage(message);
		
		do {
			try {
				
				message = (String) input.readObject();
				System.out.println(message);
				
			}catch(ClassNotFoundException classNotFoundException) {
				System.out.println("Error at whileChatting().");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	//close streams and sockets after you are done chatting
	private void closeSocket() {
		System.out.println("...Closing Connections...");
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//send a message to client
	private void sendMessage (String msg) {
		try {
			output.writeObject("Server - " + msg);
			output.flush();
			System.out.println("Server - " + msg);
		}catch(IOException ioException) {
			System.out.println("Error at sendMessage()");
		}
	}

}
