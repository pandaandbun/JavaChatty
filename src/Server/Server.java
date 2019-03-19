package Server;

import Server.Database.Datebase;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

	static ArrayList<DataOutputStream> clients = new ArrayList<DataOutputStream>();
	static ArrayList<String> emails = new ArrayList<String>();
	static Datebase db = new Datebase();

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
								// Message
								String line = dIn.readUTF();

								System.out.println("Command----" + line);

								// Make sense of the Message
								String[] splitted = line.split("#");
								String command = splitted[0];

								// Registration Command
								if (command.toLowerCase() == "register") {
									String clientEmail = splitted[1];
									String password = splitted[2];

									if (Register(clientEmail, password)) {
										System.out.println("Registering " + clientEmail + "succeeded");
										dOut.writeUTF("SERVER - " + clientEmail + " succesfully created!");
									} else if (!Register(clientEmail, password)) {
										System.out.println("Registering " + clientEmail + "failed");
										dOut.writeUTF("SERVER - " + clientEmail + " already exist!");
									}
								}

								// Log in Command
								if (command.toLowerCase() == "login") {
									String clientEmail = splitted[1];
									String password = splitted[2];

									if (Login(clientEmail, password)) {
										System.out.println("Logging in " + clientEmail + "succeeded");
										dOut.writeUTF("SERVER - " + clientEmail + " succesfully Login!");

										// Send Client their friend list
										String friendList = String.join("#", getFriends(clientEmail));
										dOut.writeUTF(friendList);

									} else if (!Login(clientEmail, password)) {
										System.out.println("Logging in " + clientEmail + "failed");
										dOut.writeUTF("SERVER - " + clientEmail + " either does not exist or is already login!");
									}
								}

								// Add friend Command
								if (command.toLowerCase() == "addfriend") {
									String clientEmail = splitted[1];
									String friendEmail = splitted[2];

									if (addFriend(clientEmail, friendEmail)) {
										
										System.out.println("Adding " + friendEmail + " as a friend of " + clientEmail + "succeeded");
										dOut.writeUTF("SERVER - " + friendEmail + " was added succesfully!");
										
									} else if (!addFriend(clientEmail, friendEmail)) {
										
										System.out.println("Adding " + friendEmail + " as a friend of " + clientEmail + "failed");
										dOut.writeUTF("SERVER - " + clientEmail + " either does not exist or is already your friend!");
									}
								}

								// Message Command
								if (command.toLowerCase() == "message") {
									String clientEmail = splitted[1];
									String friendEmail = splitted[2];
									String message = splitted[3];

									if (!emails.contains(friendEmail)) {
										System.out.println(friendEmail + " is not online");
										dOut.writeUTF("SERVER - " + friendEmail + " is not online.");
									}

									if (emails.contains(friendEmail)) {
										System.out.println(clientEmail + " --> " + friendEmail + " ---- " + message);
										clients.get(emails.indexOf(friendEmail)).writeUTF(clientEmail + " - " + message);
										clients.get(emails.indexOf(friendEmail)).flush();
									}
								}

								// Logout Command
								if (command.toLowerCase() == "logout") {
									String clientEmail = splitted[1];
							
									clients.remove(dOut);
									emails.remove(clientEmail);
									dIn.close();
									dOut.close();
									socket.close();
									
									System.out.println(clientEmail + " successfully logout!");
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
	
	

	public static boolean Register(String clientEmail, String password) {
		boolean isProfileinDB = db.checkProfile(clientEmail);
		boolean isProfileinDBNow = false;

		if (!isProfileinDB) {
			isProfileinDBNow = db.createProfile(clientEmail, password);
		}

		return isProfileinDBNow;
	}

	public static boolean Login(String clientEmail, String password) {
		boolean isProfileinDB = db.checkProfile(clientEmail);

		if (isProfileinDB) {
			if (!emails.contains(clientEmail)) {
				emails.add(clientEmail);
				return db.logIn(clientEmail, password);
			} else if (emails.contains(clientEmail)) {
				return false;
			}
		} else if (!isProfileinDB) {
			return false;
		}
	}
	
	public static String[] getFriends(String clientEmail) {

		String[] friendList = db.getFriendList(clientEmail);
		return friendList;
	}

	public static boolean addFriend(String clientEmail, String friendEmail) {
		boolean isProfileinDB = db.checkProfile(friendEmail);

		if (isProfileinDB) {
			return db.addFriend(clientEmail, friendEmail);
		} else if (!isProfileinDB) {
			return false;
		}
	}

}