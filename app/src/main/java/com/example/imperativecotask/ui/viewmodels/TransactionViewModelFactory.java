package com.example.imperativecotask.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.imperativecotask.utils.SecureStorage;
import com.example.imperativecotask.data.repositories.TransactionRepository;

public class TransactionViewModelFactory implements ViewModelProvider.Factory {
    private final TransactionRepository repository;
    private final SecureStorage secureStorage;

    public TransactionViewModelFactory(TransactionRepository repository, SecureStorage secureStorage) {
        this.repository = repository;
        this.secureStorage = secureStorage;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TransactionViewModel.class)) {
            return (T) new TransactionViewModel(repository, secureStorage);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
