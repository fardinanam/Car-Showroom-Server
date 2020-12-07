package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerManager extends Thread{
    private Socket socket;
    private int clientId;
    private BufferedReader inputFromClient;
    private PrintWriter outputToClient;

    public ServerManager(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
        System.out.println("Client " + clientId + " connected");
    }

    @Override
    public void run() {
        try {
            inputFromClient = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            outputToClient = new PrintWriter(
                    socket.getOutputStream(), true);
            // Starting message to client
            outputToClient.println("Server Connected");
            while (true) {
                String clientRequest = inputFromClient.readLine();
                System.out.println("Client " + clientId + " requests: " + clientRequest);
                String responseToClient = handleClientRequest(clientRequest);
                if(responseToClient.equals("exit")) {
                    outputToClient.println("exit");
                    System.out.println("Client " + clientId + " disconnected");
                    break;
                } else {
                    outputToClient.println(responseToClient);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    /**
     * @param clientRequest Comma separated request. First argument denotes the type of request
     */
    private String handleClientRequest(String clientRequest) {
        String[] requestMessages = new String[3];
        String response = "NULL";
        requestMessages = clientRequest.split(",");

        //TODO: handle requests from client with if else
        if(requestMessages[0].equals("LIN")) {
            response = handleLogin(requestMessages[1], requestMessages[2]);
        } else if(clientRequest.equals("exit")) {
            response = "exit";
        }
        return response;
    }

    // TODO: Handle login properly
    private String handleLogin(String username, String pass) {
        if(username.equals("admin") && pass.equals("123")) {
            return "LIN,login successful," + username;
        } else {
            return "LIN,Access Denied";
        }
    }
}
