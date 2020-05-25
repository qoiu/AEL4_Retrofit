package com.geekbrains.ael4_retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepoUsers {

    @SerializedName("login")private String user;
    @SerializedName("avatar_url")private String img_url;
    private List<RepoUserInfo> repository;

    RepoUsers(String user, String img_url) {
        this.user = user;
        this.img_url = img_url;
    }

    public RepoUserInfo getRepository(int id) {
        return repository.get(id);
    }

    public List<RepoUserInfo> getRepositoryList() {
        return repository;
    }

    void setRepository(List<RepoUserInfo> repository) {
        this.repository = repository;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
