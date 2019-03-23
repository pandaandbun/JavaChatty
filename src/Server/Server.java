package Server;

import Server.Database.createProfile;
import Server.Database.Login;
import Server.Database.User;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Server extends Application {

	// Mapping of sockets to clientEmails
	private Hashtable<String, DataOutputStream> clients = new Hashtable<String, DataOutputStream>();
	// static createProfile cp = new createProfile();

	// Text area for displaying contents
	private TextArea ta = new TextArea();

	// Server socket
	private ServerSocket serverSocket;

	private VBox vb = new VBox();
	private Button exitBt = new Button("Exit");

	// Database
	private createProfile cb = new createProfile();
	private Login lg = new Login();
	// private User ur = new User();

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		ta.setEditable(false);

		vb.getChildren().addAll(ta, exitBt);
		vb.setPadding(new Insets(20, 20, 50, 20));
		vb.setSpacing(10);

		exitBt.setOnAction(e -> {
			System.exit(0);
		});

		// Create a scene and place it in the stage
		Scene scene = new Scene(vb);
		primaryStage.setTitle("Lab 10"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		new Thread(() -> listen()).start();
	}

	private void listen() {
		try {
			// Create a server socket
			serverSocket = new ServerSocket(8080);
			Platform.runLater(() -> ta.appendText("MultiThreadServer started at " + new Date() + '\n'));

			while (true) {
				// Listen for a new connection request
				Socket socket = serverSocket.accept();

				// Display the client number
				Platform.runLater(() -> ta.appendText("Connection from " + socket + " at " + new Date() + '\n'));

				// Create a new thread for the connection
				new ServerThread(socket);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	class ServerThread extends Thread {
		private Socket socket;
		private DataInputStream din;
		private DataOutputStream dout;

		/** Construct a thread */
		public ServerThread(Socket socket) {
			this.socket = socket;
			start();
		}

		/** Run a thread */
		public void run() {
			try {
				// Create data input and output streams
				din = new DataInputStream(socket.getInputStream());
				dout = new DataOutputStream(socket.getOutputStream());

				// Continuously serve the client
				while (true) {
					String line = din.readUTF();
					// Making sense of the message sent by the client
					String[] splitted = line.split("#");
					String command = splitted[0];

					// Add chat to the server ta
					ta.appendText(line + '\n');

					// END
					if (command.equals("END")) {
						String clientEmail = splitted[1];
						logOut(clientEmail);
						break;
					}

					// REGISTRATION
					if (command.equals("REGISTER")) {
						String clientEmail = splitted[1];
						String password = splitted[2];
						Register(clientEmail, password);
					}

					// LOGIN
					if (command.equals("LOGIN")) {
						String clientEmail = splitted[1];
						String password = splitted[2];
						Login(clientEmail, password);
					}

					// ADD FRIEND (IN PROGRESS)
					if (command.equals("ADDFRIEND")) {
						String clientEmail = splitted[1];
						String friendEmail = splitted[2];
						addFriend(clientEmail, friendEmail);
					}

					// MESSAGE
					if (command.equals("MESSAGE")) {
						String clientEmail = splitted[1];
						String friendEmail = splitted[2];
						String message = splitted[3];
						sendMessage(clientEmail, friendEmail, message);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		// send message to client
		public void sendMessage(String clientEmail, String friendEmail, String message) throws IOException {
			clients.get(friendEmail).writeUTF("\n" + clientEmail + ": " + message);
			clients.get(friendEmail).flush();
		}

		public void logOut(String clientEmail) throws IOException {
			dout.writeUTF("END");
			clients.remove(clientEmail);
			din.close();
			socket.close();
		}

		public void Register(String clientEmail, String password) throws IOException {
			boolean isProfileinDB = cb.checkProfile(clientEmail);
			boolean isProfileCreated = false;

			if (!isProfileinDB) {
				isProfileCreated = cb.createProfile(clientEmail, password);
				if (isProfileCreated) {
					clients.put(clientEmail, dout);
					dout.writeUTF("\nSERVER: Account Registered.");

				} else {
					dout.writeUTF("\nSERVER: Account Failed to Registered.");
				}
			} else if (isProfileinDB) {
				dout.writeUTF("\nSERVER: Account already existed.");
			}

		}

		public void Login(String clientEmail, String password) throws IOException {
			System.out.println("Login");
			boolean isProfileinDB = cb.checkProfile(clientEmail);

			if (isProfileinDB) {
				System.out.println("Login - 1");
				if (clients.containsKey(clientEmail)) {
					dout.writeUTF("\nSERVER: Account Already Online.");
				}

				if (!lg.CheckLogin(clientEmail, password)) {
					dout.writeUTF("\nSERVER: Incorrect Email or Password.");
				}

				if (!clients.containsKey(clientEmail) && lg.CheckLogin(clientEmail, password)) {
					clients.put(clientEmail, dout);
					dout.writeUTF("\nSERVER: Account Logged In.");
				}
			} else if (!isProfileinDB) {
				System.out.println("Login - 5");
				dout.writeUTF("\nSERVER: Account Not Registered.");
			}
		}

		public void addFriend(String clientEmail, String friendEmail) throws IOException {
			boolean isProfileinDB = cb.checkProfile(friendEmail);

			if (isProfileinDB) {
				// ur.addFriend(clientEmail, friendEmail);
				dout.writeUTF("\nSERVER: " + friendEmail + " was added to" + clientEmail + "'s Friend List.");
			} else if (!isProfileinDB) {
				dout.writeUTF("\nSERVER: " + friendEmail + " is already in" + clientEmail + "'s Friend List.");
			}
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
