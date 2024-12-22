package ThreadMatters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import Database.DB;
import Database.Relation;
import ProgSys.Message;
import ProgSys.MyPacket;
import ProgSys.Parse;
import SAVEDATA.TextReader;
import Serialization.Serialize;

import java.lang.Thread;

public class MyThread extends Thread {
    private volatile boolean running = true;
    private String name;

    // PROG SYS
    private DB db;
    private DB dbRep;
    private Socket client;
    private ObjectOutputStream outStream;
    private BufferedReader input;
    private PrintWriter output;

    // type of the usage
    private boolean rep;

    // public MyThread(MyProcess myProcess) {
    // super(myProcess);
    // }

    public MyThread(String name, Socket client, DB db, DB dbRep) throws Exception {
        this.setName(name);
        this.setClient(client);
        this.outStream = new ObjectOutputStream(this.client.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.output = new PrintWriter(this.client.getOutputStream(), true);
        this.setDb(db);
        this.setDbRep(dbRep);

        System.out.println("waiting for the client type");
        String clientType = this.input.readLine();
        System.out.println("Client type: " + clientType + " \n ");
        if (clientType.equals("rep"))
            this.rep = true;
        else
            this.rep = false;

        // unblock the client
        this.output.write("continue" + "\n");
        this.output.flush();
    }

    public void customStop() {
        this.running = false;
    }

    public void run() {
        while (running) {
            try {
                logic();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logic() throws IOException {
        // Communication loop
        String clientMessage;
        while ((clientMessage = input.readLine()) != null) {
            System.out.println("Client: " + clientMessage);
            if ("exit".equalsIgnoreCase(clientMessage)) {
                System.out.println("Client disconnected");
                break;
            }
            try {
                if (clientMessage.equals("show tables")) {
                    String tablesList = new String();
                    for (int i = 0; i < db.getListeRelation().size(); i++) 
                        tablesList += "" + db.getListeRelation().get(i).getName() + " | ";
                    MyPacket rPacket = new MyPacket(tablesList);
                    outStream.writeObject(rPacket);
                } else if (clientMessage.split(" ")[0].equals("drop")) {
                    if(!this.rep)
                        this.db.drop(clientMessage.split(" ")[1]);
                } else {
                    String[] parseMessage = Parse.parseIt(clientMessage);
                    String operation = "" + parseMessage[0].charAt(0);
                    if (operation.equals("1")) {
                        Relation temp = Message.select(parseMessage, db, dbRep, this.rep);
                        if (temp != null)
                            temp.display();
                        else
                            throw new Exception("The relation is null");
                        outStream.writeObject(temp);
                    } else if (operation.equals("2")) {
                        int insert = Message.insert(parseMessage, db, dbRep, this.rep);
                        outStream.writeObject(new MyPacket("Inserted successfully with code " + insert));
                    } else if (operation.equals("3")) {
                        int create = Message.createTable(parseMessage, db, dbRep, this.rep);
                        // Relation ttemp = db.getRelationByName("temp");

                        outStream.writeObject(new MyPacket("Created successfully with code " + create));
                    }
                }
                Serialize.serialize(getPath(false), db);
                Serialize.serialize(getPath(true), dbRep);
            } catch (Exception e) {
                System.out.println("Server: " + e.getMessage());
                outStream.writeObject(
                        new MyPacket("\nComment: "
                                + e.getMessage()));
            }
        }
        try {
            if (client != null && !client.isClosed()) {
                client.close();
                System.out.println("Client socket closed.");
            }
        } catch (IOException e) {
            System.err.println("Error closing sockets: " + e.getMessage());
        }
        this.customStop();
    }

    public static String getPath(boolean rep) throws FileNotFoundException, IOException {
        TextReader textReader = new TextReader();
        ArrayList<String[]> data = textReader.getFileData("Conf.txt");
        String path = data.get(1)[1];
        if (rep)
            path = data.get(3)[1];
        return path;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public ObjectOutputStream getOutStream() {
        return outStream;
    }

    public void setOutStream(ObjectOutputStream outStream) {
        this.outStream = outStream;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    public DB getDbRep() {
        return dbRep;
    }

    public void setDbRep(DB dbRep) {
        this.dbRep = dbRep;
    }

}