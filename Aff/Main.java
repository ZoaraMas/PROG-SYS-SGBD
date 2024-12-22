package Aff;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Database.DB;
import Database.Relation;
import ProgSys.Message;
import ProgSys.Parse;
import SAVEDATA.TextReader;
import Serialization.Serialize;
import ThreadMatters.MyThread;

public class Main {
    // Prog Sys
    public static void main(String[] args) throws Exception {
        /// Serveur
        System.out.println("DESERIALIZATION");
        DB db = null;
        db = Serialize.deserialize(getPath(false));
        DB dbRep = null;
        dbRep = Serialize.deserialize(getPath(true));

        ///
        String create = new String("create table temp ( int a, varchar b )");
        String[] arr = Parse.parseIt(create);
        System.out.println("grille " + arr[0]);
        Message.createTable(arr, db, dbRep, false);
        System.out.println("table cree");
        Relation tmp = db.getRelationByName("temp");
        tmp.display();
        // PROG SYS ////
        System.out.println("Prog Sys");
        System.out.println("====================================");
        int port = Main.getPort(); // Port number the server will listen on
        ServerSocket serverSocket = null;
        // try {
        serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);
        int count = 0; // used for the number of client connected till the beginning
        while (true) {
            System.out.println("waiting for client to connect");
            Socket socket = serverSocket.accept();
            // Waiting for the first message to know if the client want to access to the
            // simple or replication database
            count++;
            MyThread t = new MyThread("Client " + (count - 1), socket, db, dbRep);
            t.start();
            System.out.println("Client " + (count - 1) + " connected.");
        }
    }

    public static String getPath(boolean rep) throws FileNotFoundException, IOException {
        TextReader textReader = new TextReader();
        ArrayList<String[]> data = textReader.getFileData("Conf.txt");
        String path = data.get(1)[1];
        if (rep)
            path = data.get(3)[1];
        return path;
    }

    public static int getPort() throws FileNotFoundException, IOException {
        TextReader textReader = new TextReader();
        ArrayList<String[]> data = textReader.getFileData("Conf.txt");
        String port = data.get(0)[1];
        return Integer.parseInt(port);
    }

}
