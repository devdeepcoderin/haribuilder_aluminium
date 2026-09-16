package com.deepcoder.hbalum.datalayers.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("isError")
    @Expose
    private boolean isError;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("response")
    @Expose
    private T response;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}