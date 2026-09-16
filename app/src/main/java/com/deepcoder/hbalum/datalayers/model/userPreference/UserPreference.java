package com.deepcoder.hbalum.datalayers.model.userPreference;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPreference {

	@SerializedName("is_trial")
	private Boolean isTrial;

	@SerializedName("is_payment_verified")
	private Boolean isPaymentVerified;

	@SerializedName("days_used")
	private int dayUsed;

	@SerializedName("is_applied")
	private boolean isApplied;

	@SerializedName("trial_limit")
	private int trialLimit;

	@SerializedName("payment_status")
	private int paymentStatus;

	@SerializedName("active_subscription")
	private ActiveSubscription activeSubscription;


	public ActiveSubscription getActiveSubscription() {
		return activeSubscription;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public Boolean getTrial() {
		return isTrial;
	}

	public void setTrial(Boolean trial) {
		isTrial = trial;
	}

	public Boolean getPaymentVerified() {
		return isPaymentVerified;
	}

	public void setPaymentVerified(Boolean paymentVerified) {
		isPaymentVerified = paymentVerified;
	}

	public int getDayUsed() {
		return dayUsed;
	}

	public void setDayUsed(int dayUsed) {
		this.dayUsed = dayUsed;
	}

	public boolean isApplied() {
		return isApplied;
	}

	public void setApplied(boolean applied) {
		isApplied = applied;
	}

	public int getTrialLimit() {
		return trialLimit;
	}

	public void setTrialLimit(int trialLimit) {
		this.trialLimit = trialLimit;
	}
}