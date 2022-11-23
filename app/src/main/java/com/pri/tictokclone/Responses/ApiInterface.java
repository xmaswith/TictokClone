package com.pri.tictokclone.Responses;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    ///getting all posts///
    @GET("/MediaObject.json")
    Call<Users> performAllPosts();

    ///getting all sounds///
    @GET("/SoundModel.json")
    Call<Users> performAllSounds();
}
