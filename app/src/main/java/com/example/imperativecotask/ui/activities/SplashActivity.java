package com.example.imperativecotask.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imperativecotask.R;
import com.example.imperativecotask.utils.SecureStorage;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SecureStorage secureStorage = new SecureStorage(this);
            boolean isLoggedIn = secureStorage.getToken() != null;

            Intent intent = new Intent(this, isLoggedIn ? EnterPinActivity.class : LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
