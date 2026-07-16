package org.example;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    // Array of train details
    private String[] trainDetails = {
        "Train 101: City Express - Platform 2",
        "Train 102: Coastal Line - Platform 4",
        "Train 103: Mountain View - Platform 1",
        "Train 104: River Breeze - Platform 5",
        "Train 105: Sunset Limited - Platform 3"
    };
    private int currentIndex = 0;  // To track which train is currently displayed

    @Override
    public void start(Stage primaryStage) {
        // Create a Label to act as the notice board
        Label label = new Label(trainDetails[currentIndex]);

        // Create a VBox layout and add the label to it
        VBox vbox = new VBox(10);  // 10 is the spacing between components
        vbox.getChildren().add(label);

        // Setting up the Scene
        Scene scene = new Scene(vbox, 400, 200);

        // Setting the Stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Railway Station Notice Board");
        primaryStage.show();

        // Create a Timeline to update the label every 5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            // Update the label with the next train detail
            currentIndex = (currentIndex + 1) % trainDetails.length;  // Loops back to the start after the last train
            label.setText(trainDetails[currentIndex]);
        }));

        // Make the timeline run indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();  // Start the timeline
    }

    public static void main(String[] args) {
        launch(args);
    }
}
