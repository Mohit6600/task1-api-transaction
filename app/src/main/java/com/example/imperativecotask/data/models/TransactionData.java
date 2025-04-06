package com.example.imperativecotask.data.models;

import com.google.gson.annotations.SerializedName;

public class TransactionData {
    @SerializedName("id")
    private String id;

    @SerializedName("amount")
    private double amount;

    @SerializedName("description")
    private String description;

    @SerializedName("date")
    private String date;

    @SerializedName("category")
    private String category;

    // Getters
    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}