package com.sunmi.pocketvendor.network;

public class Response {

    private int statusCode;
    private String result;
    private String failedCode;

    public boolean isError = false;

    public Response(int statusCode, String result) {
        this.statusCode = statusCode;
        this.result = result;
    }


    public String getFailedCode() {
        return failedCode;
    }

    public void setFailedCode(String failedCode) {
        this.failedCode = failedCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
