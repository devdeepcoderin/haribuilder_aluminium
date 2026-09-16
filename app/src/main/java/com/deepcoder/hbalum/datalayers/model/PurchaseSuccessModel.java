package com.deepcoder.hbalum.datalayers.model;

import com.google.gson.annotations.SerializedName;

public class PurchaseSuccessModel {

	@SerializedName("subscription_id")
	private String subscriptionId;

	@SerializedName("order_tracking_id")
	private String orderTtrackingIid;

	@SerializedName("expired_at")
	private String expiredAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("user_id")
	private int useId;

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getOrderTtrackingIid() {
		return orderTtrackingIid;
	}

	public void setOrderTtrackingIid(String orderTtrackingIid) {
		this.orderTtrackingIid = orderTtrackingIid;
	}

	public String getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(String expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getUseId() {
		return useId;
	}

	public void setUseId(int useId) {
		this.useId = useId;
	}
}