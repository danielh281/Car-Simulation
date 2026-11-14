package com.dh.carsimulationassignment;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

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
    private final List<ParallelTransition> carAnimations = new ArrayList<>();
    private ObservableList<CarRaceModel> finishedCars = FXCollections.observableArrayList();
    private IntegerProperty numOfFinishedCars = new SimpleIntegerProperty(0);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Random rand = new Random();

        // Cache all car nodes and car race models
        carInfo.put(new CarRaceModel(CarType.FORD, CarColor.RED, 50), redCar);
        carInfo.put(new CarRaceModel(CarType.BMW, CarColor.PINK, 93), pinkCar);
        carInfo.put(new CarRaceModel(CarType.BENZ, CarColor.BLUE, 25), blueCar);
        carInfo.put(new CarRaceModel(CarType.TOYOTA, CarColor.GREEN, 76), greenCar);

        // Randomly set each car's speed & update position
        for (Entry<CarRaceModel, Pane> entry : carInfo.entrySet()) {
            CarRaceModel carRaceModel = entry.getKey();
            Pane carNode = entry.getValue();

            carRaceModel.setSpeed(rand.nextDouble(Config.MIN_SPEED, Config.MAX_SPEED));
            
            // Update car position  
            carRaceModel.getPositionX().bind(carNode.translateXProperty());
            carRaceModel.getPositionY().bind(carNode.translateYProperty());
            
            // Mark the cars as finished when they cross the line
            carRaceModel.getPositionX().addListener((ov, oldValue, newValue) -> {
                if (newValue.doubleValue() >= Config.FINISH_POSITION_X && !finishedCars.contains(carRaceModel)) {
                    finishedCars.add(carRaceModel);
                }
            });
        }

        // Listen for simulation speed updates
        simulationSpeedSlider.valueProperty().addListener((ov, oldValue, newValue) -> {
            simulationSpeed.set((double) newValue);
            simulationSpeedLabel.setText(String.format("%.1fx", newValue));
        });
        
        // Listen for race status updates
        raceState.addListener((ov, oldValue, newValue) -> {
            raceStatusLabel.setText(newValue.getRaceStatusMessage());
            
            if (raceState.get().equals(RaceState.FINISHED)) {
                CarRaceModel firstCar = finishedCars.get(0);
                
                String carEntryName = firstCar.getCarEntryName();
                String message = String.format("Congratulations! %s is the winner!", carEntryName);
                
                raceMessageLabel.setText(message);
            }
        });
        
        // Bind button disable property
        startButton.disableProperty().bind(raceState.isNotEqualTo(RaceState.WAITING));
        resetButton.disableProperty().bind(raceState.isEqualTo(RaceState.WAITING));
        pauseButton.disableProperty().bind(raceState.isEqualTo(RaceState.WAITING)
                .or(raceState.isEqualTo(RaceState.FINISHED)));
        
        // Listen for when all cars finished
        finishedCars.addListener((ListChangeListener<CarRaceModel>) cl -> {
            while (cl.next()) {
                if (cl.wasAdded()) {
                    for (CarRaceModel carFinished : cl.getAddedSubList()) {
                        int place = finishedCars.indexOf(carFinished) + 1;
                        setLeaderboardEntry(place, carFinished);
                    }

                    if (finishedCars.size() >= carInfo.size()) {
                        raceState.set(RaceState.FINISHED);
                    }
                }
            }
        });
    }    

    @FXML
    private void startOnAction(ActionEvent event) {
        // Only accept start button in waiting state
        if (!raceState.get().equals(RaceState.WAITING)) {
            return;
        }
        
        raceState.set(RaceState.ONGOING);

        for (Entry<CarRaceModel, Pane> entry : carInfo.entrySet()) {
            CarRaceModel carRaceModel = entry.getKey();
            Pane carNode = entry.getValue();
            
            // Calculate finish time using speed and pixels for finish position
            double finishTime = Config.FINISH_POSITION_X / carRaceModel.getSpeed();
            Duration transitionDuration = Duration.seconds(finishTime);
            
            ParallelTransition carAnimation = new ParallelTransition();
            carAnimation.setInterpolator(Interpolator.EASE_IN);
            carAnimation.rateProperty().bind(simulationSpeed);
            
            // Move car forwards
            TranslateTransition translate = new TranslateTransition(transitionDuration, carNode);
            translate.setFromX(0);
            translate.setToX(Config.FINISH_POSITION_X);
            carAnimation.getChildren().add(translate);
            
            // Rotate the wheels
            for (Node childNode : carNode.getChildren()) {
                if (childNode.getId() != null && childNode.getId().equals("wheel")) {
                    // Calculate circumference of wheel and get rotation angle for distance
                    double diameter = childNode.getLayoutBounds().getWidth();
                    double circumference = diameter * Math.PI;
                    double revolutions = Config.FINISH_POSITION_X / circumference;
                    double angleRad = revolutions * 360;
                    
                    RotateTransition rotateTransition = new RotateTransition(transitionDuration, childNode);
                    rotateTransition.setFromAngle(0);
                    rotateTransition.setToAngle(angleRad);
                    
                    carAnimation.getChildren().add(rotateTransition);
                }
            }
            
            carAnimation.play();
            
            // Store the transition so later, we can modify it
            carAnimations.add(carAnimation);
        }
    }

    @FXML
    private void pauseOnAction(ActionEvent event) {
        if (raceState.get() == RaceState.WAITING || raceState.get() == RaceState.FINISHED) {
            return;
        }
        
        // Decide which state the raace is going into next when it gets paused
        RaceState nextState = raceState.get() == RaceState.ONGOING ? RaceState.PAUSED : RaceState.ONGOING;
        
        for (Transition transition : carAnimations) {
            if (nextState == RaceState.PAUSED) {
                transition.pause();
            } else {
                transition.play();
            }
        }
        
        raceState.set(nextState);
    }

    @FXML
    private void resetOnAction(ActionEvent event) {
        if (raceState.get() == RaceState.WAITING) {
            return;
        }

        raceState.set(RaceState.WAITING);

        for (ParallelTransition carAnimation : carAnimations) {
            carAnimation.jumpTo(Duration.ZERO);
            carAnimation.stop();
        }
        
        // Snaps back all of the cars to the start in case that the transition is already finished
        for (Pane carNode : carInfo.values()) {
            carNode.setTranslateX(0);
        }
        
        // Reset states
        raceMessageLabel.setText("");
        clearLeaderboardEntries();
        finishedCars.clear();
        carAnimations.clear();
    }
    
    public void setLeaderboardEntry(int place, CarRaceModel carFinished) {
        Label placeLabel = switch (place) {
            case 1 -> firstPlaceLabel;
            case 2 -> secondPlaceLabel;
            case 3 -> thirdPlaceLabel;
            case 4 -> fourthPlaceLabel;
            default -> null;
        };

        if (placeLabel == null) {
            return;
        }

        String prefix = switch (place) {
            case 1 -> "1st.";
            case 2 -> "2nd.";
            case 3 -> "3rd.";
            case 4 -> "4th.";
            default -> "";
        };

        if (carFinished == null) {
            placeLabel.setText(prefix);
        } else {
            String carEntryName = carFinished.getCarEntryName();
            placeLabel.setText(prefix + " " + carEntryName);
        }
    }
    
    public void clearLeaderboardEntries() {
        for (int i = 1; i < 5; i++) {
            setLeaderboardEntry(i, null);
        }
    }
}
