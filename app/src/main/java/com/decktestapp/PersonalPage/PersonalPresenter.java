package com.decktestapp.PersonalPage;

import com.decktestapp.api.ApiInterface;
import com.decktestapp.api.ApiUtils;
import com.decktestapp.models.UserAlbumPhoto;
import com.decktestapp.models.UserAlbums;
import com.decktestapp.models.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalPresenter implements PersonalView.Presenter{

    private PersonalView.View view;

    private ArrayList<String> userAlbumsArrayList;
    private ArrayList<String> userPhotos;

    public PersonalPresenter(PersonalView.View view) {
        this.view = view;
    }

    @Override
    public void getUserAlbums(int user_id, String access_token) {
        userAlbumsArrayList = new ArrayList<>();
        userPhotos = new ArrayList<>();
        ApiInterface apiInterface;
        apiInterface = ApiUtils.getApi();
        Call<UserAlbums> call = apiInterface.getUserAlbums(access_token, user_id);
        call.enqueue(new Callback<UserAlbums>() {
            @Override
            public void onResponse(Call<UserAlbums> call, Response<UserAlbums> response) {
                UserAlbums userAlbums = response.body();
                int albums_count = userAlbums.getResponse().getCount();
                userAlbumsArrayList.clear();
                for (int i = 0; i < albums_count; i++) {
                    userAlbumsArrayList.add(userAlbums.getResponse().getItems().get(i).getTitle());
                }
                view.fillSpinner(userAlbumsArrayList);
                view.actionListeners(userAlbums);
            }

            @Override
            public void onFailure(Call<UserAlbums> call, Throwable t) {

            }
        });
    }

    @Override
    public void getUserInfo(int user_id) {
        ApiInterface apiInterface;
        apiInterface = ApiUtils.getApi();
        Call<UserInfo> call = apiInterface.getUserInfo(user_id);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                String firstName = userInfo.getResponse().get(0).getFirstName();
                String lastName = userInfo.getResponse().get(0).getLastName();
                view.showUserName(firstName, lastName);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public void getUserAlbumPhoto(String access_token, int user_id, int album_id) {
        ApiInterface apiInterface;
        apiInterface = ApiUtils.getApi();
        Call<UserAlbumPhoto> call = apiInterface.getUserAlbumPhoto(access_token, user_id, album_id, 20);
        call.enqueue(new Callback<UserAlbumPhoto>() {
            @Override
            public void onResponse(Call<UserAlbumPhoto> call, Response<UserAlbumPhoto> response) {
                UserAlbumPhoto userAlbumPhoto = response.body();
                int photos_count = userAlbumPhoto.getResponse().getCount();
                userPhotos.clear();
                for (int i = 0; i < photos_count; i++) {
                    userPhotos.add(userAlbumPhoto.getResponse().getItems().get(i).getPhoto604());
                }
                view.recyclerViewSetup(userPhotos);
            }

            @Override
            public void onFailure(Call<UserAlbumPhoto> call, Throwable t) {

            }
        });
    }
}
