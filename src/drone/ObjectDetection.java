package drone; // Package declaration

import java.util.ArrayList; // Importing ArrayList class from java.util package
import java.util.List; // Importing List interface from java.util package
import java.util.Random; // Importing Random class from java.util package

public class ObjectDetection {

    // Method to detect objects around the drone
    public static List<DetectedObject> detectObjects(int droneX, int droneY, int range) {
        List<DetectedObject> detectedObjects = new ArrayList<>(); // List to store detected objects
        Random random = new Random(); // Random number generator

        // Simulate the detection of random objects around the drone
        for (int i = 0; i < 5; i++) {  // Loop to create 5 random objects (adjust the number as needed)
            // Calculate the object's random position within the range around the drone
            int x = droneX + random.nextInt(range * 2 + 1) - range; // Random X position within the range
            int y = droneY + random.nextInt(range * 2 + 1) - range; // Random Y position within the range

            // Ensure objects are within the bounds of the grid, and avoid clustering at the boundaries
            x = Math.max(5, Math.min(x, 95));  // Clamp X position to be within [5, 95]
            y = Math.max(5, Math.min(y, 95));  // Clamp Y position to be within [5, 95]

            // Randomly assign type and subtype
            String type = random.nextBoolean() ? "Human" : "Vehicle"; // Randomly choose between "Human" and "Vehicle"
            String subType = type.equals("Human")
                    ? (random.nextBoolean() ? "Male Adult" : "Female Adult") // If Human, choose between "Male Adult" and "Female Adult"
                    : (random.nextBoolean() ? "SUV" : "Truck"); // If Vehicle, choose between "SUV" and "Truck"

            // Create and add the detected object to the list
            detectedObjects.add(new DetectedObject(x, y, type, subType)); // Add new DetectedObject to the list
        }

        return detectedObjects; // Return the list of detected objects
    }

    // Inner class to represent detected objects
    public static class DetectedObject {
        public int x; // X position of the detected object
        public int y; // Y position of the detected object
        public String type; // Type of the detected object (e.g., Human, Vehicle)
        public String subType; // Subtype of the detected object (e.g., Male Adult, SUV)

        // Constructor to initialize the detected object
        public DetectedObject(int x, int y, String type, String subType) {
            this.x = x; // Set X position
            this.y = y; // Set Y position
            this.type = type; // Set type
            this.subType = subType; // Set subtype
        }

        // Method to simulate movement of detected objects (if needed in future updates)
        public void moveObject() {
            Random random = new Random(); // Random number generator

            // Move randomly by -1, 0, or 1, while ensuring the object stays within the grid
            this.x = Math.max(5, Math.min(x + random.nextInt(3) - 1, 95)); // Randomly adjust X position within bounds
            this.y = Math.max(5, Math.min(y + random.nextInt(3) - 1, 95)); // Randomly adjust Y position within bounds
        }

        @Override
        public String toString() {
            return type + " (" + subType + ") at (" + x + ", " + y + ")"; // Return string representation of the object
        }
    }
}
