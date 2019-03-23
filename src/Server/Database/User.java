package Server.Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    //default serialVersion id
    private static final long serialVersionUID = 1L;


    private String email;
    private String password;
    private List<String> friendList = new ArrayList<>();


    public String getemail(){
        return email;
    }

    public String getpassword(){
        return password;
    }
    
    public User(String email, String password){
        this.email      = email;
        this.password   = password;
    }

    public void addFriend(String friendEmail){
        if (checkFriendExist(friendEmail) == true){
            System.out.println("friend already exists");
            return;
        }
        friendList.add(friendEmail);
    }

    public boolean checkFriendExist(String friendEmail){
        for (int i = 0; i < friendList.size(); i++){
            if (friendEmail.equals(friendList.get(i))){
                return true;
            }
        }
        return false;
    }

    public List<String> getFriendList(){
        return this.friendList;
    }


}
