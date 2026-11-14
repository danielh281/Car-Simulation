package com.dh.carsimulationassignment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * FXML Controller class for the startup car slideshow.
 *
 * @author Daniel Haddadeen (2588731)
 */
public class SlideshowController implements Initializable {

    @FXML
    private ImageView carImage;
    @FXML
    private Label welcomeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carImage.setOpacity(0);
        welcomeLabel.setOpacity(0);
        playSlideshow();
    }

    public void playSlideshow() {
        SequentialTransition masterSequence = new SequentialTransition();

        // Welcome message sequence
        FadeTransition welcomeFadeIn = new FadeTransition(Duration.seconds(0.5), welcomeLabel);
        welcomeFadeIn.setFromValue(0);
        welcomeFadeIn.setToValue(1);

        PauseTransition welcomePause = new PauseTransition(Duration.seconds(1.5));

        FadeTransition welcomeFadeOut = new FadeTransition(Duration.seconds(0.5), welcomeLabel);
        welcomeFadeOut.setFromValue(1);
        welcomeFadeOut.setToValue(0);

        masterSequence.getChildren().addAll(welcomeFadeIn, welcomePause, welcomeFadeOut);

        // Car image sequence
        for (String imagePath : Config.CAR_IMAGES_PATH) {
            Image carImageFile = new Image(getClass().getResource(imagePath).toExternalForm());

            PauseTransition swapImage = new PauseTransition(Duration.millis(10));
            swapImage.setOnFinished(e -> carImage.setImage(carImageFile));

            FadeTransition imageFadeIn = new FadeTransition(Duration.seconds(0.5), carImage);
            imageFadeIn.setFromValue(0);
            imageFadeIn.setToValue(1);

            PauseTransition imagePause = new PauseTransition(Duration.seconds(1.5));

            FadeTransition imageFadeOut = new FadeTransition(Duration.seconds(0.5), carImage);
            imageFadeOut.setFromValue(1);
            imageFadeOut.setToValue(0);

            masterSequence.getChildren().addAll(swapImage, imageFadeIn, imagePause, imageFadeOut);
        }

        // Transition to the car race simulation once the slideshow finishes
        masterSequence.setOnFinished(eh -> {
            try {
                App.setRoot("carRace");
            } catch(Exception e) {
                // ignored
            }
        });

        masterSequence.play();
    }
}