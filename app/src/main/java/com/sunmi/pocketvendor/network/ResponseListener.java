package com.sunmi.pocketvendor.network;

public interface ResponseListener {
    void onResponse(Response response);
    void onError(String error);
}
