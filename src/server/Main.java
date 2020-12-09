package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;

public class Main {
    private static final int PORT = 12121;
    private static HashSet<ServerManager> clients;

    public static void main(String[] args) {
        clients = new HashSet<>();
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is online. Waiting for clients to connect...");
            for(int i = 1; true; i++) {
                ServerManager manager = new ServerManager(serverSocket.accept(), i, clients);
                clients.add(manager);
                manager.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
