package drone; // Package declaration

import javafx.application.Application; // Importing JavaFX Application class
import javafx.application.Platform; // Importing JavaFX Platform class for UI updates
import javafx.scene.Scene; // Importing JavaFX Scene class
import javafx.scene.control.Label; // Importing JavaFX Label class
import javafx.scene.control.Tooltip; // Importing JavaFX Tooltip class
import javafx.scene.layout.Pane; // Importing JavaFX Pane class
import javafx.scene.paint.Color; // Importing JavaFX Color class
import javafx.scene.shape.Circle; // Importing JavaFX Circle class
import javafx.scene.shape.Line; // Importing JavaFX Line class
import javafx.scene.shape.Rectangle; // Importing JavaFX Rectangle class
import javafx.scene.image.Image; // Importing JavaFX Image class
import javafx.scene.image.ImageView; // Importing JavaFX ImageView class
import javafx.stage.Stage; // Importing JavaFX Stage class
import java.util.List; // Importing List class from java.util package
import drone.ObjectDetection; // Importing ObjectDetection class from drone package
import drone.ObjectDetection.DetectedObject; // Importing DetectedObject inner class from ObjectDetection
import java.util.Random; // Importing Random class from java.util package

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

    private Circle droneCircle; // Circle representing the drone
    private Circle visionRange; // Circle representing the drone's vision range
    private Pane root; // Root pane for the scene

    @Override
    public void start(Stage primaryStage) {
        root = new Pane(); // Initialize the root pane

        // Optional: Add background image here if needed
        Image backgroundImage = new Image("file:assets/map.png"); // Load background image
        ImageView backgroundView = new ImageView(backgroundImage); // Create ImageView for the background
        backgroundView.setFitWidth(WIDTH); // Set the width of the background
        backgroundView.setFitHeight(HEIGHT); // Set the height of the background

        // Add the background to the root
        root.getChildren().add(backgroundView);

        // Draw the grid
        drawGrid(root);

        // Create the drone circle
        droneCircle = new Circle(10, Color.BLUE); // Create a blue circle for the drone
        droneCircle.setTranslateX(CELL_SIZE * 2); // Set initial X position inside the grid
        droneCircle.setTranslateY(CELL_SIZE * 2); // Set initial Y position inside the grid
        root.getChildren().add(droneCircle); // Add the drone circle to the root

        // Add vision range for the drone
        visionRange = new Circle(CELL_SIZE, Color.TRANSPARENT); // Create a transparent circle for vision range
        visionRange.setStroke(Color.BLUE); // Set the stroke color to blue
        visionRange.setOpacity(0.3); // Make it semi-transparent
        visionRange.setTranslateX(droneCircle.getTranslateX()); // Set initial X position
        visionRange.setTranslateY(droneCircle.getTranslateY()); // Set initial Y position
        root.getChildren().add(visionRange); // Add the vision range circle to the root

        // Create the scene
        Scene scene = new Scene(root, WIDTH, HEIGHT); // Create a new scene with the root pane
        primaryStage.setScene(scene); // Set the scene on the primary stage
        primaryStage.setTitle("Drone Surveillance"); // Set the title of the stage
        primaryStage.show(); // Show the stage

        // Add a legend
        addLegend();

        // Start the simulation in a separate thread
        new Thread(() -> runSimulation()).start(); // Start the simulation in a new thread
    }

    private void runSimulation() {
        // Simulating the movement of the drone and detected objects
        for (int i = 0; i < 100; i++) { // Loop to simulate 100 steps
            Platform.runLater(() -> {
                // Move the drone
                moveDrone();

                // Get the drone's current position and detect objects
                int droneXGrid = (int) droneCircle.getTranslateX() / CELL_SIZE; // Calculate drone's X position in the grid
                int droneYGrid = (int) droneCircle.getTranslateY() / CELL_SIZE; // Calculate drone's Y position in the grid
                List<DetectedObject> detectedObjects = ObjectDetection.detectObjects(droneXGrid, droneYGrid, 2); // Detect objects around the drone

                // Show the detected objects within the drone's vision radius
                showDetectedObjects(detectedObjects);
            });

            try {
                Thread.sleep(1000); // Pause for 1 second between updates
            } catch (InterruptedException e) {
                e.printStackTrace(); // Print stack trace if interrupted
            }
        }
    }

    // Move the drone within grid bounds
    private void moveDrone() {
        double newX = droneCircle.getTranslateX() + CELL_SIZE; // Calculate new X position
        double newY = droneCircle.getTranslateY() + CELL_SIZE; // Calculate new Y position

        // Wrap around if the drone goes outside the grid
        if (newX >= WIDTH) newX = 0; // Wrap around X position
        if (newY >= HEIGHT) newY = 0; // Wrap around Y position

        droneCircle.setTranslateX(newX); // Set new X position
        droneCircle.setTranslateY(newY); // Set new Y position
        visionRange.setTranslateX(newX); // Set new X position for vision range
        visionRange.setTranslateY(newY); // Set new Y position for vision range
    }

    private void showDetectedObjects(List<DetectedObject> objects) {
        // Remove previous objects
        root.getChildren().removeIf(node -> node instanceof Circle && node != droneCircle && node != visionRange); // Remove all circles except the drone and vision range

        for (DetectedObject obj : objects) {
            Circle objectCircle = new Circle(5); // Set the radius of the detected object

            // Set color based on object type
            if (obj.type.equals("Human")) {
                objectCircle.setFill(obj.subType.equals("Male Adult") ? Color.BLUE : Color.PINK); // Set color based on subtype
            } else if (obj.type.equals("Vehicle")) {
                objectCircle.setFill(obj.subType.equals("SUV") ? Color.DARKGRAY : Color.ORANGE); // Set color based on subtype
            }

            // Ensure objects are within drone's vision radius
            double scaleX = WIDTH / (double) GRID_WIDTH; // Calculate scale factor for X
            double scaleY = HEIGHT / (double) GRID_HEIGHT; // Calculate scale factor for Y

            objectCircle.setTranslateX(obj.x * scaleX); // Set X position of the object
            objectCircle.setTranslateY(obj.y * scaleY); // Set Y position of the object

            // Add a tooltip with object details
            Tooltip tooltip = new Tooltip(obj.type + " (" + obj.subType + ")"); // Create a tooltip with object details
            Tooltip.install(objectCircle, tooltip); // Install the tooltip on the object circle

            root.getChildren().add(objectCircle); // Add the object circle to the root
        }
    }

    private void drawGrid(Pane root) {
        int gridSpacing = CELL_SIZE; // Spacing between grid lines
        for (int i = 0; i <= WIDTH; i += gridSpacing) {
            Line verticalLine = new Line(i, 0, i, HEIGHT); // Create vertical grid line
            Line horizontalLine = new Line(0, i, WIDTH, i); // Create horizontal grid line

            verticalLine.setStroke(Color.LIGHTGRAY); // Set color of vertical line
            horizontalLine.setStroke(Color.LIGHTGRAY); // Set color of horizontal line

            root.getChildren().addAll(verticalLine, horizontalLine); // Add grid lines to the root
        }
    }

    // Add legend for the drone and objects
    private void addLegend() {
        // Create rectangles and labels for the legend
        Rectangle droneLegend = new Rectangle(10, 10, Color.BLUE); // Create rectangle for drone legend
        Label droneLabel = new Label("Drone", droneLegend); // Create label for drone legend

        Rectangle humanLegend = new Rectangle(10, 10, Color.PINK); // Create rectangle for female human legend
        Label humanLabel = new Label("Female Human", humanLegend); // Create label for female human legend

        Rectangle maleLegend = new Rectangle(10, 10, Color.BLUE); // Create rectangle for male human legend
        Label maleLabel = new Label("Male Human", maleLegend); // Create label for male human legend

        Rectangle suvLegend = new Rectangle(10, 10, Color.DARKGRAY); // Create rectangle for SUV legend
        Label suvLabel = new Label("SUV", suvLegend); // Create label for SUV legend

        Rectangle truckLegend = new Rectangle(10, 10, Color.ORANGE); // Create rectangle for truck legend
        Label truckLabel = new Label("Truck", truckLegend); // Create label for truck legend

        // Position the legend items
        droneLabel.setLayoutX(10); // Set X position for drone label
        droneLabel.setLayoutY(10); // Set Y position for drone label
        humanLabel.setLayoutX(10); // Set X position for female human label
        humanLabel.setLayoutY(30); // Set Y position for female human label
        maleLabel.setLayoutX(10); // Set X position for male human label
        maleLabel.setLayoutY(50); // Set Y position for male human label
        suvLabel.setLayoutX(10); // Set X position for SUV label
        suvLabel.setLayoutY(70); // Set Y position for SUV label
        truckLabel.setLayoutX(10); // Set X position for truck label
        truckLabel.setLayoutY(90); // Set Y position for truck label

        // Add the legend to the root
        root.getChildren().addAll(droneLabel, humanLabel, maleLabel, suvLabel, truckLabel); // Add all legend items to the root
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}