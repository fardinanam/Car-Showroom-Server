package server;

import car.Car;
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
     * Format - For login request: "LIN(login),Username,Pass"
     *          For Add request: "ADD,(Comma separated car information)"
     *          For Delete request: "DLT,Registration number of the car"
     *          For Buy request: "BUY,(Comma separated car information)"
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
        } else if(requestMessages[0].equals("BUY")) {
            response = handleBuyCar(clientRequest.substring(4));
        }
        return response;
    }

    /**
     * Checks if the car is available in stock. If available, deletes older data
     * and adds a new car with same information as old data except the quantity.
     * Quantity is reduces by 1
     * @return "Out of stock" or successful buy message
     */
    private String handleBuyCar(String carInfo) {
        Car car = new Car(carInfo);

        int quantity = car.getQuantity();
        if(quantity > 0) {
            CarsList.getInstance().deleteCar(car.getReg());
            car.setQuantity(car.getQuantity() - 1);
            CarsList.getInstance().addNewCarToFile(car.toString());

            for(ServerManager client : clients) {
                client.sendDeleteCarResponse(car.getReg());
                client.sendCarToClient(car.toString());
            }
            return "You have bought the car with reg " + car.getReg();
        }
        return car.getReg() + " is out of stock";
    }

    /**
     * @param reg Registration number of the car that will be deleted
     */
    private String handleDeleteCar(String reg) {
        CarsList.getInstance().deleteCar(reg);
        for(ServerManager client : clients) {
            client.sendDeleteCarResponse(reg);
        }
        return "Car with Reg No. " + reg + " has been deleted";
    }

    /**
     * @param carInfo Comma separated information of a car,
     * similar to the format in the cars.txt file
     */
    private String handleAddCar(String carInfo) {
        String reg = carInfo.split(",")[0];
        if(!CarsList.getInstance().contains(reg)) {
            CarsList.getInstance().addNewCarToFile(carInfo);
            // Sending new Car info to all clients
            for(ServerManager client : clients) {
                client.sendCarToClient(carInfo);
            }
            return "Your car has been added";
        } else {
            return "Can not add your car. (A car with registration No. " + reg +
                    " already exists)";
        }

    }

    /**
     * @return Response message.
     * Format - LIN (login),(success or denial message)
     */
    private String handleLogin(String username, String pass) {
        List<String> users = UsersList.getInstance().getUsers();

        for(String user : users) {
            if(user.equals(username + "," + pass)) {
                return "LIN,login successful," + username;
            }
        }
        return "LIN,Access Denied";
    }

    /**
     * Sends delete response to the client
     */
    public void sendDeleteCarResponse(String reg) {
        outputToClient.println("DLT," + reg);
    }

    /**
     * Sends all information of a car to client
     */
    public void sendCarToClient(String car) {
        outputToClient.println("car," + car);
    }

    /**
     * Sends a list of car information to the client
     */
    private void sendCarsToClient() {
        List<String> cars = CarsList.getInstance().getCars();

        for(String car : cars) {
            sendCarToClient(car);
        }
    }
}
