package com.geekbrains.ael4_retrofit.presenters;

import com.geekbrains.ael4_retrofit.MainActivityInterface;
import com.geekbrains.ael4_retrofit.RepoActivityInterface;
import com.geekbrains.ael4_retrofit.model.RepoUsers;

public class RepoPresenter{



    RepoActivityInterface activity;

    public void bindView(RepoActivityInterface activity){
        this.activity=activity;
        activity.setHeader(user.getUser(),user.getImg_url());
    }

    RepoUsers user;

    public RepoPresenter(String name) {
        user=MainPresenter.get().getRepoUser(name);
    }

    public RepoUsers getUser(){
        return user;
    }

    public void startUrl(String url){
        activity.startBrowser(url);
    }
}
