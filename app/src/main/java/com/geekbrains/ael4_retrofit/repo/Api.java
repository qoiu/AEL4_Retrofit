package com.geekbrains.ael4_retrofit.repo;

import com.geekbrains.ael4_retrofit.model.RepoUserInfo;
import com.geekbrains.ael4_retrofit.model.RepoUsers;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    @GET("/users")
    Single<List<RepoUsers>> getUsers();

    @GET("/users/{path}")
    Single<RepoUsers> getUser(@Path("path") String path);

    @GET("/users/{path}/repos")
    Single<List<RepoUserInfo>> getUserRepos(@Path("path") String path);
}
