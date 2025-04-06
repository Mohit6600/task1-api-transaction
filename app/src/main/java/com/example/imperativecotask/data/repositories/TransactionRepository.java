package com.example.imperativecotask.data.repositories;

import com.example.imperativecotask.utils.SecureStorage;
import com.example.imperativecotask.data.api.ApiService;
import com.example.imperativecotask.data.api.LoginRequest;
import com.example.imperativecotask.data.api.LoginResponse;
import com.example.imperativecotask.data.models.TransactionData;

import java.util.List;

import retrofit2.Callback;

public class TransactionRepository {
    private final ApiService apiService;
    private final SecureStorage secureStorage;

    public TransactionRepository(ApiService apiService, SecureStorage secureStorage) {
        this.apiService = apiService;
        this.secureStorage = secureStorage;
    }

    public void login(String username, String password, Callback<LoginResponse> callback) {
        apiService.login(new LoginRequest(username, password)).enqueue(callback);
    }

    public void getTransactions(String token, Callback<List<TransactionData>> callback) {
        if (token != null) {
            apiService.getTransactions(token).enqueue(callback);
        } else {
            callback.onFailure(null, new Throwable("No token found"));
        }
    }
}
