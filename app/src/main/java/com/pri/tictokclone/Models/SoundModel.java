package com.pri.tictokclone.Models;

public class SoundModel {
    private String sound_img;
    private String sound_file;
    private String sound_title;

    public SoundModel() {
    }

    public SoundModel(String sound_img, String sound_file, String sound_title) {
        this.sound_img = sound_img;
        this.sound_file = sound_file;
        this.sound_title = sound_title;
    }

    public String getSound_img() {
        return sound_img;
    }

    public void setSound_img(String sound_img) {
        this.sound_img = sound_img;
    }

    public String getSound_file() {
        return sound_file;
    }

    public void setSound_file(String sound_file) {
        this.sound_file = sound_file;
    }

    public String getSound_title() {
        return sound_title;
    }

    public void setSound_title(String sound_title) {
        this.sound_title = sound_title;
    }
}
