# Robotic_Hand_Exoskeleton

Exoskeleton design and associated web application for controlling the device, tracking patient rehabilitation progress, and providing analytics to physical therapists.

**Published Conference Paper:**  
https://www.scitepress.org/Papers/2025/138354/138354.pdf

## 🧩 Repository Structure

The project includes a full-stack application and embedded system components organized as follows:

### **ExoPath**

`ExoPath_webapp` contains the complete code for the web application and device control system:

- **Frontend (React)**  
  User interface for therapists and patients

- **Backend (Java Spring Boot)**  
  Handles business logic, APIs, and communication with the device

- **esp32_**  
  Firmware for the Arduino Nano ESP32 microcontroller used to control the exoskeleton hardware

- **Database Script**  
  SQL scripts for database creation and initialization
