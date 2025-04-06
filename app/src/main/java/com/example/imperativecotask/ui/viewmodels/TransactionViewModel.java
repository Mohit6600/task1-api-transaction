package com.example.imperativecotask.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imperativecotask.utils.SecureStorage;
import com.example.imperativecotask.data.api.LoginResponse;
import com.example.imperativecotask.data.models.TransactionData;
import com.example.imperativecotask.data.repositories.TransactionRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionViewModel extends ViewModel {
    private final TransactionRepository repository;
    private final SecureStorage secureStorage;
    private final MutableLiveData<List<TransactionData>> transactions = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public TransactionViewModel(TransactionRepository repository, SecureStorage secureStorage) {
        this.repository = repository;
        this.secureStorage = secureStorage;
    }

    public void login(String username, String password) {
        isLoading.postValue(true);
        repository.login(username, password, new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    secureStorage.saveToken(response.body().getToken());
                    fetchTransactions();
                } else {
                    error.postValue("Login failed: " +
                            (response.errorBody() != null ?
                                    response.errorBody().toString() :
                                    "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public void fetchTransactions() {
        isLoading.postValue(true);
        String token = secureStorage.getToken();
        if (token == null || token.isEmpty()) {
            error.postValue("Not authenticated");
            isLoading.postValue(false);
            return;
        }

        repository.getTransactions(token, new Callback<List<TransactionData>>() {
            @Override
            public void onResponse(Call<List<TransactionData>> call, Response<List<TransactionData>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    transactions.postValue(response.body());
                } else {
                    error.postValue("Failed to fetch transactions: " +
                            (response.errorBody() != null ?
                                    response.errorBody().toString() :
                                    "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<List<TransactionData>> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public LiveData<List<TransactionData>> getTransactions() {
        return transactions;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void logout() {
        secureStorage.clearToken();
        secureStorage.clearNumberPassword();
        transactions.postValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}