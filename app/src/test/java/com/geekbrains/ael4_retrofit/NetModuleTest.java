package com.geekbrains.ael4_retrofit;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telecom.Call;
import android.util.Log;

import com.geekbrains.ael4_retrofit.dagger.DaggerNetModules;
import com.geekbrains.ael4_retrofit.dagger.NetworkModule;
import com.geekbrains.ael4_retrofit.model.RepoUserInfo;
import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.repo.Api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NetModuleTest {
    @Test
    public void RetrofitCorrect(){
        Activity activity= Mockito.mock(Activity.class);
        //ConnectivityManager cm=Mockito.mock(ConnectivityManager.class);
        //Mockito.when(activity.getSystemService(Mockito.anyString())).thenReturn(cm);
        DaggerNetModules module=new DaggerNetModules();
        Api api=module.getRetrofit().create(Api.class);
        api.getUsers().subscribeWith(new DisposableSingleObserver<List<RepoUsers>>() {
            @Override
            public void onSuccess(List<RepoUsers> repoUsers) {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("test",e.getMessage());

            }
        });

        //Api api=module.getApi(module.getRetrofit());
        //api.getUser("mojombo").subscribeOn();
        //NetworkModule module=Mockito.mock(NetworkModule.class);
       /* NetworkInfo info=module.getNetworkInfo();
        Mockito.verify(cm).getActiveNetworkInfo();
        assertEquals(info,module.getNetworkInfo();
        */
    }
}
