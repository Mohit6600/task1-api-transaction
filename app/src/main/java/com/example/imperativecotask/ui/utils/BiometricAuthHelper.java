package com.example.imperativecotask.ui.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class BiometricAuthHelper {
    private final Context context;
    private final Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricAuthListener listener;
    private boolean isMandatory = false;
    private int maxAttempts = 3;
    private int attemptCount = 0;
    private String mandatoryNegativeText = "Exit";

    public BiometricAuthHelper(Context context) {
        this.context = context;
        this.executor = ContextCompat.getMainExecutor(context);
    }

    public void setBiometricAuthListener(BiometricAuthListener listener) {
        this.listener = listener;
    }

    public void setMandatory(boolean mandatory) {
        this.isMandatory = mandatory;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void setMandatoryNegativeText(String text) {
        this.mandatoryNegativeText = text;
    }

    public void authenticate(String title, String subtitle, String negativeButtonText) {
        createBiometricPrompt();
        setupPromptInfo(title, subtitle, negativeButtonText);
        showBiometricPrompt();
    }

    private void createBiometricPrompt() {
        biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        attemptCount = 0;
                        if (listener != null) {
                            listener.onAuthenticationSuccess();
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        attemptCount++;
                        if (listener != null) {
                            listener.onAuthenticationFailed();
                        }
                        handleFailedAttempt();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if (listener != null) {
                            listener.onAuthenticationError(errorCode, errString);
                        }
                        handleAuthenticationError(errorCode, errString);
                    }
                });
    }

    private void setupPromptInfo(String title, String subtitle, String negativeButtonText) {
        BiometricPrompt.PromptInfo.Builder builder = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setConfirmationRequired(false);

        String buttonText = isMandatory ?
                (mandatoryNegativeText != null ? mandatoryNegativeText : "Exit") :
                (negativeButtonText != null ? negativeButtonText : "Cancel");

        builder.setNegativeButtonText(buttonText);
        promptInfo = builder.build();
    }

    private void showBiometricPrompt() {
        try {
            biometricPrompt.authenticate(promptInfo);
        } catch (Exception e) {
            if (listener != null) {
                listener.onBiometricException(e);
            }
        }
    }

    private void handleFailedAttempt() {
        if (attemptCount >= maxAttempts) {
            if (listener != null) {
                listener.onMaxAttemptsReached();
            }
        } else if (isMandatory) {
            showBiometricPrompt();
        }
    }

    private void handleAuthenticationError(int errorCode, CharSequence errString) {
        if (isMandatory) {
            switch (errorCode) {
                case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                    if (listener != null) {
                        listener.onCriticalError(errorCode, "Authentication required. Exiting...");
                    }
                    break;

                case BiometricPrompt.ERROR_USER_CANCELED:
                case BiometricPrompt.ERROR_CANCELED:
                case BiometricPrompt.ERROR_TIMEOUT:

                    showBiometricPrompt();
                    break;

                default:
                    if (listener != null) {
                        listener.onCriticalError(errorCode, errString);
                    }
                    break;
            }
        }
    }

    public interface BiometricAuthListener {
        void onAuthenticationSuccess();

        void onAuthenticationFailed();

        void onAuthenticationError(int errorCode, CharSequence errString);

        void onMaxAttemptsReached();

        void onCriticalError(int errorCode, CharSequence errString);

        void onBiometricException(Exception e);
    }
}
