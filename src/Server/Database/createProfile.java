package Server.Database;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import Server.Database.User;


public class createProfile {

    public static boolean createProfile(String email, String password){
        createProfile objectIO = new createProfile();

        User user = new User(email,password);//should get information by here
        objectIO.WriteObjectToFile(user.getemail(),user);
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

    public static void WriteObjectToFile(String fileName, User user) {

        try {
            if (checkProfile(fileName) == true){
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
