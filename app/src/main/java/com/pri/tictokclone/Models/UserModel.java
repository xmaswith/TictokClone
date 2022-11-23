package com.pri.tictokclone.Models;

public class UserModel {
    private String userid;
    private String username;
    private String password;

    public UserModel(String userid, String username, String password) {
        this.userid = userid;
        this.username = username;
        this.password = password;
    }

    public UserModel() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
