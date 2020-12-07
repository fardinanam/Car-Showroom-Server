package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static final int PORT = 12121;

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is online. Waiting for clients to connect...");
            for(int i = 1; true; i++) {
                new ServerManager(serverSocket.accept(), i).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
