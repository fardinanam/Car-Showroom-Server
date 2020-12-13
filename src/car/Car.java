package car;

public class Car {
    private final String reg;
    private final int year;
    private final String colors;
    private final String make;
    private final String model;
    private final int price;
    private  int quantity;

    /**
     * @param carInfoByFileLine A comma separated line of the file with necessary information of a car
     */
    public Car(String carInfoByFileLine) {
        String[] carInfo = carInfoByFileLine.split(",");
        this.reg = carInfo[0];
        this.year = Integer.parseInt(carInfo[1]);
        colors = carInfo[2] + "," + carInfo[3] + "," + carInfo[4];
        this.make = carInfo[5];
        this.model = carInfo[6];
        this.price = Integer.parseInt(carInfo[7]);
        this.quantity = Integer.parseInt(carInfo[8]);
    }

    public String getReg() {
        return reg;
    }

    public int getYear() {
        return year;
    }

    public String getColors() {
        return colors;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return reg + "," + year + "," + colors
                + "," + make + "," + model
                + "," + price + "," + quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
