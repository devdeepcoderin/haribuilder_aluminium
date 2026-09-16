package com.deepcoder.hbalum.datalayers.model;

import com.google.gson.annotations.SerializedName;

public class PurchaseModel {

	@SerializedName("order_tracking_id")
	private String orderTrackingId;

	@SerializedName("merchant_reference")
	private String merchantReference;

	@SerializedName("redirect_url")
	private String redirectUrl;

	@SerializedName("error")
	private String error;

	@SerializedName("status")
	private String status;

	public String getOrderTrackingId() {
		return orderTrackingId;
	}

	public String getMerchantReference() {
		return merchantReference;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getError() {
		return error;
	}

	public String getStatus() {
		return status;
	}
}