package Server;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Server {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
		

	
	public void startRunning(){        
		try{
			server = new ServerSocket(6789, 100); //6789 is a dummy port for testing, this can be changed. The 100 is the maximum people waiting to connect.
			while(true){
				try{
					//Trying to connect and have conversation
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					System.out.println("\n Server ended the connection! ");
				} finally{
					closeConnection(); //Changed the name to something more appropriate
				}
			}
		} catch (IOException ioException){
			ioException.printStackTrace();
		}
	}
	//wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		System.out.println(" Waiting for someone to connect... \n");
		connection = server.accept();
		System.out.println(" Now connected to " + connection.getInetAddress().getHostName());
	}
	
	//get stream to send and receive data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		
		System.out.println("\n Streams are now setup \n");
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		String message = " You are now connected! ";
		sendMessage(message);
		do{
			try{
				message = (String) input.readObject();
				System.out.println("\n" + message);
			}catch(ClassNotFoundException classNotFoundException){
				System.out.println("The user has sent an unknown object!");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	public void closeConnection(){
		System.out.println("\n Closing Connections... \n");
		try{
			output.close(); //Closes the output path to the client
			input.close(); //Closes the input path to the server, from the client.
			connection.close(); //Closes the connection between you can the client
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//Send a message to the client
	private void sendMessage(String message){
		try{
			output.writeObject("SERVER - " + message);
			output.flush();
			System.out.println("\nSERVER -" + message);
		}catch(IOException ioException){
			chatWindow.append("\n ERROR: CANNOT SEND MESSAGE, PLEASE RETRY");
		}
	}
}