package com.deepcoder.hbalum.datalayers.model.userPreference;

import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("id")
	private int id;

	@SerializedName("payment_status")
	private int paymentStatus;

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	@SerializedName("email_verified_at")
	private String emailVerifiedAt;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	public int getId() {
		return id;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getEmailVerifiedAt() {
		return emailVerifiedAt;
	}

	public String getMobile() {
		return mobile;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}
}