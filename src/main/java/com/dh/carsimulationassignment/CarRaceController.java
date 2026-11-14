package com.dh.carsimulationassignment;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
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
public class CarRaceController implements Initializable {


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
    private final Map<CarRaceModel, Pane> carInfo = new HashMap<>();
    private final List<Transition> carAnimations = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Random rand = new Random();

        // Cache all car nodes and car race models
        carInfo.put(new CarRaceModel(CarType.Ford, CarColor.RED, 50), redCar);
        carInfo.put(new CarRaceModel(CarType.BMW, CarColor.PINK, 93), pinkCar);
        carInfo.put(new CarRaceModel(CarType.Benz, CarColor.BLUE, 25), blueCar);
        carInfo.put(new CarRaceModel(CarType.Toyota, CarColor.GREEN, 76), greenCar);

        // Randomly set each car's speed & update position
        for (Entry<CarRaceModel, Pane> entry : carInfo.entrySet()) {
            CarRaceModel carRaceModel = entry.getKey();
            Pane carNode = entry.getValue();

            carRaceModel.setSpeed(rand.nextDouble(Config.MIN_SPEED, Config.MAX_SPEED));

            carNode.translateXProperty().addListener((ov, oldValue, newValue) -> {
                carRaceModel.setPositionX((double) newValue);
            });

            carNode.translateYProperty().addListener((ov, oldValue, newValue) -> {
                carRaceModel.setPositionY((double) newValue);
            });
        }

        // TODO: Create transition animations for each car


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
        // Only accept start button in waiting state
        if (!raceState.get().equals(RaceState.WAITING)) {
            return;
        }

        startButton.setDisable(true);

        for (Entry<CarRaceModel, Pane> entry : carInfo.entrySet()) {
            CarRaceModel carRaceModel = entry.getKey();
            Pane carNode = entry.getValue();

            double finishTime = Config.FINISH_POSITION_X / carRaceModel.getSpeed();
            
            TranslateTransition translate = new TranslateTransition(Duration.seconds(finishTime), redCar);
            translate.setFromX(0);
            translate.setToX(450);
            translate.setInterpolator(Interpolator.EASE_IN);

            carAnimations.add(translate);
        }
    }

    @FXML
    private void pauseOnAction(ActionEvent event) {
        if (raceState.get().equals(RaceState.WAITING)) {
            return;
        }

        for (Transition transition : carAnimations) {
            if (raceState.get().equals(RaceState.ONGOING)) {
                raceState.set(RaceState.PAUSED);
                transition.pause();
            } else {
                raceState.set(RaceState.ONGOING);
                transition.play();
            }
        }
    }

    @FXML
    private void resetOnAction(ActionEvent event) {
        if (raceState.get().equals(RaceState.WAITING)) {
            return;
        }

        raceState.set(RaceState.WAITING);

        for (Transition transition : carAnimations) {
            transition.stop();
            transition.jumpTo(Duration.ZERO);
        }
    }
}
