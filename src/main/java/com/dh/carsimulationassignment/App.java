package com.dh.carsimulationassignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader slideshowLoader = getFXMLLoader("slideshow");
        FXMLLoader carRaceLoader = getFXMLLoader("carRace");
        
        scene = new Scene(slideshowLoader.load());
        
        stage.setScene(scene);
        stage.setTitle("Car Race Simulation");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(getFXMLLoader(fxml).load());
        
        Stage stage = (Stage) scene.getWindow();
        
        if (stage != null) {
            stage.sizeToScene();
            stage.centerOnScreen();
        }
    }

    private static FXMLLoader getFXMLLoader(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }

}