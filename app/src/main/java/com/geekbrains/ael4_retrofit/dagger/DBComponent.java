package com.geekbrains.ael4_retrofit.dagger;

import android.os.Bundle;

import dagger.Subcomponent;
import io.reactivex.Single;

@Subcomponent(modules = DaggerDBModules.class)
public interface DBComponent {
    Single<Bundle> sugarSave();
}
