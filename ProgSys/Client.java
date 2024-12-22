package ProgSys;

import java.io.*;
import java.net.*;
import java.util.HashSet;

public class Client {
    private String type;
    private String host;
    private int port;
    private Socket socket;
    private ObjectInputStream inStream;
    private PrintWriter output;
    private BufferedReader userInput;

    public Client(String host, int port, String type) {
        setType(type);
        setHost(host);
        setPort(port);
    }

    public void close() {
        sendQuery("exit");
    }

    public Object recvPacket() throws ClassNotFoundException, IOException {
        if (socket.isClosed()) {
            System.out.println("client closed");
            throw new IOException("Socket is closed.");
        }
        Object recvPacket = null;
        while (recvPacket == null)
            recvPacket = this.inStream.readObject();
        return recvPacket;
    }

    public void sendQuery(String query) {
        if ("exit".equalsIgnoreCase(query)) {
            System.out.println("Disconnected from server");
        }
        // Send the message to the server
        output.println(query);
    }

    public void startClient() throws InterruptedException {
        try {
            this.socket = new Socket(this.host, this.port);
            System.out.println("Connected to the server");
            // Input and output streams
            setInStream(new ObjectInputStream(socket.getInputStream())); // Get the package object from
            // the server
            setOutput(new PrintWriter(socket.getOutputStream(), true));
            setUserInput(new BufferedReader(new InputStreamReader(this.socket.getInputStream())));

            // Write a message to the server to inform the type of client: simple or
            // replication
            Thread.sleep(200);
            this.output.write(this.type + "\n");
            this.output.flush();
            // System.out.println("type sended");

            while (true) {
                String messageFromServer = this.userInput.readLine();
                if (messageFromServer != null) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        }
        // if(!this.socket.isClosed()) System.out.println("THIS IS CLOSED 2");
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectInputStream getInStream() {
        return inStream;
    }

    public void setInStream(ObjectInputStream inStream) {
        this.inStream = inStream;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    public BufferedReader getUserInput() {
        return userInput;
    }

    public void setUserInput(BufferedReader userInput) {
        this.userInput = userInput;
    }

    public void setType(String type) {
        this.type = type;
    }

}
