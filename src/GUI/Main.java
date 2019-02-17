package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane border = new BorderPane();
        border.setPrefSize(800, 400);

        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

        primaryStage.setTitle("Project");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("/GUI/style/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
