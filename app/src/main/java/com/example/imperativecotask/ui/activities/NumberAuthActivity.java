package com.example.imperativecotask.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imperativecotask.R;
import com.example.imperativecotask.utils.SecureStorage;

public class NumberAuthActivity extends AppCompatActivity {

    private EditText etNumberPassword;
    private Button btnSavePassword;
    private SecureStorage secureStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_setup);

        secureStorage = new SecureStorage(this);

        etNumberPassword = findViewById(R.id.etNumberPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);

        btnSavePassword.setOnClickListener(v -> savePassword());
    }

    private void savePassword() {
        String password = etNumberPassword.getText().toString().trim();

        if (password.length() != 4) {
            Toast.makeText(this, "Password must be 4 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        secureStorage.saveNumberPassword(password);
        navigateToTransactions();
    }

    private void navigateToTransactions() {
        startActivity(new Intent(this, TransactionsActivity.class));
        finish();
    }
}