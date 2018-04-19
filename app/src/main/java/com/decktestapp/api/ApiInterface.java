package com.decktestapp.api;

import com.decktestapp.models.AccessToken;
import com.decktestapp.models.UserAlbumPhoto;
import com.decktestapp.models.UserAlbums;
import com.decktestapp.models.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<AccessToken> getToken(@Url String url,
                               @Query("username") String username,
                               @Query("password") String password);

    @GET("users.get?v=5.74")
    Call<UserInfo> getUserInfo(@Query("user_id") int user_id);

    @GET("photos.getAlbums?v=5.74")
    Call<UserAlbums> getUserAlbums(
            @Query("access_token") String access_token,
            @Query("owner_id") int user_id);

    @GET("photos.get?v=5.74")
    Call<UserAlbumPhoto> getUserAlbumPhoto(
            @Query("access_token") String access_token,
            @Query("owner_id") int user_id,
            @Query("album_id") int album_id,
            @Query("count") int count);
}