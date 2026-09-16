package com.deepcoder.hbalum.datalayers.retrofit;

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
        message = "";
        statusCode = 0;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}