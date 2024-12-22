package Serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Database.DB;

public class Serialize {
    public static void serialize(String path, DB db) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(db);
            System.out.println("Objet sérialisé dans : " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void verifyAndCreatefile(String path) throws Exception {
        File fichier = new File(path);
        if(!fichier.exists()) {
            if(fichier.createNewFile()) {
            } else {
                throw new Exception("Custom Error: file not found and could not be created here: " + path);                
            }
        }
    }

    public static DB deserialize(String path) throws Exception {
        verifyAndCreatefile(path);
         try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            DB result = (DB) ois.readObject();
            System.out.println("Objet désérialisé : ");
            return result;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
