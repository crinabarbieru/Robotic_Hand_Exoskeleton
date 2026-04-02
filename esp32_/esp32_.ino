#include <WiFi.h>
#include <WebServer.h>
#include <ESP32Servo.h>


const char* ssid = "your_password";
const char* password = "your_password";

Servo myServo;
const int servoPin = 9;  // Servo motor digital pin
const int flexPin = A0;  // Flex sensor analog pin

int targetAngle = 0;
int currentAngle = 0;
bool isSweeping = false;
bool shouldStop = false;
int sweepDirection = 1;
unsigned long lastSweepTime = 0;
const int sweepDelay = 15;

// Flex sensor states
enum FlexState { IDLE,
                 CALIBRATING,
                 MONITORING };
FlexState flexState = IDLE;
int baselineValue = -1;
int currentFlexValue = 0;
int minBendValue = -1;
unsigned long calibrationStartTime = 0;
const unsigned long calibrationDuration = 3000;  // 3s calibration

WebServer server(80);

void setCORSHeaders() {
  server.sendHeader("Access-Control-Allow-Origin", "http://localhost:3000");
  server.sendHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
  server.sendHeader("Access-Control-Allow-Headers", "Content-Type");
}

void handleRoot() {
  setCORSHeaders();
  server.send(200, "text/plain", "ESP32 Web Server Ready");
}

void handleCalibrate() {
  if (server.method() == HTTP_OPTIONS) {
    setCORSHeaders();
    server.send(204);
  } else if (server.method() == HTTP_GET) {
    flexState = CALIBRATING;
    calibrationStartTime = millis();
    minBendValue = 2048;  // Reset previous min
    setCORSHeaders();
    server.send(200, "text/plain", "Calibration started - keep sensor straight");
  } else {
    setCORSHeaders();
    server.send(405, "text/plain", "Method Not Allowed");
  }
}

void handleStop() {
  if (server.method() == HTTP_OPTIONS) {
    setCORSHeaders();
    server.send(204);
  } else if (server.method() == HTTP_POST) {
    // End monitoring and send final results
    flexState = IDLE;

    setCORSHeaders();
    if (minBendValue == -1) minBendValue = currentFlexValue;
    if (baselineValue == -1) baselineValue = currentFlexValue;
    String response = "{\"baseline\":" + String(baselineValue) + ",\"minBend\":" + String(minBendValue) + ",\"current\":" + String(currentFlexValue) + "}";
    server.send(200, "application/json", response);
    minBendValue = -1;
    baselineValue = -1;
  } else {
    setCORSHeaders();
    server.send(405, "text/plain", "Method Not Allowed");
  }
}

void handleMessage() {
  if (server.method() == HTTP_OPTIONS) {
    setCORSHeaders();
    server.send(204);
  } else if (server.method() == HTTP_POST) {
    String message = server.arg("message");
    Serial.println("Received from web: " + message);

    if (message == "stop") {
      shouldStop = true;
      isSweeping = false;
      setCORSHeaders();
      server.send(200, "text/plain", "Servo stopped");
      myServo.write(0);
      currentAngle = 0;
    } else {
      int number = message.toInt();
      number = constrain(number, 1, 10);
      targetAngle = map(number, 1, 10, 18, 180);
      isSweeping = true;
      shouldStop = false;

      setCORSHeaders();
      String response = "Servo sweeping between 0° and " + String(targetAngle) + "° (input: " + message + ")";
      server.send(200, "text/plain", response);
    }
  } else {
    setCORSHeaders();
    server.send(405, "text/plain", "Method Not Allowed");
  }
}

void updateServoSweep() {
  if (!isSweeping || shouldStop) return;

  if (millis() - lastSweepTime >= sweepDelay) {
    lastSweepTime = millis();

    currentAngle += sweepDirection;

    if (currentAngle >= targetAngle) {
      currentAngle = targetAngle;
      sweepDirection = -1;
    } else if (currentAngle <= 0) {
      currentAngle = 0;
      sweepDirection = 1;
    }

    myServo.write(currentAngle);
  }
}

void updateFlexSensor() {
  currentFlexValue = analogRead(flexPin);

  switch (flexState) {
    case CALIBRATING:
      // During calibration, establish baseline (average of readings)
      if (millis() - calibrationStartTime < calibrationDuration) {
        if (baselineValue == -1) {
          baselineValue = currentFlexValue;  // First reading
        } else {
          baselineValue = (baselineValue * 0.9) + (currentFlexValue * 0.1);  // Better smoothing
        }
      } else {
        flexState = MONITORING;
        Serial.println("Calibration complete. Baseline: " + String(baselineValue));
      }
      break;

    case MONITORING:
      if (minBendValue == -1 || currentFlexValue < minBendValue) {
        minBendValue = currentFlexValue;
      }
      break;

    case IDLE:
    default:
      // Do nothing
      break;
  }
}

void setup() {
  Serial.begin(115200);
  delay(1000);

  ESP32PWM::allocateTimer(0);
  myServo.setPeriodHertz(50);
  myServo.attach(servoPin, 500, 2400);
  myServo.write(0);
  delay(1000);

  pinMode(flexPin, INPUT);

  WiFi.begin(ssid, password);
  Serial.print("Connecting to WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("\nConnected to WiFi");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  server.on("/", handleRoot);
  server.on("/message", handleMessage);
  server.on("/calibrate", handleCalibrate);
  server.on("/stop-bend", handleStop);
  server.on("/flex-values", HTTP_GET, []() {
  setCORSHeaders();

    // Ensure we have valid values
    int sendMin = minBendValue == -1 ? currentFlexValue : minBendValue;
    int sendBaseline = baselineValue == -1 ? currentFlexValue : baselineValue;

    String response = "{\"current\":" + String(currentFlexValue) + ",\"baseline\":" + String(sendBaseline) + ",\"minBend\":" + String(sendMin) + "}";
    server.send(200, "application/json", response);
  });
  server.begin();
}

void loop() {
  server.handleClient();
  updateServoSweep();
  updateFlexSensor();
}