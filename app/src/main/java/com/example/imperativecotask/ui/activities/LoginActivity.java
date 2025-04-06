package com.example.imperativecotask.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.imperativecotask.data.api.ApiService;
import com.example.imperativecotask.data.api.RetrofitClient;
import com.example.imperativecotask.data.repositories.TransactionRepository;
import com.example.imperativecotask.databinding.ActivityMainBinding;
import com.example.imperativecotask.ui.viewmodels.TransactionViewModel;
import com.example.imperativecotask.ui.viewmodels.TransactionViewModelFactory;
import com.example.imperativecotask.utils.SecureStorage;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TransactionViewModel viewModel;
    private SecureStorage secureStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initVariables();

        if (secureStorage.getToken() != null) {
            if (secureStorage.getNumberPassword() != null) {

                navigateToEnterPin();

            } else {
                showBiometricAuth();
            }

        }

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void initVariables() {
        secureStorage = new SecureStorage(this);
        ApiService apiService = RetrofitClient.getApiService();
        TransactionRepository repository = new TransactionRepository(apiService, secureStorage);
        TransactionViewModelFactory factory = new TransactionViewModelFactory(repository, secureStorage);
        viewModel = new ViewModelProvider(this, factory).get(TransactionViewModel.class);
    }

    private void attemptLogin() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        viewModel.login(username, password);

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getTransactions().observe(this, transactions -> {
            if (transactions != null) {
                binding.progressBar.setVisibility(View.GONE);
                secureStorage.saveToken(secureStorage.getToken());
                navigateToNumberAuth();
            }
        });
    }

    private void showBiometricAuth() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                navigateToTransactions();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use your fingerprint or face to login")
                .setNegativeButtonText("Use Account Password")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void navigateToNumberAuth() {
        startActivity(new Intent(this, NumberAuthActivity.class));
        finish();
        Toast.makeText(this, "LogIn Successfully", Toast.LENGTH_SHORT).show();
    }

    private void navigateToTransactions() {
        startActivity(new Intent(this, TransactionsActivity.class));
        finish();
    }

    private void navigateToEnterPin() {
        startActivity(new Intent(this, EnterPinActivity.class));
        finish();
    }
}