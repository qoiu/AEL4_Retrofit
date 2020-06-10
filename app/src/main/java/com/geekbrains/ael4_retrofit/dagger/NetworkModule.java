package com.geekbrains.ael4_retrofit.dagger;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {
    private final Activity activity;

    public NetworkModule(Activity activity) {
        this.activity = activity;
    }


    @Provides
    public ConnectivityManager getConnectivityManager(){
        return (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    public NetworkInfo getNetworkInfo(){
        return (getConnectivityManager()!=null)?getConnectivityManager().getActiveNetworkInfo():null;
    }
}
