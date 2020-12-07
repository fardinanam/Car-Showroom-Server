package fileHandleClasses;

import java.io.BufferedReader;
import java.io.FileReader;
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
                addCars(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void addCars(String car) {
        cars.add(car);
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
