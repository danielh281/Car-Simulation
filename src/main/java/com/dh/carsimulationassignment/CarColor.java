package com.dh.carsimulationassignment;

/**
 * Enumerations for car colors
 * @author Daniel Haddadeen (2588731)
 */
public enum CarColor {
    RED("Red"),
    PINK("Pink"),
    BLUE("Blue"),
    GREEN("Green");
    
    private String name;

    CarColor(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
