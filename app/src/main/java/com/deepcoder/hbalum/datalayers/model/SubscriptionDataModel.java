package com.deepcoder.hbalum.datalayers.model;

import com.google.gson.annotations.SerializedName;

public class SubscriptionDataModel {

	@SerializedName("title")
	private String title;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("description")
	private String description= "";

	@SerializedName("duration_type")
	private String duration_type;

	@SerializedName("duration")
	private int duration;

	@SerializedName("amount")
	private double amount;

	@SerializedName("id")
	private int id;

	private boolean selected = false;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration_type() {
		return duration_type;
	}

	public void setDuration_type(String duration_type) {
		this.duration_type = duration_type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}