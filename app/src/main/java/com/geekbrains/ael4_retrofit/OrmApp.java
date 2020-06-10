package com.geekbrains.ael4_retrofit;

import android.app.Activity;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.geekbrains.ael4_retrofit.dagger.AppComponent;
import com.geekbrains.ael4_retrofit.dagger.DBComponent;
import com.geekbrains.ael4_retrofit.dagger.DaggerAppComponent;
import com.geekbrains.ael4_retrofit.dagger.DaggerDBModules;
import com.geekbrains.ael4_retrofit.dagger.NetworkComponent;
import com.geekbrains.ael4_retrofit.dagger.NetworkModule;

import java.util.List;

public class OrmApp extends Application {
    private static OrmApp instance;
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        component= DaggerAppComponent.create();
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }

    public static AppComponent getComponent(){
        return component;
    }

    public static OrmApp get(){return instance;}

    public static DBComponent getDB(){
        return component.getDBComponent(new DaggerDBModules());
    }

    public static NetworkComponent getNetworkComponent(Activity activity){
        return component.getNetworkComponent(new NetworkModule(activity));
    }



}
