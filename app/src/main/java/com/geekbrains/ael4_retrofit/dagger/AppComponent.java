package com.geekbrains.ael4_retrofit.dagger;


import com.geekbrains.ael4_retrofit.MainActivity;
import dagger.Component;

@Component(modules = {DaggerNetModules.class})
public interface AppComponent {
    void injectsToMainActivity(MainActivity activity);
    NetworkComponent getNetworkComponent(NetworkModule module);
    DBComponent getDBComponent(DaggerDBModules modules);
}
