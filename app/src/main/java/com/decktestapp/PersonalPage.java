package com.decktestapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.decktestapp.adapters.UserPhotosAdapter;
import com.decktestapp.api.ApiInterface;
import com.decktestapp.api.ApiUtils;
import com.decktestapp.fragments.FullPhotoFragment;
import com.decktestapp.models.UserAlbumPhoto;
import com.decktestapp.models.UserAlbums;
import com.decktestapp.models.UserInfo;
import com.decktestapp.utils.ItemClickSupport;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalPage extends AppCompatActivity {

    private TextView username;
    private String access_token;
    private int user_id, album_id;

    private ArrayList<String> userAlbumsArrayList;
    private ArrayList<String> userPhotos;

    private Spinner spinnerAlbums;
    private RecyclerView recyclerView;

    private UserPhotosAdapter userPhotosAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        username = findViewById(R.id.username);
        spinnerAlbums = findViewById(R.id.spinnerAlbums);
        recyclerView = findViewById(R.id.items);
        userAlbumsArrayList = new ArrayList<>();
        userPhotos = new ArrayList<>();
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            user_id = intent.getExtras().getInt(MainActivity.USER_ID);
            access_token = intent.getExtras().getString(MainActivity.ACCESS_TOKEN);
            initUserPage(user_id);
            initUi(user_id, access_token);
        }
    }

    private void initUi(int user_id, String access_token) {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout_dropdown, userAlbumsArrayList);
                adapter.setDropDownViewResource(R.layout.spinner_layout_dropdown);
                spinnerAlbums.setAdapter(adapter);
                actionListeners(userAlbums);
            }

            @Override
            public void onFailure(Call<UserAlbums> call, Throwable t) {

            }
        });
    }

    private void initUserPage(int user_id) {
        ApiInterface apiInterface;
        apiInterface = ApiUtils.getApi();
        Call<UserInfo> call = apiInterface.getUserInfo(user_id);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                String firstName = userInfo.getResponse().get(0).getFirstName();
                String lastName = userInfo.getResponse().get(0).getLastName();
                username.setText(firstName.concat(" ").concat(lastName));
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    private void actionListeners(final UserAlbums userAlbums) {
        spinnerAlbums.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                album_id = userAlbums.getResponse().getItems().get(position).getId();
                loadUserAlbumPhoto(album_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadUserAlbumPhoto(int album_id) {
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
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                userPhotosAdapter = new UserPhotosAdapter(getApplicationContext(), userPhotos);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(userPhotosAdapter);
                showFragment(access_token, user_id, album_id);
            }

            @Override
            public void onFailure(Call<UserAlbumPhoto> call, Throwable t) {

            }
        });
    }

    private void showFragment(String access_token, int user_id, int album_id) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            Log.d("myLogs", "position: "+position);
            FullPhotoFragment fullPhotoFragment = FullPhotoFragment.instance(access_token, user_id, album_id, position);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack("fullPhotoFragment");
            transaction.replace(R.id.container, fullPhotoFragment)
                    .commit();
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}
