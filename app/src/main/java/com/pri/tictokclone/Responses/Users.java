package com.pri.tictokclone.Responses;

import com.google.gson.annotations.SerializedName;
import com.pri.tictokclone.Models.MediaObject;
import com.pri.tictokclone.Models.SoundModel;

import java.util.ArrayList;
import java.util.List;

public class Users {

    @SerializedName("ALL_POSTS")
    private ArrayList<MediaObject> AllPosts;

    @SerializedName("ALL_SOUNDS")
    private ArrayList<SoundModel> AllSounds;

    public ArrayList<MediaObject> getAllPosts(){
        return AllPosts;
    }

    public ArrayList<SoundModel> getAllSounds() {
        return AllSounds;
    }
}
