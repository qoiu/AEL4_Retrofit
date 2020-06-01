package com.geekbrains.ael4_retrofit.presenters;

import com.geekbrains.ael4_retrofit.MainActivityInterface;
import com.geekbrains.ael4_retrofit.model.RepoUsers;

import java.util.List;

public interface MainPresenterInterface {
    void requestUserList();
    void requestUserRepoList(String name);
    List<RepoUsers> getUsersList();
    RepoUsers getRepoUser(String name);
    void addUser(String name);
    void bindView(MainActivityInterface activity);
}
