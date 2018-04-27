package com.decktestapp.MainPage;

import android.util.Log;

import com.decktestapp.api.ApiInterface;
import com.decktestapp.api.ApiUtils;
import com.decktestapp.models.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthPresenter implements AuthView.Presenter {

    private AuthView.View view;

    private String access_token;
    private int user_id;

    public AuthPresenter(AuthView.View view) {
        this.view = view;
    }

    @Override
    public void logIn(String url, String login, String pass) {
        ApiInterface apiInterface;
        apiInterface = ApiUtils.getApi();
        Call<AccessToken> call = apiInterface.getToken(url, login, pass);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken accessToken = response.body();
                if (accessToken == null) {
                    view.showError();
                    return;
                } else {
                    view.hideError();
                    user_id = accessToken.getUserId();
                    access_token = accessToken.getAccessToken();
                }
                view.startPersonalPage(user_id, access_token);
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.d("myLogs", "Failed!");
            }
        });
    }
}
