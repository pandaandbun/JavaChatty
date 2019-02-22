package GUI.Chat;

import GUI.Model.User;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 * A class for rendering users name on userlist.
 */
class FriendCellRenderer implements Callback<ListView<User>,ListCell<User>>{
    @Override
    public ListCell<User> call(ListView<User> p) {

        ListCell<User> cell = new ListCell<>(){

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();

                    Label name = new Label(user.getFullName());
                    name.setStyle("-fx-text-fill: white ;");
                    name.setFont(new Font("Avenir Next Regular", 16));
                    hBox.getChildren().add(name);
                    hBox.setAlignment(Pos.CENTER);

                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}