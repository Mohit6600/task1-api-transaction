package com.example.imperativecotask.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.imperativecotask.R;
import com.example.imperativecotask.utils.SecureStorage;

import java.util.concurrent.Executor;

public class EnterPinActivity extends AppCompatActivity {

    private EditText etEnterPin;
    private Button btnSubmitPin;
    private Button btnUseBiometric;
    private SecureStorage secureStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        secureStorage = new SecureStorage(this);

        etEnterPin = findViewById(R.id.etEnterPin);
        btnSubmitPin = findViewById(R.id.btnSubmitPin);
        btnUseBiometric = findViewById(R.id.btnUseBiometric);
        btnSubmitPin.setOnClickListener(v -> submitPin());
        btnUseBiometric.setOnClickListener(v -> checkBiometricAvailability());

        checkBiometricAvailability();
    }

    private void submitPin() {
        String enteredPin = etEnterPin.getText().toString().trim();
        String savedPin = secureStorage.getNumberPassword();

        if (enteredPin.equals(savedPin)) {
            navigateToTransactions();
        } else {
            Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToTransactions() {
        startActivity(new Intent(this, TransactionsActivity.class));
        finish();
    }

    private void checkBiometricAvailability() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(EnterPinActivity.this, "Biometric error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                navigateToTransactions();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(EnterPinActivity.this, "Biometric authentication failed.",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate using your biometric credential")
                .setNegativeButtonText("Use PIN")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }
}