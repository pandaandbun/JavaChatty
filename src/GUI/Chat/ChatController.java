package GUI.Chat;

import GUI.Model.User;
import GUI.Util.MessageType;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import GUI.Model.Message;
import javafx.scene.text.Font;

public class ChatController {

    @FXML private TextArea messageBox;
    @FXML private ListView chatListView;
    @FXML private ListView friendsList;
    @FXML private Button addFriendBtn;
    @FXML private Button buttonSend;

    @FXML
    private void sendMessage(javafx.event.ActionEvent event) throws IOException {

        /**
         * DELETE HERE
         */
        addToChat(new Message("useremail@com", "UserFullName",
                messageBox.getText()), MessageType.RIGHT);
        addToChat(new Message("sadas@asdjkasd", "John Doe",
                messageBox.getText()), MessageType.LEFT);

        messageBox.clear();

    }

    public void initialize() {

        bindSendButton();
        setStyles();


        /**
         *  CHANGE HERE
         *  This is a demonstration of how to add a user and its friends
         *  Load user's friend list from database
         */

        User u1 = new User("Dutch", "saasdd", null);
        User u2 = new User("John", "sadasd", null);
        User u3 = new User("Arthur", "saasdasd", null);

        ArrayList<User> friends = new ArrayList<User>();
        friends.add(u1);
        friends.add(u2);
        friends.add(u3);

        User user = new User("Johnny", "sadsad@sadas.com", friends);
        setUserList(user);



        friendsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {

            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                /**
                  * USER SELECTED A FRIEND FROM LIST -> UPDATE CHAT HERE
                  * Load message history from file
                 */
                System.out.println("Selected item: " + newValue.getFullName());
                /**
                 *  HOW TO DISPLAY MESSAGES ON CHAT:
                 *  call addToChat(message, type)
                 *  type: message is from you or your friend -> right for you, left for the friend
                 *  example: addToChat(new Message("sadas@asdjkasd", "John Doe",
                 *                 "sadaslkmd asldmas dlksamdasm  dlasmdas laskmd"), BubbleType.LEFT);
                 */
            }
        });
    }


    public void addToChat(Message msg, MessageType type) {

        Label bbleMessage = new Label();
        if(type == MessageType.LEFT) {
            bbleMessage.setText(msg.getFullname() + ": " + msg.getMessage());
        } else {
            bbleMessage.setText(msg.getMessage());
        }

        HBox hbox = new HBox();


        //hbox.setMaxWidth(130);
        if(type == MessageType.LEFT) {
            bbleMessage.setPadding(new Insets(0,0,0,10));
            bbleMessage.setBackground(new Background(new BackgroundFill(Color.rgb(229,228,234),
                   new CornerRadii(7), new Insets(-3, -4, -3, 6))));
            hbox.setPadding(new Insets(0, 0, 0, 5));
            hbox.setAlignment(Pos.CENTER_LEFT);
        } else {
            bbleMessage.setPadding(new Insets(0,10,0,0));
            bbleMessage.setBackground(new Background(new BackgroundFill(Color.rgb(25,120,248),
                    new CornerRadii(7), new Insets(-3, 6, -3, -4))));

            bbleMessage.setStyle("-fx-text-fill: white ;") ;
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPadding(new Insets(0, 5, 0, 0));

        }

        hbox.getChildren().add(bbleMessage);

        chatListView.getItems().add(hbox);

    }

    @FXML
    private void addFriendPressed(javafx.event.ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(300);
        dialog.setTitle("Add friend");
        dialog.setHeaderText("Enter email:");
        dialog.setContentText("Email: ");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            /**
             * SEND INVITATION EMAIL HERE
             */
            System.out.println("Email: " + name);
        });
    }

    private void setUserList(User user) {
        Platform.runLater(() -> {
            ObservableList<User> users = FXCollections.observableList(user.getFriends());
            friendsList.setItems(users);
            friendsList.setCellFactory(new FriendCellRenderer());
        });
    }

    private void setStyles() {

        buttonSend.setBackground(new Background(new BackgroundFill(Color.rgb(52, 181, 117), null, null)));
        buttonSend.setFont(new Font("Avenir Next Bold", 14));
        buttonSend.setStyle("-fx-text-fill: white ;");

        messageBox.setBackground(new Background(new BackgroundFill(Color.rgb(77, 89, 122), null, null)));
        messageBox.setStyle("-fx-text-fill: white; ");

        DropShadow shadow = new DropShadow();
        addFriendBtn.setEffect(shadow);
        chatListView.setBackground(new Background(new BackgroundFill(Color.rgb(77, 89, 122), null, null)));

        addFriendBtn.setBackground(new Background(new BackgroundFill(Color.rgb(49, 112, 214), null, null)));
        addFriendBtn.setFont(new Font("Avenir Next Bold", 16));
        addFriendBtn.setStyle("-fx-text-fill: white ;");
    }

    private void bindSendButton() {
        BooleanBinding booleanBind = messageBox.textProperty().isEmpty();
        buttonSend.disableProperty().bind(booleanBind);
    }


}
