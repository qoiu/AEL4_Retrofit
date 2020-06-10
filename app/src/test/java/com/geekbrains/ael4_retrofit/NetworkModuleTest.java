package com.geekbrains.ael4_retrofit;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.geekbrains.ael4_retrofit.dagger.NetworkModule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NetworkModuleTest {
    @Test
    public void getNetworkInfo_null(){
        NetworkModule module=new NetworkModule(Mockito.mock(Activity.class));
        assertEquals(null,module.getNetworkInfo());
    }

    @Test
    public void getNetworkInfoCorrect(){
        Activity activity=Mockito.mock(Activity.class);
        ConnectivityManager cm=Mockito.mock(ConnectivityManager.class);
        Mockito.when(activity.getSystemService(Mockito.anyString())).thenReturn(cm);
        NetworkModule module=new NetworkModule(activity);
        //NetworkModule module=Mockito.mock(NetworkModule.class);
        NetworkInfo info=module.getNetworkInfo();
        Mockito.verify(cm).getActiveNetworkInfo();
        assertEquals(info,module.getNetworkInfo());
    }
}
