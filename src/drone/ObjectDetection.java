package drone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectDetection {
    
    private static final int GRID_SIZE = 100;
    private Random random = new Random();

    // Class to represent detected objects (human or vehicle)
    public static class DetectedObject {
        public String type;  // "Human" or "Vehicle"
        public String subType;  // "Male", "Female", "Car", "Truck", etc.
        public int x, y;  // Coordinates of the object

        public DetectedObject(String type, String subType, int x, int y) {
            this.type = type;
            this.subType = subType;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return type + " (" + subType + ") at (" + x + ", " + y + ")";
        }
    }

    // Generates a random list of detected objects in the drone's range
    public List<DetectedObject> detectObjects(int droneX, int droneY, int range) {
        List<DetectedObject> detectedObjects = new ArrayList<>();

        // Randomly generate 1 to 5 objects
        int numObjects = random.nextInt(5) + 1;

        for (int i = 0; i < numObjects; i++) {
            // Randomly decide if the object is a human or vehicle
            String type = random.nextBoolean() ? "Human" : "Vehicle";
            String subType;

            // If it's a human, determine male/female, adult/child
            if (type.equals("Human")) {
                subType = random.nextBoolean() ? "Male" : "Female";
                subType += random.nextBoolean() ? " Adult" : " Child";
            } else {
                // If it's a vehicle, determine the type
                int vehicleType = random.nextInt(3);
                if (vehicleType == 0) {
                    subType = "Car";
                } else if (vehicleType == 1) {
                    subType = "Truck";
                } else {
                    subType = "SUV";
                }
            }

            // Randomly place the object within the drone's range
            int x = droneX + random.nextInt(range * 2 + 1) - range;
            int y = droneY + random.nextInt(range * 2 + 1) - range;

            // Ensure the object is within the grid boundaries
            x = Math.max(0, Math.min(x, GRID_SIZE));
            y = Math.max(0, Math.min(y, GRID_SIZE));

            detectedObjects.add(new DetectedObject(type, subType, x, y));
        }

        return detectedObjects;
    }
}

