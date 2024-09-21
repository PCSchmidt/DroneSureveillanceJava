package drone;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.List;
import javafx.application.Platform;

public class DroneVisualizer extends Application {

    private static final int WIDTH = 600;    // Width of the window
    private static final int HEIGHT = 600;   // Height of the window
    private static final int GRID_SIZE = 100; // Size of the grid

    private Circle droneCircle;  // Circle to represent the drone

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Create a circle representing the drone
        droneCircle = new Circle(10, Color.BLUE);
        droneCircle.setTranslateX(WIDTH / 2);
        droneCircle.setTranslateY(HEIGHT / 2);
        root.getChildren().add(droneCircle);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Drone Surveillance");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the simulation in a separate thread
        new Thread(() -> runSimulation(root)).start();
    }

    // Runs the drone simulation
    
/**
 * @param root
 */
private void runSimulation(Pane root) {
    Drone drone = new Drone();
    for (int i = 0; i < 10; i++) {
        drone.moveDrone();
        
        // Update the drone position on the JavaFX Application Thread
        Platform.runLater(() -> {
            updateDronePosition(drone.getPosX(), drone.getPosY());
        });
        
        List<ObjectDetection.DetectedObject> detectedObjects = drone.detectObjects();
        
        // Show detected objects on the JavaFX Application Thread
        Platform.runLater(() -> {
            showDetectedObjects(root, detectedObjects);
        });

        try {
            Thread.sleep(1000);  // 1-second delay to simulate real-time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

    // Update the drone's position on the grid
    private void updateDronePosition(int x, int y) {
        // Scale the drone's position based on the grid and window size
        double scaleX = WIDTH / (double) GRID_SIZE;
        double scaleY = HEIGHT / (double) GRID_SIZE;

        // Set the new position of the drone
        droneCircle.setTranslateX(x * scaleX);
        droneCircle.setTranslateY(y * scaleY);
    }

    // Display detected objects on the grid
    private void showDetectedObjects(Pane root, List<ObjectDetection.DetectedObject> objects) {
        root.getChildren().removeIf(node -> node != droneCircle);  // Clear previous objects
        for (ObjectDetection.DetectedObject obj : objects) {
            Circle objectCircle = new Circle(5);
            objectCircle.setFill(obj.type.equals("Human") ? Color.GREEN : Color.RED);  // Green for humans, red for vehicles

            // Scale the object positions based on the grid size and window size
            double scaleX = WIDTH / (double) GRID_SIZE;
            double scaleY = HEIGHT / (double) GRID_SIZE;

            objectCircle.setTranslateX(obj.x * scaleX);
            objectCircle.setTranslateY(obj.y * scaleY);
            root.getChildren().add(objectCircle);
        }
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}

