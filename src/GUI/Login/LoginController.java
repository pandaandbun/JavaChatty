package GUI.Login;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class LoginController {
    @FXML private AnchorPane root;
    @FXML private Button signUpBtn;
    @FXML private Button loginBtn;
    @FXML private TextField userNameTxtField;
    @FXML private PasswordField passwordField;



    public void initialize() {
        bindFields();
        DropShadow shadow = new DropShadow();
        loginBtn.setEffect(shadow);
    }

    @FXML
    private void registrationBtnClicked(javafx.event.ActionEvent event) throws IOException {

        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/GUI/Registration/Registration.fxml"));
        root.getChildren().setAll(anchorPane);
    }

    @FXML private void loginBtnClicked(javafx.event.ActionEvent event) throws IOException {


        /**
         *  Check login crendentials here. If it's wrong, uncomment below code and don't proceed page transition
         */

        // Wrong username-password pair:
        /*Alert alert = new Alert(Alert.AlertType.ERROR, "Username or password is incorrect!");
        alert.showAndWait();*/

        System.out.println("Username: " + userNameTxtField.getText());
        System.out.println("Password: " + passwordField.getText());

        // Segue to chat screen
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/GUI/Chat/ChatView.fxml"));
        root.getChildren().setAll(anchorPane);

        userNameTxtField.clear();
        passwordField.clear();



    }

    void bindFields() {
        BooleanBinding booleanBind = userNameTxtField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty());
        loginBtn.disableProperty().bind(booleanBind);
    }

}
