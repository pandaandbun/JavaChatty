import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.File.*;
import DB.User;

public class createProfile {

    public static void main(String args[]) {

        createProfile objectIO = new createProfile();

        User user = new User("9752@qq.com","zla");//should get information by here
        objectIO.WriteObjectToFile(user.getemail(),user);
    }

    public static boolean checkexist(String email){
        File tmp = new File(email);
        boolean exist = tmp.exists();
        return exist;
    }
    public static boolean WriteObjectToFile(String fileName, User user) {

        try {
            if (checkexist(fileName) == true){
                System.out.println("user data already exist");
                return false;
            }
            File f = new File(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(user);
            oos.flush();
            oos.close();
            System.out.println("The Object  was succesfully written to a file");
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
