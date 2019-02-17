package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RegistrationController {

    @FXML
    AnchorPane root;

    @FXML
    TextField fullnameTextField;

    @FXML
    TextField emailTextField;

    @FXML
    PasswordField passwordField;

    @FXML
    private void signUpBtnPressed(javafx.event.ActionEvent event) {
        if (checkFields()) {


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "At least one field is empty!");
            alert.showAndWait();
        }

    }

    @FXML
    private void backBtnPressed(javafx.event.ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        root.getChildren().setAll(anchorPane);
    }

    boolean checkFields() {
        if (fullnameTextField.getText() == null || fullnameTextField.getText().trim().isEmpty() ||
                emailTextField.getText() == null || emailTextField.getText().trim().isEmpty() || passwordField.getText() == "" ||
                passwordField.getText().trim().isEmpty()) {
            return false;
        }

        return true;
    }

}
