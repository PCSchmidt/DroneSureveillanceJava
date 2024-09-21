package drone;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.List;
import drone.ObjectDetection;
import drone.ObjectDetection.DetectedObject;


public class DroneVisualizer extends Application {

    private static final int WIDTH = 800;  // Width of the scene
    private static final int HEIGHT = 600;  // Height of the scene
    private static final int GRID_SIZE = 100;  // Size of the grid
    private Circle droneCircle;
    private Circle visionRange;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Optional: Add background image here if needed
        // Load the background image (ensure the path is correct)
        Image backgroundImage = new Image("file:assets/map.jpg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(WIDTH);
        backgroundView.setFitHeight(HEIGHT);
    
        // Add the background to the root
        root.getChildren().add(backgroundView);

        // Draw the grid
        drawGrid(root);

        // Create the drone circle
        droneCircle = new Circle(10, Color.BLUE);
        droneCircle.setTranslateX(WIDTH / 2);
        droneCircle.setTranslateY(HEIGHT / 2);
        root.getChildren().add(droneCircle);

        // Add vision range for drone
        visionRange = new Circle(100, Color.TRANSPARENT);
        visionRange.setStroke(Color.BLUE);
        visionRange.setOpacity(0.3);  // Make it semi-transparent
        visionRange.setTranslateX(WIDTH / 2);
        visionRange.setTranslateY(HEIGHT / 2);
        root.getChildren().add(visionRange);

        // Create the scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Drone Surveillance");
        primaryStage.show();

        // Start the simulation in a separate thread
        new Thread(() -> runSimulation(root)).start();
    }

    private void runSimulation(Pane root) {
        // Simulating the movement of the drone and detected objects
        for (int i = 0; i < 100; i++) {
            Platform.runLater(() -> {
                // Move the drone and update its vision range
                droneCircle.setTranslateX(droneCircle.getTranslateX() + 1);
                droneCircle.setTranslateY(droneCircle.getTranslateY() + 1);
                visionRange.setTranslateX(droneCircle.getTranslateX());
                visionRange.setTranslateY(droneCircle.getTranslateY());

                // Get the drone's current position and detection range
                int posX = (int) droneCircle.getTranslateX();
                int posY = (int) droneCircle.getTranslateY();
                int range = (int) visionRange.getRadius();

                // Detect objects around the drone
                List<ObjectDetection.DetectedObject> detectedObjects = ObjectDetection.detectObjects(posX, posY, range);

                // Show the detected objects on the screen
                showDetectedObjects(root, detectedObjects);
            });

            try {
                Thread.sleep(1000);  // Pause for 1 second between updates
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawGrid(Pane root) {
        int gridSpacing = 50;  // Spacing between grid lines
        for (int i = 0; i <= WIDTH; i += gridSpacing) {
            Line verticalLine = new Line(i, 0, i, HEIGHT);
            Line horizontalLine = new Line(0, i, WIDTH, i);

            verticalLine.setStroke(Color.LIGHTGRAY);
            horizontalLine.setStroke(Color.LIGHTGRAY);

            root.getChildren().addAll(verticalLine, horizontalLine);
        }
    }

    private void showDetectedObjects(Pane root, List<ObjectDetection.DetectedObject> objects) {
        // Remove previous objects
        root.getChildren().removeIf(node -> node instanceof Circle && node != droneCircle && node != visionRange);

        for (ObjectDetection.DetectedObject obj : objects) {
            Circle objectCircle = new Circle(5); //Set the radius of the detected object 

            // Set color based on object type
            if (obj.type.equals("Human")) {
                objectCircle.setFill(obj.subType.equals("Male Adult") ? Color.BLUE : Color.PINK);
            } else if (obj.type.equals("Vehicle")) {
                objectCircle.setFill(obj.subType.equals("SUV") ? Color.DARKGRAY : Color.ORANGE);
            }

            double scaleX = WIDTH / (double) GRID_SIZE;
            double scaleY = HEIGHT / (double) GRID_SIZE;

            objectCircle.setTranslateX(obj.x * scaleX);
            objectCircle.setTranslateY(obj.y * scaleY);

            // Add a tooltip with object details
            Tooltip tooltip = new Tooltip(obj.type + " (" + obj.subType + ")");
            Tooltip.install(objectCircle, tooltip);

            root.getChildren().add(objectCircle);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
