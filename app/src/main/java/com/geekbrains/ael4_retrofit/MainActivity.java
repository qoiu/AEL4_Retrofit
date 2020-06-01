package com.geekbrains.ael4_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.geekbrains.ael4_retrofit.presenters.MainPresenter;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private EditText editText;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MainPresenterInterface presenter;
    private RecyclerUserAdapter recyclerDataAdapter;

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUsers() {
        progressBar.setVisibility(View.GONE);
        recyclerDataAdapter.update();
    }

    @Override
    public void showUserRepos(String name) {
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), RepoInfoActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit_query);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Button btnLoad = (Button) findViewById(R.id.btnApply);
        presenter= MainPresenter.get();
        presenter.bindView(this);
        if (isConnected()) {
            presenter.requestUserList();
        } else {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
        }
        btnLoad.setOnClickListener((v) -> {
            presenter.addUser(editText.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
        });
        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerDataAdapter = new RecyclerUserAdapter(presenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerDataAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerDataAdapter);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < 28) {
            assert cm != null;
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) return true;
                return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
            return false;
        } else {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
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

    @Override
    protected void onPause() {
       /* if (!disposable.isDisposed()) {
            disposable.dispose();
            progressBar.setVisibility(View.GONE);
        }*/
        super.onPause();
    }
}
