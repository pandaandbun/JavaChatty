import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.File.*;
import java.util.ArrayList;
import java.util.List;
import DB.User;

public class friend{
    public static List<String> getFriendList(String ClientEmail){
        User user = readObjectFile(ClientEmail);
        return user.getFriendList();
    }

    public static void main(String[] args) {
        User user = readObjectFile("975284274@qq.com"); // thisobejct file contain 3 friend 1,2,3;
        addFriend("975284274@qq.com","4");
        List<String> fl = getFriendList("975284274@qq.com");
        for (int i = 0; i < fl.size();i++){
            System.out.println(fl.get(i));
        }
    }

    public static String getname(String clientEmail){
        User user = readObjectFile(clientEmail);
        return user.getname();
    }

    public static boolean addFriend(String clientEmail, String friendEmail){
        User user = readObjectFile(clientEmail);
        user.addFriend(friendEmail);
        createProfile objectIO = new createProfile();
        objectIO.WriteObjectToFile(user.getemail(),user,false);
        return true;
    }

    public static User readObjectFile(String fileName){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            User user = (User)ois.readObject();
            ois.close();
            System.out.println("The Object  was succesfully read");
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
            User user = new User("error","error");
            return user;
        }
    }

    public static boolean createProfile(String email, String password, String name){
        createProfile objectIO = new createProfile();

        User user = new User(email,password,name);//should get information by here
        objectIO.WriteObjectToFile(user.getemail(),user, true);
        if (checkProfile(user.getemail()) == true){
            return true;
        }
        return false;
    }

    public static boolean checkProfile(String email){
        File tmp = new File(email);
        boolean exist = tmp.exists();
        return exist;
    }


    public static void WriteObjectToFile(String fileName, User user, boolean indicator) {

        try {
            if ((checkProfile(fileName) == true) && (indicator == true)){
                System.out.println("user data already exist");
                return;
            }
            File f = new File(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(user);
            oos.flush();
            oos.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
