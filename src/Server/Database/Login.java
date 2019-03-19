import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.File.*;
import DB.User;

public class Login{
    public boolean CheckLogin(String email, String password){
        if (checkexist(email) == true){
            User user = readObjectFile(email);
            if (user.getpassword().equals(password)){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static boolean checkexist(String email){
        File tmp = new File(email);
        boolean exist = tmp.exists();
        return exist;
    }

    public static User readObjectFile(String fileName){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            User user = (User)ois.readObject();
            ois.close();
            System.out.println("The Object  was succesfully writen into object");
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
            User user = new User("error","error");
            return user;
        }
    }
}
