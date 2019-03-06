package GUI.Registration;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RegistrationController {

    @FXML AnchorPane root;
    @FXML TextField fullnameTextField;
    @FXML TextField emailTextField;
    @FXML PasswordField passwordField;
    @FXML Button registerBtn;

    public void initialize() {
        bindFields();
    }

    @FXML
    private void signUpBtnPressed(javafx.event.ActionEvent event) throws IOException {

        /**
         * SIGN UP HERE
         */

        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/GUI/Chat/ChatView.fxml"));
        root.getChildren().setAll(anchorPane);

        fullnameTextField.clear();
        emailTextField.clear();
        passwordField.clear();
    }

    @FXML
    private void backBtnPressed() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/GUI/Login/LoginPage.fxml"));
        root.getChildren().setAll(anchorPane);
    }

    void bindFields() {
        BooleanBinding booleanBind = fullnameTextField.textProperty().isEmpty()
                .or(emailTextField.textProperty().isEmpty())
                .or(passwordField.textProperty().isEmpty());
        registerBtn.disableProperty().bind(booleanBind);
    }

}
