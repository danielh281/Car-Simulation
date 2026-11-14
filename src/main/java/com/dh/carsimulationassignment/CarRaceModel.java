package com.dh.carsimulationassignment;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Model class containing each car's data
 * @author Daniel Haddadeen (2588731)
 */
public class CarRaceModel {
    private CarType type;
    private CarColor color;
    private int number;
    private DoubleProperty positionX = new SimpleDoubleProperty(0);
    private DoubleProperty positionY = new SimpleDoubleProperty(0);
    private double speed;

    public CarRaceModel(CarType type, CarColor color, int number) {
        this.type = type;
        this.color = color;
        this.number = number;
    }
    
    /**
     * Creates the entry name for the CarRaceModel object
     * @return The entry name of the CarRaceModel
     */
    public String getCarEntryName() {
        return String.format("Car #%d (%s)", number, color.getName());
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPositionX(double positionX) {
        this.positionX.set(positionX);
    }

    public void setPositionY(double positionY) {
        this.positionY.set(positionY);
    }

    public CarColor getColor() {
        return color;
    }
    
    public int getNumber() {
        return number;
    }
    
    public double getSpeed() {
        return speed;
    }

    public DoubleProperty getPositionX() {
        return positionX;
    }

    public DoubleProperty getPositionY() {
        return positionY;
    }
}
