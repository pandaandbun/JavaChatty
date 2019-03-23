package Server;

import Server.Database.createProfile;
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
	private Hashtable clients = new Hashtable();
	// static createProfile cp = new createProfile();

	// Text area for displaying contents
	private TextArea ta = new TextArea();

	// Server socket
	private ServerSocket serverSocket;

	private VBox vb = new VBox();
	private Button exitBt = new Button("Exit");

	private createProfile cb = new createProfile();

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

				// Save output stream to hashtable
				// clients.put(clientEmail, dout);

				// Create a new thread for the connection
				new ServerThread(socket);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	class ServerThread extends Thread {
		private Socket socket;
		//private String[] splitted;
		//private String clientEmail = "";
		//private String friendEmail = "";
		//private String password = "";
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

					if (line.equals("END")) {
						din.close();
						socket.close();
						break;
					}
					
					// Making sense of the message sent by the client
					String[] splitted = line.split("#");
					String command = splitted[0];

					// REGISTRATION
					if (command.equals("REGISTER")) {
						String clientEmail = splitted[1];
						String password = splitted[2];
						Register(clientEmail, password); 
					}

					
					// Add chat to the server ta
					// ta.appendText(clientEmail + ": " + line + '\n');
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		public void Register(String clientEmail, String password) throws IOException {
			boolean isProfileinDB = cb.checkProfile(clientEmail);
			boolean isProfileCreated = false;

			if (!isProfileinDB) {
				isProfileCreated = cb.createProfile(clientEmail, password);
				if (isProfileCreated) {
					dout.writeUTF("SERVER: Account Registered.");
				} else {
					dout.writeUTF("SERVER: Account Failed to Registered.");
				}
			} else if (isProfileinDB) {
				dout.writeUTF("SERVER: Account already existed.");
			}

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
