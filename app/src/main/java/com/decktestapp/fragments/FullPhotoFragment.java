package com.decktestapp.fragments;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.decktestapp.R;
import com.decktestapp.api.ApiInterface;
import com.decktestapp.api.ApiUtils;
import com.decktestapp.models.UserAlbumPhoto;
import com.decktestapp.utils.GlideApp;
import com.github.chrisbanes.photoview.PhotoView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullPhotoFragment extends Fragment {

    private View view;
    private PhotoView photoView;
    public static Bundle bundle = new Bundle();

    private String access_token;
    private int user_id, album_id, photo_index;


    public static FullPhotoFragment instance(String access_token, int user_id, int album_id, int position) {
        bundle.putString("access_token", access_token);
        bundle.putInt("user_id", user_id);
        bundle.putInt("album_id", album_id);
        bundle.putInt("photo_index", position);
        return new FullPhotoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fullphoto, container, false);
        access_token = bundle.getString("access_token");
        user_id = bundle.getInt("user_id");
        album_id = bundle.getInt("album_id");
        photo_index = bundle.getInt("photo_index");
        photoView = view.findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.ic_launcher_background);
        showFullPhoto();
        return view;
    }

    private void showFullPhoto() {
        ApiInterface apiInterface;
        apiInterface = ApiUtils.getApi();
        Call<UserAlbumPhoto> call = apiInterface.getUserAlbumPhoto(access_token, user_id, album_id, 20);
        call.enqueue(new Callback<UserAlbumPhoto>() {
            @Override
            public void onResponse(Call<UserAlbumPhoto> call, Response<UserAlbumPhoto> response) {
                UserAlbumPhoto userAlbumPhoto = response.body();
                String photo = userAlbumPhoto.getResponse().getItems().get(photo_index).getPhoto2560();
                GlideApp.with(getActivity().getApplication().getApplicationContext())
                        .load(photo)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(photoView);
                initScreenListener();
            }

            @Override
            public void onFailure(Call<UserAlbumPhoto> call, Throwable t) {

            }
        });
    }

    private void initScreenListener() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED );
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }
}
