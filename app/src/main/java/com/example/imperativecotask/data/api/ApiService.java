package com.example.imperativecotask.data.api;

import com.example.imperativecotask.data.models.TransactionData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("transactions")
    Call<List<TransactionData>> getTransactions(@Header("Authorization") String token);
}

