# Transactions App - API Integration

This Android application integrates with the PrepStripe API to authenticate users and display their transaction history. It also implements biometric authentication for secure access and uses EncryptedSharedPreferences for secure token storage.

## Dependencies

The following dependencies are used in this project:

* **Retrofit + Moshi (JSON Parsing):**
  
* **Room Database (Optional):**
   
* **Biometric Authentication:**

* **EncryptedSharedPreferences:**
    
* **Hilt (Dependency Injection):**
    
* **ViewModel + LiveData:**
    

## Installation

1.  Clone the repository to your local machine.
2.  Open the project in Android Studio.
3.  Build and run the application on an Android emulator or physical device.

## Usage

1.  **Login:** Enter your credentials on the login screen.
2.  **Transactions:** Upon successful login, the app will display a list of transactions fetched from the API.
3.  **Biometric Authentication:** On subsequent app launches, you will be prompted to authenticate using your device's biometric authentication.
4.  **Logout:** Use the logout option to clear the stored token and return to the login screen.

## Notes

* Ensure that you have a valid API key and endpoint to test the application.
* Biometric authentication will only work on devices with biometric capabilities.
* The Room Database dependency is included but may not be used in the base implementation, depending on the requirements.
* Hilt is used for dependency injection to maintain a clean and testable architecture.

## Apk

* Debug APK: [Download](app-debug.apk)
