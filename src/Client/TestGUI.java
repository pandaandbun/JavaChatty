package Client;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestGUI {
	public static void display() {
		Stage window = new Stage();
		Pane rootBJ = new Pane();

		Button regBt = new Button("Register");
		Button loginBt = new Button("Login");
		Button sendBt = new Button("Send");
		Button addBt = new Button("Add Friend");
		Button friendBt = new Button("Get Friend");
		Button exitBt = new Button("Exit");

		window.setTitle("Black Jack");
		rootBJ.getChildren().addAll(sendBt, addBt, friendBt, exitBt);

		Scene scene = new Scene(rootBJ);
		window.setScene(scene);
		window.showAndWait();
	}
}
