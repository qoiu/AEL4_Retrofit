package com.geekbrains.ael4_retrofit.database;

import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.orm.SugarRecord;


public class SugarModel extends SugarRecord {
    private String user;
    private String avatarUrl;

    public SugarModel() {
    }

    public SugarModel(RepoUsers users){
        this.user=users.getUser();
        this.avatarUrl=users.getImg_url();
    }

    public SugarModel(String user, String avatarUrl) {
        this.user = user;
        this.avatarUrl = avatarUrl;
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
}
