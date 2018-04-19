package com.decktestapp.api;

public class ApiUtils {
    public static final String BASE_URL_AUTH = "https://api.vk.com/method/";

    public static ApiInterface getApi() {
        return RetrofitClient.getClient(BASE_URL_AUTH).create(ApiInterface.class);

    }

}
