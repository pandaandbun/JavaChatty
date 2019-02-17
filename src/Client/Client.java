package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	
	/* tutorial
	String msg, respond;
	
	Socket socket = new Socket("127.0.0.1", 8080);
	Scanner sc = new Scanner (System.in); //local
	Scanner sc1 = new Scanner (socket.getInputStream()); //server responding
	
	System.out.println("Enter a msg: ");
	msg = sc.nextLine();
	
	PrintStream p = new PrintStream (socket.getOutputStream()); //print to server
	p.println(msg);
	
	
	respond = sc1.nextLine();
	System.out.println(respond);
	*/

	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input; 
	private String message = "";
	private String serverIP;

}
