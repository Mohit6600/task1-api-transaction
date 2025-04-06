package com.example.imperativecotask.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class SecureStorage {
    private final SharedPreferences sharedPreferences;

    public SecureStorage(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SecureStorage", e);
        }
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString("AUTH_TOKEN", token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString("AUTH_TOKEN", null);
    }

    public void clearToken() {
        sharedPreferences.edit().remove("AUTH_TOKEN").apply();
    }

    public void saveNumberPassword(String password) {
        sharedPreferences.edit().putString("number_password", password).apply();
    }

    public String getNumberPassword() {
        return sharedPreferences.getString("number_password", null);
    }

    public void clearNumberPassword() {
        sharedPreferences.edit().remove("number_password").apply();
    }
}