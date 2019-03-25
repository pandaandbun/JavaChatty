package Client;

import java.io.IOException;

import Client.Client;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatBoxGUI {

	public void display(Stage root, Client cli, String username) throws IOException {
		Stage window = new Stage();

		// User input text fields
		TextField tfMsg = new TextField();
		TextField tffriend = new TextField();

		// Chat Display
		TextField usernameBox = new TextField(username);
		TextArea taBox = new TextArea();
		usernameBox.setEditable(false);
		taBox.setEditable(false);
		
		// Enable Client.java listener to update the chat box
		cli.setUpdatechat(taBox);
		// Enable Client.java to listen 
		cli.startListener();

		// Buttons to send commands to the server
		Button sendBt = new Button("Send");
		Button addBt = new Button("Add Friend");
		Button friendBt = new Button("Get Friend");
		Button exitBt = new Button("Logout");

		// User input text fields
		HBox hbUser = new HBox(new Label("User: "), usernameBox);
		HBox hbFriend = new HBox(new Label("Friend: "), tffriend);
		HBox hbMsg = new HBox(new Label("Message: "), tfMsg);

		// Command Buttons
		HBox firstBox = new HBox(addBt, friendBt);
		firstBox.setSpacing(10);

		// Adding everything the pane
		VBox vb = new VBox();
		vb.getChildren().addAll(hbUser, hbFriend, hbMsg, taBox, firstBox, sendBt, exitBt);
		vb.setPadding(new Insets(20, 20, 50, 20));
		vb.setSpacing(10);
		vb.setStyle("-fx-base: rgba(60, 60, 60, 255);");

		// Send a message to the server
		sendBt.setOnAction(e -> {
			String message = "MESSAGE" + "#" + usernameBox.getText() + "#" + tffriend.getText() + "#" + tfMsg.getText();
			cli.sendMessage(message);
		});

		// Adding another user to friend list
		addBt.setOnAction(e -> {
			String message = "ADDFRIEND" + "#" + usernameBox.getText() + "#" + tffriend.getText();
			cli.sendMessage(message);
		});

		// Getting the friend list of the user
		friendBt.setOnAction(e -> {
			String message = "GETFRIEND" + "#" + usernameBox.getText();
			cli.sendMessage(message);
		});
		
		// Logging out
		exitBt.setOnAction(e -> {
			window.hide();
			String message = "LOGOUT" + "#" + usernameBox.getText();
			root.show();
			cli.sendMessage(message);
		});
		
		window.setTitle("JavaChatty");
		Scene scene = new Scene(vb);
		window.setScene(scene);
		window.showAndWait();

	}
}
