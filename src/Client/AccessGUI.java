package Client;

import java.io.DataInputStream;
import java.io.IOException;

import Client.ChatBoxGUI;
import javafx.application.Application;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccessGUI extends Application {

	private DataInputStream input;
	private String serverMessage = "";
	
	// Initializing the connection between Client and Server
	private Client cli = new Client();
	
	// Initializing ChatBox 
	private ChatBoxGUI ChatBox = new ChatBoxGUI();

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws IOException {
		// User input text fields
		TextField tfName = new TextField();
		TextField tfPass = new TextField();
		
		// Adding System Display
		TextArea alert = new TextArea();
		alert.setEditable(false);
		alert.setPrefSize(400, 50);
		alert.setText("Welcome to JavaChatty! The best Chat Room of 2019.");
		alert.setStyle("-fx-text-fill: white;-fx-font-size: 15px;");

		// Buttons to send commands to the server
		Button regBt = new Button("Register");
		Button logBt = new Button("Log In");
		Button exitBt = new Button("Exit");

		// Setting containers for User input text fields
		HBox hbUser = new HBox(new Label("User: "), tfName);
		HBox hbMsg = new HBox(new Label("Password: "), tfPass);

		// Setting containers for Command Buttons
		HBox firstBox = new HBox(regBt, logBt);
		firstBox.setSpacing(10);

		// Adding everything into a vertical box
		VBox vb = new VBox();
		vb.getChildren().addAll(alert, hbUser, hbMsg, firstBox, exitBt);
		vb.setPadding(new Insets(20, 20, 50, 20));
		vb.setSpacing(10);
		vb.setStyle("-fx-base: rgba(60, 60, 60, 255);");
		
		// Setting the data input stream 
		input = cli.getInput();

		// EXIT
		exitBt.setOnAction(e -> {
			String message = "END" + "#";
			cli.sendMessage(message);
			cli.closeConnection();
			System.exit(0);
		});

		// LOGIN
		logBt.setOnAction(e -> loginHandler(tfName, tfPass, alert, primaryStage));

		// REGISTER
		regBt.setOnAction(e -> registrationHandler(tfName, tfPass, alert, primaryStage));

		// Create a scene and place it in the stage
		Scene scene = new Scene(vb);
		primaryStage.setTitle("JavaChatty - Login"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}
	
	public void enableListener() throws IOException {
		serverMessage = (String) input.readUTF();
	}
	
	public void loginHandler (TextField tfName, TextField tfPass, TextArea alert, Stage primaryStage) {
		String username = tfName.getText();
		String message = "LOGIN" + "#" + username + "#" + tfPass.getText();
		cli.sendMessage(message);

		try {
			enableListener();
			
			if (serverMessage.equals("Account Logged In.")) {
				primaryStage.hide();
				ChatBox.display(primaryStage, cli, username);
			} else if (serverMessage.equals("Incorrect Email or Password.")) {
				alert.setText("Incorrect Email or Password.");
			} else if (serverMessage.equals("Account Already Online.")) {
				alert.setText("Account Already Online.");
			} else if (serverMessage.equals("Account Not Registered.")) {
				alert.setText("Account Not Registered.");
			}
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}
	
	public void registrationHandler (TextField tfName, TextField tfPass, TextArea alert, Stage primaryStage) {
		String username = tfName.getText();
		String message = "REGISTER" + "#" + username + "#" + tfPass.getText();
		cli.sendMessage(message);

		try {
			enableListener();
			
			if (serverMessage.equals("SUCCESS")) {
				primaryStage.hide();
				ChatBox.display(primaryStage, cli, username);
			} else if (serverMessage.equals("FAIL")) {
				alert.setText("Account Failed to Register. Account already exist.");
			}
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
