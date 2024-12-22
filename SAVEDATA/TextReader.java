package SAVEDATA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextReader {
    // Get txt file's data that are each separated by semicolons and going to the next line 
    public ArrayList<String[]> getFileData (String fileName) throws FileNotFoundException, IOException {
        ArrayList<String[]> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split("=");
                result.add(data);
            }
        }
        return result;
    }
}
