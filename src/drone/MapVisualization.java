package drone;

import java.util.List;
import drone.ObjectDetection;
import drone.ObjectDetection.DetectedObject;
import javafx.scene.layout.Pane;


public class MapVisualization {

    public static void display(int posX, int posY, List<ObjectDetection.DetectedObject> detectedObjects) {
        System.out.println("Drone at: (" + posX + ", " + posY + ")");
        
        if (detectedObjects.isEmpty()) {
            System.out.println("No objects detected.");
        } else {
            System.out.println("Detected objects:");
            for (ObjectDetection.DetectedObject obj : detectedObjects) {
                System.out.println("  - " + obj);
            }
        }
    }
}

