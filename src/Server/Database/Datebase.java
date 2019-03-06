package Server.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Datebase{
  static String id = "";
  static String password = "";
  HashMap<String,String> record = new HashMap<String,String>(); //store username and password in a hashmap

  private void writeRecord(String id, String password){
    try{
      File file = new File("C:/Users/97528/Desktop/csci2020final project/record.txt");                      //file location
      FileOutputStream fos = new FileOutputStream(file, true);      //outputfile
      String id_password = "*|*" + id + "-" + password;             //id and password
      fos.write(id_password.getBytes());
      fos.flush();                                                  //clean buffer force output
			fos.close();                                                  //close file
		} catch (IOException e) {
			System.out.println("error in writting record ...");              //error output
		}
	}


  private void readRecord(){
    byte[] fileRecord = null;
    File file = null;
    FileInputStream fis = null;
    try{
      file = new File("C:/Users/97528/Desktop/csci2020final project/record.txt");
      fis = new FileInputStream(file);
      fileRecord = new byte[(int)file.length()];
      fis.read(fileRecord);
    } catch(IOException e){
      System.out.println("error in loading record ...");
    }
    String[] recordStr = new String(fileRecord).split("\\*\\|\\*");
    for(int i = 0; i < recordStr.length; i++){
      String[] iap = recordStr[i].split("-");
      record.put(iap[0],iap[1]);
    }
    try{
      fis.close();
    } catch (IOException e){
      System.out.println("error in closing readfile");
    }
  }

  private boolean verifyInfo(String username){
    for ( String key : record.keySet()) {  //iter through the record
      if(key == username){                 //check if the username are matched
        String pwd = record.get(key);      //get password match to the username
        if (password == pwd){              //verify the password
          return true;
        }
      }
    }
    return false;
  }
}
