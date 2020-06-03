package com.geekbrains.ael4_retrofit.dagger;

import android.os.Bundle;

import com.geekbrains.ael4_retrofit.MainActivity;
import com.geekbrains.ael4_retrofit.database.Database;
import com.geekbrains.ael4_retrofit.database.SugarModel;
import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.MainPresenter;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;
import com.orm.SugarContext;

import java.util.Date;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

@Module
public class DaggerDBModules {

    @Provides
    MainPresenterInterface getPresenter(){
        return MainPresenter.get();
    }

    @Provides
    Single<Bundle> actionSingle() {
        return Single.create((SingleOnSubscribe<Bundle>) emitter -> {
            try {
                Date first = new Date();
                for (RepoUsers user : getPresenter().getUsersList()) {
                    SugarModel sugarModel = new SugarModel(user);
                    sugarModel.save();
                }
                Date second = new Date();
                Bundle bundle = new Bundle();
                bundle.putLong("count", SugarModel.listAll(SugarModel.class).size());
                bundle.putLong("msek", second.getTime() - first.getTime());
                emitter.onSuccess(bundle);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
