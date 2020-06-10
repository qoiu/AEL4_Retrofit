package com.geekbrains.ael4_retrofit.dagger;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import dagger.Subcomponent;

@Subcomponent(modules = NetworkModule.class)
public interface NetworkComponent {
    ConnectivityManager getConnectivityManager();
    NetworkInfo getNetworkInfo();
}
