package Client;

import java.io.DataInputStream;
import java.io.IOException;

import Client.ChatBoxGUI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

		// Images
		ImageView exitImg = new ImageView("Images/exit.png");
		ImageView loginImg = new ImageView("Images/Login.png");
		ImageView regImg = new ImageView("Images/register.png");

		// Title
		TextFlow textFlow = new TextFlow();
		Font font = new Font("Tahoma", 48);
		Text text1 = new Text("Java");
		Text text2 = new Text("Chatty.");

		// Username field
		TextField tfName = new TextField();

		// Password field
		PasswordField passwordField = new PasswordField();

		// Adding System Display
		TextArea alert = new TextArea();

		// Buttons to send commands to the server
		Button regBt = new Button("Register", regImg);
		Button logBt = new Button("Log In", loginImg);
		Button exitBt = new Button("Exit", exitImg);

		// Setting containers for User input text fields
		VBox vbUser = new VBox(new Label("User: "), tfName);
		VBox vbMsg = new VBox(new Label("Password: "), passwordField);
		VBox userInputBox = new VBox(vbUser, vbMsg);

		// Setting containers for Command Buttons
		HBox accessBox = new HBox(regBt, logBt);

		// Set Text Flow
		textFlow.getChildren().addAll(text1, text2);

		// Adding everything into a vertical box
		VBox rootBox = new VBox(alert, textFlow, userInputBox, accessBox, exitBt);

		// Prompt Text
		tfName.setPromptText("Your username");
		passwordField.setPromptText("Your password");

		// Set Color
		text1.setFill(Color.RED);
		text2.setFill(Color.BLUE);

		// Set Font
		text1.setFont(font);
		text2.setFont(font);

		// Set Textfield Sizes
		tfName.setPrefHeight(40);
		passwordField.setPrefHeight(40);

		// Set Images sizes
		exitImg.setFitHeight(40);
		exitImg.setFitWidth(40);
		loginImg.setFitHeight(40);
		loginImg.setFitWidth(40);
		regImg.setFitHeight(40);
		regImg.setFitWidth(60);

		// Setting Spacing
		accessBox.setSpacing(10);
		userInputBox.setSpacing(10);
		rootBox.setSpacing(10);

		// Set margin
		rootBox.setMargin(userInputBox, new Insets(10, 20, 50, 20));
		rootBox.setMargin(textFlow, new Insets(50, 20, 50, 20));

		// Set Padding
		rootBox.setPadding(new Insets(20, 20, 50, 20));
		exitBt.setPadding(new Insets(5, 10, 10, 5));
		logBt.setPadding(new Insets(5, 10, 10, 5));
		regBt.setPadding(new Insets(5, 10, 10, 5));

		// Alignment
		userInputBox.setAlignment(Pos.CENTER);
		accessBox.setAlignment(Pos.CENTER);
		rootBox.setAlignment(Pos.CENTER);

		// Styling
		textFlow.setStyle("-fx-text-alignment: center;");
		exitBt.setStyle(
				"-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-color: darkred;-fx-border-color:black;-fx-border-width: 3 3 3 3;");
		regBt.setStyle(
				"-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-color: darkgreen;-fx-border-color:black;-fx-border-width: 3 3 3 3;");
		logBt.setStyle(
				"-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-color: darkgreen;-fx-border-color:black;-fx-border-width: 3 3 3 3;");
		rootBox.setStyle("-fx-base: rgba(60, 60, 60, 255);");
		alert.setStyle(
				"-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold; -fx-border-color:black;-fx-border-width: 3 3 3 3;");

		// Setting the data input stream
		input = cli.getInput();

		// Setting alert notifications
		alert.setEditable(false);
		alert.setPrefSize(415, 50);
		alert.setText("Welcome to JavaChatty! The best Chat Room of 2019.");

		// EXIT
		exitBt.setOnAction(e -> exitHandler());

		// LOGIN
		logBt.setOnAction(e -> loginHandler(tfName, passwordField, alert, primaryStage));

		// REGISTER
		regBt.setOnAction(e -> registrationHandler(tfName, passwordField, alert, primaryStage));

		// Create a scene and place it in the stage
		Scene scene = new Scene(rootBox);
		primaryStage.setTitle("JavaChatty's Login"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	// Listen for server reply
	public void enableListener() throws IOException {
		serverMessage = (String) input.readUTF();
	}

	// Exit the program

	public void exitHandler() {
		String message = "END" + "#";
		cli.sendMessage(message);
		cli.closeConnection();
		System.exit(0);
	}

	// Login to JavaChatty
	public void loginHandler(TextField tfName, TextField tfPass, TextArea alert, Stage primaryStage) {
		String username = tfName.getText();
		String message = "LOGIN" + "#" + username + "#" + tfPass.getText();
		cli.sendMessage(message);

		try {
			enableListener();
			String[] serverCommand = serverMessage.split("#");
			String reply = serverCommand[0];

			if (reply.equals("Account Logged In.")) {
				primaryStage.hide();
				String friends;

				if (serverCommand.length < 2) {
					friends = "You don't have any friends yet";
				} else {
					friends = serverCommand[1];
				}

				ChatBox.display(primaryStage, cli, username, friends);
			} else if (reply.equals("Incorrect Email or Password.")) {
				alert.setText("Incorrect Email or Password.");
			} else if (reply.equals("Account Already Online.")) {
				alert.setText("Account Already Online.");
			} else if (reply.equals("Account Not Registered.")) {
				alert.setText("Account Not Registered.");
			} else {
				alert.setText(serverMessage);
			}
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	// Register to JavaChatty

	public void registrationHandler(TextField tfName, TextField tfPass, TextArea alert, Stage primaryStage) {
		String username = tfName.getText();
		String message = "REGISTER" + "#" + username + "#" + tfPass.getText();
		cli.sendMessage(message);

		try {
			enableListener();

			if (serverMessage.equals("SUCCESS")) {
				primaryStage.hide();

				ChatBox.display(primaryStage, cli, username, "You don't have any friends yet");
			} else if (serverMessage.equals("FAIL")) {
				alert.setText("Account Failed to Register. Account already exist.");
			} else {
				alert.setText(serverMessage);
			}
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	// Main
	public static void main(String[] args) {
		launch(args);
	}
}
