package com.geekbrains.ael4_retrofit.model;

import com.google.gson.annotations.SerializedName;

public class RepoUserInfo {
    @SerializedName("name")private String repos;
    @SerializedName("description")private String description;
    @SerializedName("html_url")private String url;

    public String getRepos() {
        return repos;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
