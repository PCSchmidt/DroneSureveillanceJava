# Drone Surveillance Simulation

## Project Overview

This is a Java-based drone surveillance simulation project built using **JavaFX**. The project demonstrates a visual simulation where a drone moves within a city map, detects objects (humans and vehicles), and provides real-time updates on the drone's position and detected objects. The application also features a background map for a realistic view and simulates objects moving randomly within the grid.

## Features

- **Drone Movement**: The drone autonomously moves around the grid.
- **Object Detection**: Detects objects (humans and vehicles) within the drone's field of vision.
- **Randomized Object Placement**: Humans and vehicles appear randomly within the drone's detection range.
- **Real-Time Visualization**: Real-time drone movement and detected objects are visualized on a map.
- **Customizable Map**: A map image can be set as the background to provide a visual reference.

## Technologies Used

- **Java**: The main programming language for building the logic.
- **JavaFX**: For the GUI and visual simulation.
- **Random Object Detection**: Objects such as humans (adults) and vehicles (SUVs, trucks) are simulated randomly on the map.

## Setup Instructions

### Prerequisites

- **Java Development Kit (JDK)** version 11 or later
- **JavaFX SDK** version 21.0.4 or later

### Steps to Run

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/DroneSurveillanceJava.git
   ```
2. * **Download the JavaFX SDK** :

* Download the latest JavaFX SDK from [OpenJFX](https://openjfx.io/).
* Unzip the SDK to a location on your machine.
* **Compile the Java files** :
  Make sure to include the path to the JavaFX SDK when compiling:

3.  **Compile the app**

    ```bash 
    javac --module-path "/path/to/javafx-sdk-21.0.4/lib" --add-modules javafx.controls src/drone/*.java
    ```
4. **Run the application:**

    **After compiling, run the DroneVisualizer class:**

    ```bash
    java --module-path "/path/to/javafx-sdk-21.0.4/lib" --add-modules javafx.controls -cp src drone.DroneVisualizer
    ```

#### Project Structure

    ```bash
    DroneSurveillanceJava/
├── assets/
│   └── map.jpg               # Map background image
├── src/
│   └── drone/
│       ├── Drone.java        # Main drone logic
│       ├── DroneVisualizer.java # Visualization and simulation logic
│       ├── ObjectDetection.java  # Logic for detecting and handling objects (humans, vehicles)
│       ├── MapVisualization.java # (Optional) Utility for displaying map
├── README.md                 # This file
    ```

#### Simulation Details
The drone starts at a random position on the map.
It moves at a constant speed, and its movement is constrained within the grid.
Objects (humans, vehicles) are detected within the drone's range and displayed visually on the map.
Detected Objects:
Humans: Categorized as Male Adult and Female Adult.
Vehicles: Categorized as SUV and Truck.
Potential Improvements
Constrained Movement: Objects can be made to move along predefined street paths.
Drone Speed: Introduce variable speed for the drone.
Multiple Drones: Add support for multiple drones navigating the map.
Historical Tracking: Track the drone's previous path or detected objects over time.
#### Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

#### License
This project is licensed under the MIT License. See the LICENSE file for details.

#### Contact
For any questions or issues, feel free to reach out at .
