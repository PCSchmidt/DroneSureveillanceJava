package drone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectDetection {

    // Method to detect objects around the drone
    public static List<DetectedObject> detectObjects(int droneX, int droneY, int range) {
        List<DetectedObject> detectedObjects = new ArrayList<>();
        Random random = new Random();

        // Simulate the detection of random objects around the drone
        for (int i = 0; i < 5; i++) {  // Adjust the number of objects as needed
            // Calculate the object's random position within the range around the drone
            int x = droneX + random.nextInt(range * 2 + 1) - range;
            int y = droneY + random.nextInt(range * 2 + 1) - range;

            // Ensure objects are within the bounds of the grid, and avoid clustering at the boundaries
            x = Math.max(5, Math.min(x, 95));  // Use 5 and 95 to avoid clustering near the boundaries
            y = Math.max(5, Math.min(y, 95));  // Similarly adjusted for the y position

            // Randomly assign type and subtype
            String type = random.nextBoolean() ? "Human" : "Vehicle";
            String subType = type.equals("Human")
                    ? (random.nextBoolean() ? "Male Adult" : "Female Adult")
                    : (random.nextBoolean() ? "SUV" : "Truck");

            // Create and add the detected object to the list
            detectedObjects.add(new DetectedObject(x, y, type, subType));
        }

        return detectedObjects;
    }

    // Inner class to represent detected objects
    public static class DetectedObject {
        public int x;
        public int y;
        public String type;
        public String subType;

        public DetectedObject(int x, int y, String type, String subType) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.subType = subType;
        }

        // Method to simulate movement of detected objects (if needed in future updates)
        public void moveObject() {
            Random random = new Random();

            // Move randomly by -1, 0, or 1, while ensuring the object stays within the grid
            this.x = Math.max(5, Math.min(x + random.nextInt(3) - 1, 95));
            this.y = Math.max(5, Math.min(y + random.nextInt(3) - 1, 95));
        }

        @Override
        public String toString() {
            return type + " (" + subType + ") at (" + x + ", " + y + ")";
        }
    }
}
