package com.geekbrains.ael4_retrofit.dagger;


import android.os.Bundle;

import com.geekbrains.ael4_retrofit.MainActivity;
import com.geekbrains.ael4_retrofit.database.Database;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;
import com.geekbrains.ael4_retrofit.repo.Api;

import dagger.Component;
import io.reactivex.Single;

@Component(modules = {DaggerNetModules.class,DaggerDBModules.class})
public interface AppComponent {
    void injectsToMainActivity(MainActivity activity);
    boolean isConnected();
}
