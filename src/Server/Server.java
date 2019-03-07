package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

	static ArrayList<DataOutputStream> clients = new ArrayList<DataOutputStream>();
	static ArrayList<String> emails = new ArrayList<String>();

	public static void setEmail() {

	}

	public static void main(String[] args) {
		int port = 8080; // random port numbes

		try {
			ServerSocket ss = new ServerSocket(port);

			System.out.println("Waiting for a client....");
			System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
			System.out.println();

			while (true) {
				Socket socket = ss.accept();

				// SSocket sSocket = new SSocket(socket, clients);
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							DataInputStream dIn = new DataInputStream(socket.getInputStream());
							DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
							clients.add(dOut);

							while (true) {
								String line = dIn.readUTF();
								System.out.println("Recievd the line----" + line);
								
								String[] splited = line.split("#");
								String message = splited[2];
								String clientEmail = splited[0];
								String friendEmail = splited[1];
								
								if (!emails.contains(clientEmail)) {
									emails.add(clientEmail);
								} 
								
								if (!emails.contains(friendEmail)) {
									dOut.writeUTF(friendEmail + " is not online.");
								} 
								
								
								if (message.equals("END")) {
									dIn.close();
									dOut.close();
									socket.close();
								}
																
								if (emails.contains(friendEmail)) {
									clients.get(emails.indexOf(friendEmail)).writeUTF(clientEmail + " - " + message);
									clients.get(emails.indexOf(friendEmail)).flush();
								}
								
								System.out.println();
							}
						} catch (Exception e) {
							System.out.println(e);
						}

					}

				});
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}