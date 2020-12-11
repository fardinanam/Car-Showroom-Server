package fileHandleClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CarsList {
    private static CarsList instance;
    private List<String> cars;
    private CarsList() {
        cars = new ArrayList<>();
        addCarsFromFile();
    }

    public void addCarsFromFile() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("database/cars.txt"));
            while (true) {
                String line = reader.readLine();
                if(line == null) {
                    break;
                }
                addCar(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public void addNewCarToFile(String car) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("database/cars.txt", true));
            writer.append(car + "\n");
            addCar(car);
            writer.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }

    public void addCar(String car) {
        cars.add(car);
    }

    public boolean contains(String reg) {
        for(String car : cars) {
            if(car.split(",")[0].equalsIgnoreCase(reg)) {
                return true;
            }
        }
        return false;
    }


    public static CarsList getInstance() {
        if(instance == null) {
            instance = new CarsList();
        }
        return instance;
    }

    public List<String> getCars() {
        return cars;
    }
}
