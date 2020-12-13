package fileHandleClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UsersList {
    private static UsersList instance;
    private List<String> users;
    private  UsersList() {
        users = new ArrayList<>();
        addUsersFromFile();
    }

    /**
     * Parses comma separated user information from the file and adds it
     * to the list.
     * Format - username,password
     */
    public void addUsersFromFile() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("database/users.txt"));
            while (true) {
                String line = reader.readLine();
                if(line == null) {
                    break;
                }
                addUser(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void addUser(String user) {
        users.add(user);
    }

    public static UsersList getInstance() {
        if(instance == null) {
            instance = new UsersList();
        }
        return instance;
    }

    public List<String> getUsers() {
        return users;
    }
}
