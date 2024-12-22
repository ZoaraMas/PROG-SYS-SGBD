package ProgSys;

import java.io.*;
import java.net.*;

public class Server {
    public static void startServer() {
        int port = 12345; // Port number the server will listen on
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            // Input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Communication loop
            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                if ("exit".equalsIgnoreCase(clientMessage)) {
                    System.out.println("Client disconnected");
                    break;
                }
                output.println("Server: " + clientMessage.toUpperCase());
            }
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("Server error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
