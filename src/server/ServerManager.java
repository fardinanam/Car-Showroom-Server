package server;

import fileHandleClasses.CarsList;
import fileHandleClasses.UsersList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

public class ServerManager extends Thread{
    private final Socket socket;
    private final int clientId;
    private BufferedReader inputFromClient;
    private PrintWriter outputToClient;
    private final HashSet<ServerManager> clients;

    public ServerManager(Socket socket, int clientId, HashSet<ServerManager> clients) {
        this.socket = socket;
        this.clientId = clientId;
        this.clients = clients;
        System.out.println("Client " + clientId + " connected");
        try {
            inputFromClient = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            outputToClient = new PrintWriter(
                    socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // Starting message to client
            outputToClient.println("Server Connected");
            sendCarsToClient();
            while (true) {
                String clientRequest = inputFromClient.readLine();
                System.out.println("Client " + clientId + " requests: " + clientRequest);
                String responseToClient = handleClientRequest(clientRequest);
                outputToClient.println(responseToClient);

                if(responseToClient.equals("exit")) {
                    System.out.println("Client " + clientId + " disconnected");
                    clients.remove(this);
                    break;
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
        } else if (requestMessages[0].equals("ADD")) {
            response = handleAddCar(clientRequest.substring(4));
        } else if(requestMessages[0].equals("DLT")) {
            response = handleDeleteCar(requestMessages[1]);
        }
        return response;
    }

    private synchronized String handleDeleteCar(String requestMessage) {
        CarsList.getInstance().deleteCar(requestMessage);
        for(ServerManager client : clients) {
            client.sendDeleteCarResponse(requestMessage);
        }
        return "Car with Reg No. " + requestMessage + " has been deleted";
    }

    private synchronized String handleAddCar(String requestMessage) {
        String reg = requestMessage.split(",")[0];
        if(!CarsList.getInstance().contains(reg)) {
            CarsList.getInstance().addNewCarToFile(requestMessage);
            // Sending new Car info to all clients
            for(ServerManager client : clients) {
                client.sendCarToClient(requestMessage);
            }
            return "Your car has been added";
        } else {
            return "Can not add your car. (A car with registration No. " + reg +
                    " already exists)";
        }

    }

    private String handleLogin(String username, String pass) {
        List<String> users = UsersList.getInstance().getUsers();

        for(String user : users) {
            if(user.equals(username + "," + pass)) {
                return "LIN,login successful," + username;
            }
        }
       /*
        if("admin,123".equals(username + "," + pass)) {
        return "LIN,login successful," + username;
        }*/
        return "LIN,Access Denied";
    }

    public void sendDeleteCarResponse(String reg) {
        outputToClient.println("DLT," + reg);
    }

    public void sendCarToClient(String car) {
        outputToClient.println("car," + car);
    }
    private void sendCarsToClient() {
        List<String> cars = CarsList.getInstance().getCars();

        for(String car : cars) {
            sendCarToClient(car);
        }
    }
}
