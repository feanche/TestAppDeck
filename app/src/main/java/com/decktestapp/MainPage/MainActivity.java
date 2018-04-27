package com.decktestapp.MainPage;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.decktestapp.PersonalPage.PersonalPage;
import com.decktestapp.R;

public class MainActivity extends AppCompatActivity implements AuthView.View {

    private Button enter;
    private TextInputEditText etLogin, etPass;
    private TextView tvError;
    private String login, pass;
    public static final String USER_ID = "com.decktestapp.USER_ID";
    public static final String ACCESS_TOKEN = "com.decktestapp.ACCESS_TOKEN";
    private String url = "https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH";
    private AuthView.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new AuthPresenter(this);

        enter = findViewById(R.id.enter);
        etLogin = findViewById(R.id.login);
        etPass = findViewById(R.id.pass);
        tvError = findViewById(R.id.tvError);
        buttonListeners();
    }

    private void buttonListeners() {
        enter.setOnClickListener(v -> {
            login = etLogin.getText().toString().replace(" ","");
            pass = etPass.getText().toString().replace(" ","");
            presenter.logIn(url, login, pass);
        });
    }

    @Override
    public void showError() {
        tvError.setVisibility(View.VISIBLE);
        etPass.setText("");
    }

    @Override
    public void hideError() {
        tvError.setVisibility(View.GONE);
    }

    @Override
    public void startPersonalPage(int user_id, String access_token) {
        Intent intent = new Intent(getApplicationContext(), PersonalPage.class);
        intent.putExtra(USER_ID, user_id);
        intent.putExtra(ACCESS_TOKEN, access_token);
        startActivity(intent);
    }
}
