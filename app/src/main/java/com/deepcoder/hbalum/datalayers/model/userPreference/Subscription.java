package com.deepcoder.hbalum.datalayers.model.userPreference;

import com.google.gson.annotations.SerializedName;

public class Subscription {

    @SerializedName("id")
    private int id;

    @SerializedName("duration")
    private int duration;

    @SerializedName("title")
    private String title;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

	public int getId() {
		return id;
	}

	public int getDuration() {
		return duration;
	}

	public String getTitle() {
		return title;
	}

	public Double getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}
}