package car;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CarList {
    List<Car> carList;
    CarList() {
        carList = new ArrayList<>();
    }

    /** Adds a car to the car list.
     * If the car with same registration number already exits,
     * it will not add it to the database
     * @param car It will be added to the list
     */
    public void add(Car car) {
        for(Car carCheck : carList) {
            if(car.getReg().equals(carCheck.getReg())) {
                System.out.println("The car with Registration Number " +
                        car.getReg() + " is already in the database");
                return;
            }
        }
        carList.add(car);
    }

    /**
     * Deletes the car with registration number equal to reg.
     * @param reg Registration number of the car to delete from car list
     */
    public void delete(String reg) {
        for(Car carCheck : carList) {
            if(reg.equalsIgnoreCase(carCheck.getReg())) {
                carList.remove(carCheck);
                System.out.println("Car with registration number \"" + reg +
                        "\" has been deleted from the list.");
                return;
            }
        }
        System.out.println("No car with Registration Number "
            + reg + " found");
    }

    /**
     * Searches the car with the registration number equals to reg
     * and prints information of the car.
     * @param reg Registration number of the car
     */
    public void searchByReg(String reg) {
        for(Car carCheck : carList) {
            if(reg.equalsIgnoreCase(carCheck.getReg())) {
                System.out.println(carCheck);
                return;
            }
        }
        System.out.println("No such car with this Registration Number");
    }

    /**
     * Searches the car with the Make and Model equals
     * to make and model and prints the information of the
     * cars with same make and model. If made equals "ANY"(ignore case)
     * then it prints information of all the cars from same make
     * @param make make name of the car/s
     * @param model model name of the car/s
     */
    public void searchByMakeModel(String make, String model) {
        boolean hasCarFound = false;
        for(Car carCheck : carList) {
            if(make.equalsIgnoreCase(carCheck.getMake())) {
                if(model.equalsIgnoreCase(carCheck.getModel()) ||
                    model.equalsIgnoreCase("any")) {
                    System.out.println(carCheck);
                    hasCarFound = true;
                }
            }
        }
        if(!hasCarFound) {
            System.out.println("No such car with this Car Make and Car Model");
        }
    }

    /**
     * Loads information of the cars from the file and saves it to the car list
     * @param filename Car information are loaded from this file
     */
    public void loadCarList(String filename) {
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                add(new Car(line));
            }
            br.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * Saves information of the cars to the file filename
     * @param filename Car information are stored in this file
     */
    public void saveCarList(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for(Car car : carList) {
                bw.write(car.toStringForFile());
                bw.write("\n");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return carList.isEmpty();
    }
}
