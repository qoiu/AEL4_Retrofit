package com.geekbrains.ael4_retrofit.presenters;

import com.geekbrains.ael4_retrofit.MainActivityInterface;
import com.geekbrains.ael4_retrofit.model.Model;
import com.geekbrains.ael4_retrofit.model.RepoUserInfo;
import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.repo.Api;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainPresenter implements MainPresenterInterface {
    private MainActivityInterface activity;
    private Model model;
    private static MainPresenterInterface presenter;
    private Disposable disposable;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build();

    public void bindView(MainActivityInterface activity) {
        this.activity = activity;
    }

    private MainPresenter() {
        model = new Model();
    }

    public static MainPresenterInterface get() {
        if (presenter == null) presenter = new MainPresenter();
        return presenter;
    }

    @Override
    public void requestUserList() {
        Single<List<RepoUsers>> single = retrofit.create(Api.class).getUsers();
        disposable = single.retry(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<RepoUsers>>() {
            @Override
            public void onSuccess(List<RepoUsers> repoUsers) {
                model.setUsersList(repoUsers);
                activity.updateUsers();
            }

            @Override
            public void onError(Throwable e) {
                activity.toastMsg("We have some problems: \n" + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void addUser(String name) {
        Single<RepoUsers> single = retrofit.create(Api.class).getUser(name);
        disposable = single.retry(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<RepoUsers>() {
            @Override
            public void onSuccess(RepoUsers repoUsers) {
                model.addUser(repoUsers);
                activity.updateUsers();
            }

            @Override
            public void onError(Throwable e) {
                activity.toastMsg("We have some problems: \n" + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void requestUserRepoList(String name) {
        Single<List<RepoUserInfo>> single = retrofit.create(Api.class).getUserRepos(name);
        disposable = single.retry(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<RepoUserInfo>>() {
            @Override
            public void onSuccess(List<RepoUserInfo> repoUsersInfo) {
                if (model.addUserRepos(repoUsersInfo, name)) {
                    activity.showUserRepos(name);
                } else {
                    activity.toastMsg(name + " - not found");
                }
            }

            @Override
            public void onError(Throwable e) {
                activity.toastMsg("We have some problems: \n" + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public List<RepoUsers> getUsersList() {
        return model.getUsersList();
    }

    @Override
    public RepoUsers getRepoUser(String name) {
        return model.getUser(name);
    }

}

