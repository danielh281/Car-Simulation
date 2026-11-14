package com.dh.carsimulationassignment;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
/**
 * FXML Controller class
 *
 * @author Daniel Haddadeen (2588731)
 */
public class CarSimulationController implements Initializable {


    @FXML
    private Pane redCar;
    @FXML
    private Pane pinkCar;
    @FXML
    private Pane blueCar;
    @FXML
    private Pane greenCar;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resetButton;
    @FXML
    private Slider simulationSpeedSlider;
    @FXML
    private Label simulationSpeedLabel;
    @FXML
    private Label raceStatusLabel;
    @FXML
    private Label raceMessageLabel;
    @FXML
    private Label firstPlaceLabel;
    @FXML
    private Label secondPlaceLabel;
    @FXML
    private Label thirdPlaceLabel;
    @FXML
    private Label fourthPlaceLabel;
    
    private ObjectProperty<RaceState> raceState = new SimpleObjectProperty<>(RaceState.WAITING);
    private DoubleProperty simulationSpeed = new SimpleDoubleProperty(1);
    private Map<CarType, Pane> carInfo = new HashMap() {
        {
            put(CarType.RED, redCar);
            put(CarType.PINK, pinkCar);
            put(CarType.BLUE, blueCar);
            put(CarType.GREEN, greenCar);
        }
    };
    private Map<CarType, Double> finishTimes = new HashMap<>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Randomly decide finish times for each car type
        Random rand = new Random();
        
        for (CarType carType : CarType.values()) {
            finishTimes.put(carType, (Double) rand.nextDouble(Config.MIN_FINISH_TIME, Config.MAX_FINISH_TIME));
        }
        
        // Update simulation speed from slider
        simulationSpeedSlider.valueProperty().addListener((ov, oldValue, newValue) -> {
            simulationSpeed.set((double) newValue);
            simulationSpeedLabel.setText(String.format("%.1fx", newValue));
        });
        
        // Update race status label
        raceState.addListener((ov, oldValue, newValue) -> {
            raceStatusLabel.setText(newValue.getRaceStatusMessage());
        });
    }    
    
    @FXML
    private void startOnAction(ActionEvent event) {
        for (Entry<CarType, Pane> entry : carInfo.entrySet()) {
            double finishTime = finishTimes.get(entry.getKey());
            
            TranslateTransition translate = new TranslateTransition(Duration.seconds(finishTime), redCar);
            translate.setFromX(0);
            translate.setToX(450);
            translate.setInterpolator(Interpolator.EASE_IN);
            translate.play();
        }
    }

    @FXML
    private void pauseOnAction(ActionEvent event) {
    }

    @FXML
    private void resetOnAction(ActionEvent event) {
    }

}
