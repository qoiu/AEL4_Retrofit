package com.geekbrains.ael4_retrofit.database;

import com.geekbrains.ael4_retrofit.model.RepoUsers;

import io.realm.RealmObject;

public class RealmUser extends RealmObject {

    private String user;
    private String avatarUrl;

    public RealmUser() { }

    public RealmUser(RepoUsers users){
        user=users.getUser();
        avatarUrl=users.getImg_url();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public RealmUser(String user, String avatarUrl) {
        this.user = user;
        this.avatarUrl = avatarUrl;


    }
}
