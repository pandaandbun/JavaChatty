package Server;

//Database
import Server.Database.createProfile;
import Server.Database.Login;
import Server.Database.friend;

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
	// Purpose: To keep track of online users
	private Hashtable<String, DataOutputStream> clients = new Hashtable<String, DataOutputStream>();

	// Text area for displaying contents
	private TextArea ta = new TextArea();

	// Server socket
	private ServerSocket serverSocket;

	// Pane and Exit Button
	private VBox vb = new VBox();
	private Button exitBt = new Button("Exit");

	// Database
	private createProfile cb = new createProfile();
	private Login lg = new Login();
	private friend fr = new friend();

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {

		// Setting the text display to be not editable
		ta.setEditable(false);

		// Adding Text Area and Exit Button to a vertical pane
		vb.getChildren().addAll(ta, exitBt);
		vb.setPadding(new Insets(20, 20, 50, 20));
		vb.setSpacing(10);

		// Styling
		ta.setStyle("-fx-text-fill: white;-fx-font-size: 15px;");
		vb.setStyle("-fx-base: rgba(60, 60, 60, 255);");

		// Set action on exit to terminate the program
		exitBt.setOnAction(e -> {
			System.exit(0);
		});

		// Create a scene and place it in the stage
		Scene scene = new Scene(vb);
		primaryStage.setTitle("JavaChatty's Server"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		new Thread(() -> createConnection()).start();
	}

	private void createConnection() {
		try {
			// Create a server socket
			serverSocket = new ServerSocket(8080);
			Platform.runLater(() -> ta.appendText("JavaChatty's Server started at " + new Date() + '\n'));

			while (true) {
				// Listen for a new connection request
				Socket socket = serverSocket.accept();

				// Display the client number
				Platform.runLater(() -> ta.appendText("Connection from " + socket + " at " + new Date() + '\n'));

				// Create a new thread for each user
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
		private String line = "";

		/** Construct a thread */
		public ServerThread(Socket socket) {
			this.socket = socket;
			start();
		}

		/** Run a thread */
		public void run() {
			try {
				// Create data input and output streams
				setupStreams();

				boolean runServer = true;

				// Continuously serve the client
				while (runServer) {

					// Listen for a message from the clients
					enableListener();

					// Making sense of the message sent by the client
					String[] splitted = line.split("#");
					String command = splitted[0];
					
					String clientEmail;
					String password;
					String friendEmail;
					String message;

					// Add client command to the server display
					ta.appendText(line + '\n');

					// Client Command Handlers
					switch (command) {

					// When a new user register to the Database
					case "REGISTER":
						if (validateCommand(splitted, 3)) {

							clientEmail = splitted[1];
							password = splitted[2];

							Register(clientEmail, password);
						} else {
							dout.writeUTF("\nSERVER: Please fill in all Text Fields");
						}
						break;

					// When a user login into JavaChatty
					case "LOGIN":
						if (validateCommand(splitted, 3)) {
							clientEmail = splitted[1];
							password = splitted[2];

							Login(clientEmail, password);
						}
						break;

					// when a user wants to log out
					case "LOGOUT":
						if (validateCommand(splitted, 2)) {
							clientEmail = splitted[1];
							logOut(clientEmail);
						}
						break;

					// When a user add a friend into their friend list
					case "ADDFRIEND":
						if (validateCommand(splitted, 3)) {
							clientEmail = splitted[1];
							friendEmail = splitted[2];

							addFriend(clientEmail, friendEmail);
						}
						break;

					// When a user send a message to their friend
					case "MESSAGE":
						if (validateCommand(splitted, 4)) {
							clientEmail = splitted[1];
							friendEmail = splitted[2];
							message = splitted[3];

							sendMessage(clientEmail, friendEmail, message);
						}
						break;

					// When a user exit JavaChatty
					case "END":
						End();
						runServer = false;
						break;
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		// Validate the client command
		// Purpose: Clients make mistakes and the server needs to catch them 
		public boolean validateCommand(String[] splitted, int correctSize) throws IOException {
			boolean validated = true;

			if (splitted.length < correctSize) {
				validated = false;
				dout.writeUTF("\nSERVER: Please fill in all necessary Text Fields");
			}

			return validated;
		}

		// Enabling server to listen to clients messages
		public void enableListener() throws IOException {
			line = (String) din.readUTF();
		}

		// Set up 
		// Set up streams
		private void setupStreams() throws IOException {
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			dout.flush();
		}

		// Send message to friend
		public void sendMessage(String clientEmail, String friendEmail, String message) throws IOException {
			boolean isProfileinDB = cb.checkProfile(friendEmail);
			List<String> friendList = fr.getFriendList(clientEmail);

			if (isProfileinDB) {
				if (friendList.contains(friendEmail)) {
					if (clients.containsKey(friendEmail)) {
						dout.writeUTF("\n" + clientEmail + ": " + message);
						clients.get(friendEmail).writeUTF("\n" + clientEmail + ": " + message);
						clients.get(friendEmail).flush();
					} else {
						dout.writeUTF("\nSERVER: " + friendEmail + " is not online.");
					}
				} else {
					dout.writeUTF("\nSERVER: " + friendEmail + " is not your friend.");
				}
			} else {
				dout.writeUTF("\nSERVER: " + friendEmail + " does not exist.");
			}
		}

		// Logging the client out
		public void End() throws IOException {
			// dout.writeUTF("END");
			din.close();
			socket.close();
		}

		// Logging the client out
		public void logOut(String clientEmail) throws IOException {
			// System.out.println("Logout" + dout);
			dout.writeUTF("END");
			if (clients.containsKey(clientEmail)) {
				clients.remove(clientEmail);
			}
			// din.close();
			// socket.close();
		}

		// Register the client to the Database
		public void Register(String clientEmail, String password) throws IOException {
			boolean isProfileinDB = cb.checkProfile(clientEmail);
			boolean isProfileCreated = false;

			if (!isProfileinDB) {
				isProfileCreated = cb.createProfile(clientEmail, password);
				if (isProfileCreated) {
					clients.put(clientEmail, dout);
					// dout.writeUTF("\nSERVER: Account Registered. You are now Logged In");
					dout.writeUTF("SUCCESS");
				}
			} else if (isProfileinDB) {
				dout.writeUTF("FAIL");
			}

		}

		// Logging the client in
		public void Login(String clientEmail, String password) throws IOException {
			boolean isProfileinDB = cb.checkProfile(clientEmail);

			if (isProfileinDB) {
				if (clients.containsKey(clientEmail)) {
					dout.writeUTF("Account Already Online.");
				}

				if (!lg.LogIn(clientEmail, password)) {
					dout.writeUTF("Incorrect Email or Password.");
				}

				if (!clients.containsKey(clientEmail) && lg.LogIn(clientEmail, password)) {
					clients.put(clientEmail, dout);
					dout.writeUTF("Account Logged In.#" + getFriend(clientEmail));
				}
			} else if (!isProfileinDB) {
				dout.writeUTF("Account Not Registered.");
			}
		}

		// Adding friend to the client database
		public void addFriend(String clientEmail, String friendEmail) throws IOException {
			boolean isProfileinDB = cb.checkProfile(friendEmail);

			if (isProfileinDB) {
				if (fr.addFriend(clientEmail, friendEmail)) {
					// dout.writeUTF("\nSERVER: " + friendEmail + " was added to " + clientEmail +
					// "'s Friend List.");
					dout.writeUTF("ADDFRIEND#" + friendEmail);
				} else {
					dout.writeUTF("\nSERVER: " + friendEmail + " is already in " + clientEmail + "'s Friend List.");
				}
			} else if (!isProfileinDB) {
				dout.writeUTF("\nSERVER: " + friendEmail + " does not exist.");
			}
		}

		// Getting the client friend list
		public String getFriend(String clientEmail) throws IOException {
			List<String> friendList = fr.getFriendList(clientEmail);
			String friends = String.join(",", friendList);

			return friends;
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
