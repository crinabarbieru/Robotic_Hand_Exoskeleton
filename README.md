# Robotic_Hand_Exoskeleton

Exoskeleton design and associated web application for controlling the device, tracking patient rehabilitation progress, and providing analytics to physical therapists.

**Published Conference Paper:**  
https://www.scitepress.org/Papers/2025/138354/138354.pdf

## 🧩 Repository Structure

The project includes a full-stack application and embedded system components organized as follows:

### **ExoPath**

`ExoPath_webapp` contains the complete code for the web application and device control system:

- **Frontend (React)**  
  User interface for therapists and patients, and communication with the robotic device.

- **Backend (Java Spring Boot)**  
  Handles business logic, APIs, communication with the database.

- **esp32_**  
  Firmware for the Arduino Nano ESP32 microcontroller used to control the exoskeleton hardware.

- **Database Script**  
  SQL scripts for database creation and initialization.


`3d_models` contains the STL files for all exoskeleton components. To recreate the exoskeleton, the components in STL files need to be sliced and 3D printed (the prototype was printed using PLA and TPU filaments).

`img` contains images of the initial and final protoypes, as well as images with 3d models of individual components.
