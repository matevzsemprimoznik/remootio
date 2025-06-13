# Remootio

Remootio is a mobile application designed to provide smart control over your doors or gates, enabling you to open, close, and manage access from your Android device. The app features a modern UI built with Jetpack Compose and supports real-time actions using cloud integration (Firebase). Project includes embedded firmware code that runs on the hardware devices to execute these actions securely and reliably.

## Features

- **Remote Device Control:**  
  Seamlessly control compatible devices (e.g., garage doors, gates) from your smartphone or tablet.
- **Multi-Platform Support:**  
  Native Android app and a cross-platform Expo app for iOS and Android.
- **Embedded Firmware:**  
  C++ codebase for ESP32 microcontrollers, providing robust hardware-level communication.
- **Modern UI:**  
  Clean and responsive user interface built with Jetpack Compose for Android and React Native for Expo.
- **Secure Communication:**  
  Uses secure protocols to ensure safe device access and operation.
- **Easy Setup:**  
  User-friendly onboarding and device configuration.
- **Real-time Status Updates:**  
  Receive instant feedback on device status and actions.
- **Extensible:**  
  Modular codebases for easy customization and expansio

## Project Structure

```
remootio/
├── android/    # Android native app source code
├── api/    # Vercel lamba functions
├── embedded/   Microcontroller source code
├── expo/  # Expo app source code
```

### `android/`
- Native Android application code built with Kotlin and Jetpack Compose.  
- Includes UI components, app logic, Firebase integration, and Android-specific resources.
- Allow user to trigger opening and closing door
- Allow user to set auto closing time

### `api/`
- Serverless backend functions deployed on Vercel.  
- These cloud functions handle business logic, API endpoints, and integration with external services.

### `embedded/`
- Embedded C++ firmware targeting ESP32 microcontrollers.  
- Responsible for device-level control, communication, and integration with the Remootio hardware ecosystem.
- Execute actions received from Firebase

### `expo/`
- Cross-platform mobile app built with React Native and Expo.  
- Allow user to trigger door opening and closing
- Includes Firebase integration

