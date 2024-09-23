package drone; // Package declaration

import java.util.List; // Importing List class from java.util package
import drone.ObjectDetection; // Importing ObjectDetection class from drone package
import drone.ObjectDetection.DetectedObject; // Importing DetectedObject inner class from ObjectDetection
import drone.MapVisualization; // Importing MapVisualization class from drone package

public class Drone {
    private int posX, posY; // Drone's position on the map
    private static final int GRID_SIZE = 100; // Size of the grid
    private static final int DETECTION_RANGE = 10; // Drone's detection range

    private ObjectDetection objectDetection; // Instance of ObjectDetection class

    // Constructor to initialize the drone's position and object detection system
    public Drone() {
        // Start the drone at the center of the grid
        this.posX = GRID_SIZE / 2;
        this.posY = GRID_SIZE / 2;
        this.objectDetection = new ObjectDetection(); // Initialize object detection
    }

    // Method to start the simulation
    public void startSimulation() {
        System.out.println("Starting drone simulation...");
        // Loop to simulate drone movement and object detection
        for (int i = 0; i < 10; i++) {
            moveDrone(); // Move the drone
            List<ObjectDetection.DetectedObject> detectedObjects = detectObjects(); // Detect objects around the drone
            MapVisualization.display(posX, posY, detectedObjects); // Display the drone's position and detected objects on the map
        }
    }

    // Simulates random drone movement
    public void moveDrone() {
        // Randomly move the drone up, down, left, or right
        this.posX += (int)(Math.random() * 10 - 5); // Move in X direction
        this.posY += (int)(Math.random() * 10 - 5); // Move in Y direction

        // Ensure the drone stays within the grid
        this.posX = Math.max(0, Math.min(posX, GRID_SIZE)); // Clamp posX to be within [0, GRID_SIZE]
        this.posY = Math.max(0, Math.min(posY, GRID_SIZE)); // Clamp posY to be within [0, GRID_SIZE]

        System.out.println("Drone moved to position: (" + posX + ", " + posY + ")"); // Print the new position of the drone
    }

    // Getter method for posX
    public int getPosX() {
        return this.posX; // Return the drone's X position
    }
    
    // Getter method for posY
    public int getPosY() {
        return this.posY; // Return the drone's Y position
    }
    
    // Detects objects around the drone
    public List<ObjectDetection.DetectedObject> detectObjects() {
        System.out.println("Detecting objects...");
        // Call the detectObjects method of ObjectDetection class and return the list of detected objects
        return objectDetection.detectObjects(posX, posY, DETECTION_RANGE);
    }
}
