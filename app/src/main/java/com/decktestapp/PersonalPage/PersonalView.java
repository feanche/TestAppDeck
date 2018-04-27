package com.decktestapp.PersonalPage;

import com.decktestapp.models.UserAlbums;
import com.decktestapp.utils.BaseView;

import java.util.ArrayList;

public interface PersonalView {

    interface View extends BaseView {
        void showUserName(String firstName, String lastName);
        void fillSpinner(ArrayList<String> userAlbumsArrayList);
        void actionListeners(UserAlbums userAlbums);
        void recyclerViewSetup(ArrayList<String> userPhotos);
    }

    interface Presenter {
        void getUserInfo(int user_id);
        void getUserAlbums(int user_id, String access_token);
        void getUserAlbumPhoto(String access_token, int user_id, int album_id);
    }

}
