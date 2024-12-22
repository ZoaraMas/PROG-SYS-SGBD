package ProgSys;

import java.io.*;
import java.net.*;
import java.util.HashSet;

public class ClientAncien {
    private String host;
    private int port;
    private Socket socket;
    private ObjectInputStream inStream;
    private PrintWriter output;
    private BufferedReader userInput;
    public static void main(String[] args) throws ClassNotFoundException {
        String host = "localhost"; // Server address
        int port = 12345; // Server port

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to the server");

            // Input and output streams
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream()); // Get the package object from
                                                                                         // the server

            // BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Communication loop
            String userMessage;
            while (true) {
                System.out.print("You: ");
                userMessage = userInput.readLine();
                if ("exit".equalsIgnoreCase(userMessage)) {
                    System.out.println("Disconnected from server");
                    break;
                }

                // Send the message to the server
                output.println(userMessage);

                // The server makes the process and then send the message object
                // Sends a confirmation message first
                // String rcvMessage = input.readLine();
                // System.out.println("rcv = " + rcvMessage);
                MyPacket recvPacket = null;
                while(recvPacket == null) recvPacket = (MyPacket) inStream.readObject();
                // Afficher le resultat de la requete
                System.out.println(recvPacket.getMessage());
            }
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
