package com.decktestapp.PersonalPage;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.decktestapp.MainPage.MainActivity;
import com.decktestapp.R;
import com.decktestapp.adapters.UserPhotosAdapter;
import com.decktestapp.fragments.FullPhotoFragment;
import com.decktestapp.models.UserAlbums;
import com.decktestapp.utils.ItemClickSupport;

import java.util.ArrayList;

public class PersonalPage extends AppCompatActivity implements PersonalView.View{

    private TextView username;
    private String access_token;
    private int user_id, album_id;

    private Spinner spinnerAlbums;
    private RecyclerView recyclerView;
    private UserPhotosAdapter userPhotosAdapter;

    private PersonalView.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        presenter = new PersonalPresenter(this);
        username = findViewById(R.id.username);
        spinnerAlbums = findViewById(R.id.spinnerAlbums);
        recyclerView = findViewById(R.id.items);

        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            user_id = intent.getExtras().getInt(MainActivity.USER_ID);
            access_token = intent.getExtras().getString(MainActivity.ACCESS_TOKEN);
            presenter.getUserInfo(user_id);
            presenter.getUserAlbums(user_id, access_token);
        }
    }

    @Override
    public void showUserName(String firstName, String lastName) {
        username.setText(firstName.concat(" ").concat(lastName));
    }

    @Override
    public void fillSpinner(ArrayList<String> userAlbumsArrayList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout_dropdown, userAlbumsArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_layout_dropdown);
        spinnerAlbums.setAdapter(adapter);
    }


    @Override
    public void actionListeners(UserAlbums userAlbums) {
        spinnerAlbums.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                album_id = userAlbums.getResponse().getItems().get(position).getId();
                presenter.getUserAlbumPhoto(access_token, user_id, album_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void recyclerViewSetup(ArrayList<String> userPhotos) {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        userPhotosAdapter = new UserPhotosAdapter(getApplicationContext(), userPhotos);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(userPhotosAdapter);
        showFragment(access_token, user_id, album_id);
    }

    private void showFragment(String access_token, int user_id, int album_id) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
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
