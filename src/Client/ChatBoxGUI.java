package Client;

import java.io.IOException;

import Client.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

public class ChatBoxGUI {

	public void display(Stage root, Client cli, String username, String friends) throws IOException {
		Stage window = new Stage();

		// Images
		ImageView addFriendImg = new ImageView("Images/addfriend.png");
		ImageView exitImg = new ImageView("Images/exit.png");
		ImageView sendImg = new ImageView("Images/send.png");

		// Title
		TextFlow title = new TextFlow();
		Font font = new Font("Tahoma", 48);
		Text text1 = new Text("Java");
		Text text2 = new Text("Chatty.");

		// Friend List
		ListView<String> friendList = new ListView<>();

		// User input text fields
		TextField tfMsg = new TextField();
		TextField tffriend = new TextField();

		// Chat Display
		TextField usernameBox = new TextField(username);
		TextArea chatBox = new TextArea();

		// Buttons to send commands to the server
		Button sendBt = new Button("Send", sendImg);
		Button addBt = new Button("Add Friend", addFriendImg);
		Button exitBt = new Button("Logout", exitImg);

		// User input text fields
		VBox vbUser = new VBox(new Label("User: "), usernameBox);
		VBox vbFriend = new VBox(new Label("Add Friend: "), tffriend);
		VBox vbMsg = new VBox(new Label("Message: "), tfMsg);
		VBox vbFriendList = new VBox(new Label("Friend List: "), friendList);
		VBox vbChatBox = new VBox(new Label("Chat Box: "), chatBox);

		// Command Buttons
		HBox friendBox = new HBox(vbFriend, addBt);
		HBox messageBox = new HBox(vbMsg, sendBt);
		HBox userBox = new HBox(vbUser, exitBt);

		// Adding everything the pane
		VBox vb = new VBox();

		// Enable Client.java listener to update the chat box
		cli.setUpdatechat(chatBox);
		cli.setUpdateFriendList(friendList);

		// Enable Client.java to listen
		cli.startListener();

		// Set friend list
		if (friends.equals("You don't have any friends yet")) {
			friendList.getItems().addAll(friends);
		} else {
			String[] splittedFriends = friends.split(",");
			friendList.getItems().addAll(splittedFriends);
		}

		// Set Text Flow
		title.getChildren().addAll(text1, text2);
		
		// Set Color
		text1.setFill(Color.RED);
		text2.setFill(Color.BLUE);

		// Set Font
		text1.setFont(font);
		text2.setFont(font);

		// Prompt Text
		tfMsg.setPromptText("Your message");
		tffriend.setPromptText("Add your friend username here and press Add Friend to start talking to them.");
		chatBox.setPromptText("Welcome to JavaChatty!\nThe best Chat App of 2019. Here's how you use it:\r"
				+ "1. To message somone, you must first add them as a friend. Enter their username in the Friend's Text Field and press Add Friend.\r"
				+ "2. After adding them as friend, you can select their username in your Friend List and type in the message you wish to send them.\r"
				+ "3. Now that you have added a friend, select them as the receiver and type in your message. Press send so you can start chatting.\r"
				+ "4. Be warn, they may not be online.");
		
		// Set Images sizes
		addFriendImg.setFitHeight(40);
		addFriendImg.setFitWidth(40);
		exitImg.setFitHeight(40);
		exitImg.setFitWidth(40);
		sendImg.setFitHeight(40);
		sendImg.setFitWidth(40);

		// Set size
		tfMsg.setPrefColumnCount(40);
		tffriend.setPrefColumnCount(40);
		usernameBox.setPrefColumnCount(20);
		friendList.setPrefSize(120, 120);

		// Set TextField Width
		tfMsg.setPrefHeight(40);
		tffriend.setPrefHeight(40);
		usernameBox.setPrefHeight(40);

		// Set user to not be able to edit box
		usernameBox.setEditable(false);
		chatBox.setEditable(false);

		// Set Spacing
		friendBox.setSpacing(200);
		messageBox.setSpacing(200);
		userBox.setSpacing(415);
		vb.setSpacing(10);

		// Add Everything to the VBox
		vb.getChildren().addAll(title, userBox, vbFriendList, vbChatBox, friendBox, messageBox);

		// Set Padding
		vb.setPadding(new Insets(20, 20, 50, 20));

		// Set Styling
		vb.setStyle("-fx-base: rgba(60, 60, 60, 255);");
		exitBt.setStyle(
				"-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-color: darkred;-fx-border-color:black;-fx-border-width: 3 3 3 3;");
		sendBt.setStyle(
				"-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-color: darkgreen;-fx-border-color:black;-fx-border-width: 3 3 3 3;");
		addBt.setStyle(
				"-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;-fx-background-color: darkgreen;-fx-border-color:black;-fx-border-width: 3 3 3 3;");

		/* Action handler */
		// Send a message to the server
		sendBt.setOnAction(e -> sendMessage(friendList, usernameBox, tfMsg, cli));

		// Adding another user to friend list
		addBt.setOnAction(e -> addFriend(usernameBox, tffriend, cli));

		// Logging out
		exitBt.setOnAction(e -> logOut(window, root, usernameBox, cli));

		// Set scene
		window.setTitle("JavaChatty");
		Scene scene = new Scene(vb);
		window.setScene(scene);
		window.showAndWait();

	}

	public void sendMessage(ListView<String> friendList, TextField usernameBox, TextField tfMsg, Client cli) {
		String friendEmail = friendList.getSelectionModel().getSelectedItem();
		String message = "MESSAGE" + "#" + usernameBox.getText() + "#" + friendEmail + "#" + tfMsg.getText();

		cli.sendMessage(message);
	}

	public void addFriend(TextField usernameBox, TextField tffriend, Client cli) {
		String message = "ADDFRIEND" + "#" + usernameBox.getText() + "#" + tffriend.getText();

		cli.sendMessage(message);
	}

	public void logOut(Stage window, Stage root, TextField usernameBox, Client cli) {
		window.hide();
		String message = "LOGOUT" + "#" + usernameBox.getText();
		root.show();

		cli.sendMessage(message);
	}
}
