package com.decktestapp.MainPage;

import com.decktestapp.utils.BaseView;

public interface AuthView {

    interface View extends BaseView {
        void showError();
        void hideError();
        void startPersonalPage(int user_id, String access_token);
    }

    interface Presenter {
        void logIn(String url, String login, String pass);
    }

}
