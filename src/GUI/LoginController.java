package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class LoginController {
    @FXML
    private Button signUpBtn;

    @FXML
    private TextField userNameTxtField;


    @FXML
    private AnchorPane root;

    public void initialize() {
        //signUpBtn.setUnderline(true);
        //signUpBtn.setStyle("-fx-background-color: transparent;");
        //rememberMeBtn.setUnderline(true);
        //rememberMeBtn.setStyle("-fx-background-color: transparent;");


    }

    @FXML
    private void registrationBtnClicked(javafx.event.ActionEvent event) throws IOException {

        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        root.getChildren().setAll(anchorPane);
    }

}
