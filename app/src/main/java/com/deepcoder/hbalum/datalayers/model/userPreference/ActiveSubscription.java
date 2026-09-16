package com.deepcoder.hbalum.datalayers.model.userPreference;

import com.google.gson.annotations.SerializedName;

public class ActiveSubscription {
    @SerializedName("id")
    private int id;

    @SerializedName("subscription_id")
    private int subscriptionId;

    @SerializedName("expired_at")
    private String expiredAt="";

    @SerializedName("order_tracking_id")
    private String orderTrackingId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

	@SerializedName("user")
    private User user;


	@SerializedName("subscription")
	private Subscription subscription;

	public Subscription getSubscription() {
		return subscription;
	}

	public int getId() {
		return id;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public String getExpiredAt() {
		return expiredAt;
	}

	public String getOrderTrackingId() {
		return orderTrackingId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}
}