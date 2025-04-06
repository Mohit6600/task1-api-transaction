package com.example.imperativecotask.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.imperativecotask.R;
import com.example.imperativecotask.data.api.ApiService;
import com.example.imperativecotask.data.api.RetrofitClient;
import com.example.imperativecotask.data.models.TransactionData;
import com.example.imperativecotask.data.repositories.TransactionRepository;
import com.example.imperativecotask.databinding.ActivityTransactionsBinding;
import com.example.imperativecotask.ui.adapters.TransactionAdapter;
import com.example.imperativecotask.ui.viewmodels.TransactionViewModel;
import com.example.imperativecotask.ui.viewmodels.TransactionViewModelFactory;
import com.example.imperativecotask.utils.SecureStorage;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private ActivityTransactionsBinding binding;
    private TransactionViewModel viewModel;
    private TransactionAdapter adapter;
    private SecureStorage secureStorage;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        secureStorage = new SecureStorage(this);

        searchEditText = findViewById(R.id.searchEditText);

        setupViewsAndLoadData();
        setupSearchFilter();
    }

    private void setupViewsAndLoadData() {

        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter();
        binding.rvTransactions.setAdapter(adapter);

        ApiService apiService = RetrofitClient.getApiService();
        TransactionRepository repository = new TransactionRepository(apiService, secureStorage);
        TransactionViewModelFactory factory = new TransactionViewModelFactory(repository, secureStorage);
        viewModel = new ViewModelProvider(this, factory).get(TransactionViewModel.class);

        loadTransactions();

        binding.btnLogout.setOnClickListener(v -> logout());
    }

    private void loadTransactions() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getTransactions().observe(this, transactions -> {
            binding.progressBar.setVisibility(View.GONE);
            if (transactions != null && !transactions.isEmpty()) {
                adapter.setTransactions(transactions);
            } else {
                Toast.makeText(this, "No transactions found", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.fetchTransactions();
    }

    private void logout() {
        viewModel.getTransactions().removeObservers(this);

        viewModel.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        Toast.makeText(this, "Log Out Successfully", Toast.LENGTH_SHORT).show();
    }

    private void setupSearchFilter() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTransactions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterTransactions(String searchText) {
        List<TransactionData> originalTransactions = viewModel.getTransactions().getValue();

        if (originalTransactions == null || originalTransactions.isEmpty()) {
            return;
        }

        List<TransactionData> filteredTransactions = new ArrayList<>();
        for (TransactionData transaction : originalTransactions) {
            String category = transaction.getCategory().toLowerCase();
            String[] words = category.split("\\s+");

            if (words.length > 0 && words[0].startsWith(searchText.toLowerCase())) {
                filteredTransactions.add(transaction);
            }
        }
        adapter.setTransactions(filteredTransactions);
    }
}