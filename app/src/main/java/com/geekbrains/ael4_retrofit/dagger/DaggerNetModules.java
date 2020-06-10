package com.geekbrains.ael4_retrofit.dagger;

import android.content.Context;
import android.net.ConnectivityManager;

import com.geekbrains.ael4_retrofit.MainActivity;
import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.MainPresenter;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;
import com.geekbrains.ael4_retrofit.repo.Api;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerNetModules {
    @Provides
    MainPresenterInterface presenterInterface(){return MainPresenter.get();}

    @Provides
    public String provideEndpoint(){
        return "https://api.github.com";
    }
    @Provides
    public Retrofit getRetrofit(String provider) {
        return new Retrofit.Builder()
                .baseUrl(provider)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Provides
    public Api getApi(Retrofit retrofit){
        return retrofit.create(Api.class);
    }

    @Provides
    Single<List<RepoUsers>> getRequest(Api api){
        return api.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
