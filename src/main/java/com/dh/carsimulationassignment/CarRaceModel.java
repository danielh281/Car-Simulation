package com.dh.carsimulationassignment;

public class CarRaceModel {
    private CarType type;
    private CarColor color;
    private int number;
    private double positionX;
    private double positionY;
    private double speed;

    public CarRaceModel(CarType type, CarColor color, int number) {
        this.type = type;
        this.color = color;
        this.number = number;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getSpeed() {
        return speed;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
}
