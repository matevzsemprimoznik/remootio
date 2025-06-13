#define ENABLE_USER_AUTH
#define ENABLE_DATABASE
#define DEFAULT_FLASH_FS FILESYSTEM
#define SSL_CLIENT WiFiClientSecure

#include <Arduino.h>
#include <WiFi.h>
#include <FirebaseClient.h>
#include <WiFiClientSecure.h>
#include <ArduinoJson.h>
#include "secrets.h"

const int gateRemotePin = 25;
const int ledPin = 2;

NoAuth no_auth;

FirebaseApp app;
RealtimeDatabase Database;
AsyncResult streamResult;

SSL_CLIENT ssl_client, stream_ssl_client;

using AsyncClient = AsyncClientClass;
AsyncClient aClient(ssl_client), streamClient(stream_ssl_client);

bool waitingForCloseTrigger = false;
unsigned long openCommandTime = 0;
const unsigned long openDelay = 27000;

void triggerRemote() {
    digitalWrite(gateRemotePin, LOW);
    digitalWrite(ledPin, LOW);
    delay(1000);
    digitalWrite(gateRemotePin, HIGH);
    digitalWrite(ledPin, HIGH);

}

void processData(AsyncResult &aResult)
{
    // Exits when no result available when calling from the loop.
    if (!aResult.isResult())
        return;

    if (aResult.available())
    {
        RealtimeDatabaseResult &RTDB = aResult.to<RealtimeDatabaseResult>();
        if (RTDB.isStream())
        {
            const char* jsonStr = RTDB.to<const char *>();

            if (!jsonStr || strcmp(jsonStr, "null") == 0) {
                Serial.println("Warning: Received null or 'null' JSON data.");
                return;
            }

            Serial.print("Raw JSON: ");
            Serial.println(jsonStr);

            JsonDocument doc;
            DeserializationError error = deserializeJson(doc, jsonStr);

            if (error) {
                Serial.print("Failed to parse JSON: ");
                Serial.println(error.c_str());
                return;
            }

            if (!doc.containsKey("status") || !doc["status"] || !doc.containsKey("timestamp") || !doc["timestamp"]) {
                Serial.println("Warning: Missing 'status' or 'timestamp' fields.");
                return;
            }

            const char* status = doc["status"];
            const char* timestamp = doc["timestamp"];

            Serial.print("Parsed status: ");
            Serial.println(status);

            // Control gate based on status
            if (strcmp(status, "KEEP_OPENED") == 0) {
              Serial.println("The gate status is KEEP_OPENED.");
              triggerRemote();
              openCommandTime = millis();
              waitingForCloseTrigger = true;
          } else if (strcmp(status, "CLOSE") == 0) {
              Serial.println("The gate status is CLOSE.");
              triggerRemote();
          } else if (strcmp(status, "OPEN") == 0) {
              Serial.println("The gate status is OPEN.");
              triggerRemote();
          } else {
              Serial.println("Unknown action status");
          }
        }
    }
}

void setup() {
  Serial.begin(115200);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  unsigned long startAttemptTime = millis();

  // Try to connect for 10 seconds max
  while (WiFi.status() != WL_CONNECTED && millis() - startAttemptTime < 10000) {
    delay(500);
    Serial.println("Connecting to WiFi...");
    // Allow ESP32 to run background tasks and feed watchdog
    yield();
  }

  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("Connected to WiFi!");
    Serial.print("Signal Strength (RSSI): ");
    Serial.println(WiFi.RSSI());
  } else {
    Serial.println("Failed to connect to WiFi");
  }

  pinMode(gateRemotePin, OUTPUT); 
  pinMode(ledPin, OUTPUT); 

  digitalWrite(gateRemotePin, HIGH);
  digitalWrite(ledPin, HIGH);

  Firebase.printf("Firebase Client v%s\n", FIREBASE_CLIENT_VERSION);

  ssl_client.setInsecure();
  stream_ssl_client.setInsecure();

  Serial.println("Initializing app...");
  initializeApp(aClient, app, getAuth(no_auth));

  // Or intialize the app and wait.
  // initializeApp(aClient, app, getAuth(user_auth), 120 * 1000, auth_debug_print);

  app.getApp<RealtimeDatabase>(Database);

  Database.url(FIREBASE_DATABASE_URL);

  // In SSE mode (HTTP Streaming) task, you can filter the Stream events by using AsyncClientClass::setSSEFilters(<keywords>),
  // which the <keywords> is the comma separated events.
  // The event keywords supported are:
  // get - To allow the http get response (first put event since stream connected).
  // put - To allow the put event.
  // patch - To allow the patch event.
  // keep-alive - To allow the keep-alive event.
  // cancel - To allow the cancel event.
  // auth_revoked - To allow the auth_revoked event.
  // To clear all prevousely set filter to allow all Stream events, use AsyncClientClass::setSSEFilters().
  streamClient.setSSEFilters("get,put,patch,keep-alive,cancel,auth_revoked");

  // The "unauthenticate" error can be occurred in this case because we don't wait
  // the app to be authenticated before connecting the stream.
  // This is ok as stream task will be reconnected automatically when the app is authenticated.
  // The streamClient must be used for Stream only.
  Database.get(streamClient, "/actions", processData, true /* SSE mode (HTTP Streaming) */, "streamTask");


  Serial.println("Stream connected");
}

unsigned long previousMillis = 0;
const long interval = 60000;

void loop() {
  app.loop();
  unsigned long currentMillis = millis();
  
  // Check WiFi connection
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("WiFi disconnected! Attempting reconnection...");
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

    // Optional: wait a bit for it to reconnect (non-blocking preferred)
    unsigned long reconnectStart = millis();
    while (WiFi.status() != WL_CONNECTED && millis() - reconnectStart < 5000) {
      delay(500);
      Serial.print(".");
    }
    
    if (WiFi.status() == WL_CONNECTED) {
      Serial.println("\nReconnected to WiFi!");
      Serial.print("Signal Strength (RSSI): ");
      Serial.println(WiFi.RSSI());
    } else {
      Serial.println("\nReconnection failed.");
    }
  }

  if (waitingForCloseTrigger && millis() - openCommandTime >= openDelay) {
      triggerRemote();
      waitingForCloseTrigger = false;
      openCommandTime = 0;
    }

  // Add other non-blocking tasks here
}
