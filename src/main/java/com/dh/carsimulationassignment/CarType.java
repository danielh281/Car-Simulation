package com.dh.carsimulationassignment;

/**
 *
 * @author Daniel Haddadeen (2588731)
 */
public enum CarType {
    RED("Red", 50),
    PINK("Pink", 93),
    BLUE("Blue", 25),
    GREEN("Green", 76);
    
    private String colorName;
    private int number;

    CarType(String colorName, int number) {
        this.colorName = colorName;
        this.number = number;
    }
    
    public String getColorName() {
        return colorName;
    }
    
    public int getNumber() {
        return number;
    }
}
