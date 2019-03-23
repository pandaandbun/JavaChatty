package Client;

import java.io.IOException;

import Client.Client;
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

public class ClientGUI extends Application {

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws IOException {
		
		TextField tfName = new TextField();
		TextField tfMsg = new TextField();
		TextField tffriend = new TextField();
		
		TextArea taBox = new TextArea();
		taBox.setEditable(false);
		
		Client cli = new Client (taBox);
		
		Button regBt = new Button("Register");
		Button loginBt = new Button("Login");
		Button sendBt = new Button("Send");
		Button addBt = new Button("Add Friend");
		Button friendBt = new Button("Get Friend");
		Button exitBt = new Button("Exit");
		
		Label msg = new Label("Password/Message: ");
		
		HBox hbUser = new HBox (new Label("User: "), tfName);
		HBox hbFriend = new HBox (new Label("Friend: "), tffriend);
		HBox hbMsg = new HBox (msg, tfMsg);
		
		HBox firstBox = new HBox(regBt, loginBt);
		HBox secBox = new HBox(addBt, friendBt);
		firstBox.setSpacing(10);
		secBox.setSpacing(10);


		VBox vb = new VBox();
		vb.getChildren().addAll(hbUser, hbFriend, hbMsg, taBox, firstBox, secBox, sendBt, exitBt);
		vb.setPadding(new Insets(20, 20, 50, 20));
		vb.setSpacing(10);
		
		sendBt.setOnAction(e -> {
			String message = "MESSAGE" + "#" + tfName.getText() + "#" + tffriend.getText() + "#" + tfMsg.getText();
			cli.sendMessage(message);
		});
		
		regBt.setOnAction(e -> {
			String message = "REGISTER" + "#" + tfName.getText() + "#" + tfMsg.getText();
			cli.sendMessage(message);
		});
		
		loginBt.setOnAction(e -> {
			String message = "LOGIN" + "#" + tfName.getText() + "#" + tfMsg.getText();
			cli.sendMessage(message);
		});
		
		addBt.setOnAction(e -> {
			String message = "ADDFRIEND" + "#" + tfName.getText() + "#" + tffriend.getText();
			cli.sendMessage(message);
		});
		
		friendBt.setOnAction(e -> {
			String message = "GETFRIEND" + "#" + tfName.getText();
			cli.sendMessage(message);
		});

		exitBt.setOnAction(e -> {
			String message = "END" + "#" + tfName.getText() + "#" + tfMsg.getText();
			cli.sendMessage(message);
			System.exit(0);
		});
		
		// Create a scene and place it in the stage
		Scene scene = new Scene(vb);
		primaryStage.setTitle("JavaChatty"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
