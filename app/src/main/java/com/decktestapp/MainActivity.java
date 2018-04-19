package com.decktestapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.usb.UsbRequest;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.decktestapp.api.ApiInterface;
import com.decktestapp.api.ApiUtils;
import com.decktestapp.models.AccessToken;
import com.decktestapp.models.UserAlbums;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button enter;
    private TextInputEditText etLogin, etPass;
    private TextView tvError;
    private String login, pass, access_token;
    private int user_id;
    public static final String USER_ID = "com.decktestapp.USER_ID";
    public static final String ACCESS_TOKEN = "com.decktestapp.ACCESS_TOKEN";
    private String url = "https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enter = findViewById(R.id.enter);
        etLogin = findViewById(R.id.login);
        etPass = findViewById(R.id.pass);
        tvError = findViewById(R.id.tvError);
        buttonListeners();
    }

    private void buttonListeners() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface apiInterface;
                login = etLogin.getText().toString().replace(" ","");
                pass = etPass.getText().toString().replace(" ","");
                apiInterface = ApiUtils.getApi();
                Call<AccessToken> call = apiInterface.getToken(url, login, pass);
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        AccessToken accessToken = response.body();
                        if (accessToken == null) {
                            tvError.setVisibility(View.VISIBLE);
                            etPass.setText("");
                            return;
                        } else {
                            tvError.setVisibility(View.GONE);
                            user_id = accessToken.getUserId();
                            access_token = accessToken.getAccessToken();
                        }
                        Intent intent = new Intent(getApplicationContext(), PersonalPage.class);
                        intent.putExtra(USER_ID, user_id);
                        intent.putExtra(ACCESS_TOKEN, access_token);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.d("myLogs", "Failed!");
                    }
                });

            }
        });

    }
}
