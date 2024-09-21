package drone;

import java.util.List;

public class Drone {
    private int posX, posY; // Drone's position on the map
    private static final int GRID_SIZE = 100;
    private static final int DETECTION_RANGE = 10; // Drone's detection range

    private ObjectDetection objectDetection;

    public Drone() {
        // Start the drone at the center of the grid
        this.posX = GRID_SIZE / 2;
        this.posY = GRID_SIZE / 2;
        this.objectDetection = new ObjectDetection();
    }

    // Method to start the simulation
    public void startSimulation() {
        System.out.println("Starting drone simulation...");
        for (int i = 0; i < 10; i++) {
            moveDrone();
            List<ObjectDetection.DetectedObject> detectedObjects = detectObjects();
            MapVisualization.display(posX, posY, detectedObjects);
        }
    }

    // Simulates random drone movement
    public void moveDrone() {
        // Randomly move the drone up, down, left, or right
        this.posX += (int)(Math.random() * 10 - 5); // Move in X direction
        this.posY += (int)(Math.random() * 10 - 5); // Move in Y direction

        // Ensure the drone stays within the grid
        this.posX = Math.max(0, Math.min(posX, GRID_SIZE));
        this.posY = Math.max(0, Math.min(posY, GRID_SIZE));

        System.out.println("Drone moved to position: (" + posX + ", " + posY + ")");
    }

    public int getPosX() {
        return this.posX;  // Assuming posX is a field representing the drone's X position
    }
    
    public int getPosY() {
        return this.posY;  // Assuming posY is a field representing the drone's Y position
    }
    
    // Detects objects around the drone
    public List<ObjectDetection.DetectedObject> detectObjects() {
        System.out.println("Detecting objects...");
        return objectDetection.detectObjects(posX, posY, DETECTION_RANGE);
    }
}
