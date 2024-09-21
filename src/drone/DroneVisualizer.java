package drone;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.List;
import drone.ObjectDetection;
import drone.ObjectDetection.DetectedObject;
import java.util.Random;

public class DroneVisualizer extends Application {

    // Grid definition (manual 2D array)
    // 0 = non-street area, 1 = street area
    private int[][] grid = {
            { 0, 0, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 0, 0 },
            { 1, 1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1 },
            { 0, 0, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 0, 0 }
    };

    private static final int GRID_WIDTH = 6; // The width of the grid
    private static final int GRID_HEIGHT = 6; // The height of the grid
    private static final int CELL_SIZE = 100; // Size of each cell in the grid

    private static final int WIDTH = GRID_WIDTH * CELL_SIZE; // Adjust width of the scene
    private static final int HEIGHT = GRID_HEIGHT * CELL_SIZE; // Adjust height of the scene

    private Circle droneCircle;
    private Circle visionRange;
    private Pane root;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();

        // Optional: Add background image here if needed
        Image backgroundImage = new Image("file:assets/map.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(WIDTH);
        backgroundView.setFitHeight(HEIGHT);

        // Add the background to the root
        root.getChildren().add(backgroundView);

        // Draw the grid
        drawGrid(root);

        // Create the drone circle
        droneCircle = new Circle(10, Color.BLUE);
        droneCircle.setTranslateX(CELL_SIZE * 2); // Set initial position inside the grid
        droneCircle.setTranslateY(CELL_SIZE * 2);
        root.getChildren().add(droneCircle);

        // Add vision range for the drone
        visionRange = new Circle(CELL_SIZE, Color.TRANSPARENT);
        visionRange.setStroke(Color.BLUE);
        visionRange.setOpacity(0.3); // Make it semi-transparent
        visionRange.setTranslateX(droneCircle.getTranslateX());
        visionRange.setTranslateY(droneCircle.getTranslateY());
        root.getChildren().add(visionRange);

        // Create the scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Drone Surveillance");
        primaryStage.show();

        // Add a legend
        addLegend();

        // Start the simulation in a separate thread
        new Thread(() -> runSimulation()).start();
    }

    private void runSimulation() {
        // Simulating the movement of the drone and detected objects
        for (int i = 0; i < 100; i++) {
            Platform.runLater(() -> {
                // Move the drone
                moveDrone();

                // Get the drone's current position and detect objects
                int droneXGrid = (int) droneCircle.getTranslateX() / CELL_SIZE;
                int droneYGrid = (int) droneCircle.getTranslateY() / CELL_SIZE;
                List<DetectedObject> detectedObjects = ObjectDetection.detectObjects(droneXGrid, droneYGrid, 2);

                // Show the detected objects within the drone's vision radius
                showDetectedObjects(detectedObjects);
            });

            try {
                Thread.sleep(1000); // Pause for 1 second between updates
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Move the drone within grid bounds
    private void moveDrone() {
        double newX = droneCircle.getTranslateX() + CELL_SIZE;
        double newY = droneCircle.getTranslateY() + CELL_SIZE;

        // Wrap around if the drone goes outside the grid
        if (newX >= WIDTH) newX = 0;
        if (newY >= HEIGHT) newY = 0;

        droneCircle.setTranslateX(newX);
        droneCircle.setTranslateY(newY);
        visionRange.setTranslateX(newX);
        visionRange.setTranslateY(newY);
    }

    private void showDetectedObjects(List<DetectedObject> objects) {
        // Remove previous objects
        root.getChildren().removeIf(node -> node instanceof Circle && node != droneCircle && node != visionRange);

        for (DetectedObject obj : objects) {
            Circle objectCircle = new Circle(5); // Set the radius of the detected object

            // Set color based on object type
            if (obj.type.equals("Human")) {
                objectCircle.setFill(obj.subType.equals("Male Adult") ? Color.BLUE : Color.PINK);
            } else if (obj.type.equals("Vehicle")) {
                objectCircle.setFill(obj.subType.equals("SUV") ? Color.DARKGRAY : Color.ORANGE);
            }

            // Ensure objects are within drone's vision radius
            double scaleX = WIDTH / (double) GRID_WIDTH;
            double scaleY = HEIGHT / (double) GRID_HEIGHT;

            objectCircle.setTranslateX(obj.x * scaleX);
            objectCircle.setTranslateY(obj.y * scaleY);

            // Add a tooltip with object details
            Tooltip tooltip = new Tooltip(obj.type + " (" + obj.subType + ")");
            Tooltip.install(objectCircle, tooltip);

            root.getChildren().add(objectCircle);
        }
    }

    private void drawGrid(Pane root) {
        int gridSpacing = CELL_SIZE; // Spacing between grid lines
        for (int i = 0; i <= WIDTH; i += gridSpacing) {
            Line verticalLine = new Line(i, 0, i, HEIGHT);
            Line horizontalLine = new Line(0, i, WIDTH, i);

            verticalLine.setStroke(Color.LIGHTGRAY);
            horizontalLine.setStroke(Color.LIGHTGRAY);

            root.getChildren().addAll(verticalLine, horizontalLine);
        }
    }

    // Add legend for the drone and objects
    private void addLegend() {
        // Create rectangles and labels for the legend
        Rectangle droneLegend = new Rectangle(10, 10, Color.BLUE);
        Label droneLabel = new Label("Drone", droneLegend);

        Rectangle humanLegend = new Rectangle(10, 10, Color.PINK);
        Label humanLabel = new Label("Female Human", humanLegend);

        Rectangle maleLegend = new Rectangle(10, 10, Color.BLUE);
        Label maleLabel = new Label("Male Human", maleLegend);

        Rectangle suvLegend = new Rectangle(10, 10, Color.DARKGRAY);
        Label suvLabel = new Label("SUV", suvLegend);

        Rectangle truckLegend = new Rectangle(10, 10, Color.ORANGE);
        Label truckLabel = new Label("Truck", truckLegend);

        // Position the legend items
        droneLabel.setLayoutX(10);
        droneLabel.setLayoutY(10);
        humanLabel.setLayoutX(10);
        humanLabel.setLayoutY(30);
        maleLabel.setLayoutX(10);
        maleLabel.setLayoutY(50);
        suvLabel.setLayoutX(10);
        suvLabel.setLayoutY(70);
        truckLabel.setLayoutX(10);
        truckLabel.setLayoutY(90);

        // Add the legend to the root
        root.getChildren().addAll(droneLabel, humanLabel, maleLabel, suvLabel, truckLabel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
