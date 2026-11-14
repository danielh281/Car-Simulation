package com.dh.carsimulationassignment;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    }

    @FXML
    private void pauseOnAction(ActionEvent event) {
    }

    @FXML
    private void resetOnAction(ActionEvent event) {
    }

}
