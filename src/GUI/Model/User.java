package GUI.Model;

import java.util.ArrayList;

public class User {
    private String fullName;
    private String email;
    private ArrayList<User> friends;

    public User(String fullName, String email, ArrayList<User> friends) {
        this.fullName = fullName;
        this.email = email;
        this.friends = friends;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public ArrayList<User> getFriends () {
        return friends;
    }
}
