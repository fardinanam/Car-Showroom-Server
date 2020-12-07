package car;

public class Car {
    private final String reg;
    private final int year;
    private final String[] colors;
    private final String make;
    private final String model;
    private final int price;

    public Car(String reg, int year, String[] colors, String make, String model, int price) {
        this.reg = reg;
        this.year = year;
        this.colors = colors;
        this.make = make;
        this.model = model;
        this.price = price;
    }

    /**
     * @param carInfoByFileLine A comma separated line of the file with necessary information of a car
     */
    public Car(String carInfoByFileLine) {
        String[] carInfo = carInfoByFileLine.split(",");
        this.reg = carInfo[0];
        this.year = Integer.parseInt(carInfo[1]);
        this.colors = new String[3];
        for(int i=0; i<3; i++) {
            colors[i] = carInfo[2 + i];
        }
        this.make = carInfo[5];
        this.model = carInfo[6];
        this.price = Integer.parseInt(carInfo[7]);
    }

    public String getReg() {
        return reg;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    /**
     * @return String containing information of a car which is
     * formatted as a comma separated file line
     */
    public String toStringForFile() {
        StringBuilder carInfo = new StringBuilder(reg + "," + year + ",");
        for(int i = 0; i<3; i++) {
            if(i < colors.length) {
                carInfo.append(colors[i] + ",");
            } else {
                carInfo.append(",");
            }
        }
        carInfo.append(make + "," + model + "," + price);
        return carInfo.toString();
    }
    @Override
    public String toString() {
        StringBuilder carInfo = new StringBuilder();
        carInfo.append("Registration Number: " + reg + "\n");
        carInfo.append("Year Made: " + year + "\n");
        carInfo.append("Colors: ");
        for(int i=0; i<colors.length; i++) {
            if(colors[i] != null || !colors[i].equals("")) {
                carInfo.append( colors[i] + ", ");
            }
        }
        carInfo.append("\n");
        carInfo.append("Make: " + make + "\n");
        carInfo.append("Model: " + model + "\n");
        carInfo.append("Price: " + price + "\n");

        return carInfo.toString();
    }
}
