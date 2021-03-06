package fileHandleClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarsList {
    private static CarsList instance;
    private List<String> cars;
    private CarsList() {
        cars = new ArrayList<>();
        parseCarsFromFile();
    }

    /**
     * Parses comma separated car information from the file and adds it
     * to the list.
     * Format - reg,year,color1,color2,color3,make,model,price,quantity
     */
    public void parseCarsFromFile() {
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

    /**
     * Adds car to file and then to the cars ArrayList
     */
    public synchronized void addNewCarToFile(String car) {
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

    private void addCar(String car) {
        cars.add(car);
    }

    /**
     * @param reg Registration Number of the car that has to be deleted
     */
    public synchronized void deleteCar(String reg) {
        try {
            // Open the file without append mode
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("database/cars.txt"));

            // Using Iterator to avoid ConcurrentModificationException
            Iterator<String> carIterator = cars.iterator();
            while(carIterator.hasNext()) {
                String car = carIterator.next();
                if(reg.equalsIgnoreCase(car.split(",")[0])) {
                    carIterator.remove();
                } else {
                    // Write from the beginning of the file and then append
                    writer.append(car + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }

    /**
     * Checks if a car with registration number equals reg present in the list or not
     */
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
