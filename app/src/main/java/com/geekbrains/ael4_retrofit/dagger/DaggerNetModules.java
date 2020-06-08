package com.geekbrains.ael4_retrofit.dagger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.geekbrains.ael4_retrofit.MainActivity;
import com.geekbrains.ael4_retrofit.database.Database;
import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.repo.Api;

import java.util.Date;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public
class DaggerNetModules {

    private final MainActivity activity;

    public DaggerNetModules(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Provides
    Api getApi(Retrofit retrofit){
        return retrofit.create(Api.class);
    }

    @Provides
    ConnectivityManager getCM(MainActivity activity){
        return (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    boolean isConnected() {
        if (Build.VERSION.SDK_INT < 28) {
            assert getCM(activity) != null;
            NetworkInfo networkInfo = getCM(activity) .getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) return true;
                return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
            return false;
        } else {
            if (getCM(activity)  != null) {
                NetworkCapabilities capabilities = getCM(activity) .getNetworkCapabilities(getCM(activity) .getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }


/*
    @Provides
    Single<List<RepoUsers>> getUsers(){
        return getApi(getRetrofit()).create(Api.class).getUser(name);
    }

   /* boolean isConnected(){

    }*/
}
