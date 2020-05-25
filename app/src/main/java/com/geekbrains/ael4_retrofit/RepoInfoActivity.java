package com.geekbrains.ael4_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.RepoPresenter;
import com.squareup.picasso.Picasso;

public class RepoInfoActivity extends AppCompatActivity implements RepoActivityInterface{

    private RecyclerRepoAdapter repoAdapter;
    RepoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_info);
        Intent intent=getIntent();
        RecyclerView recyclerView=findViewById(R.id.repos_recycler);
        presenter=new RepoPresenter(intent.getStringExtra("name"));
        presenter.bindView(this);
        repoAdapter = new RecyclerRepoAdapter(presenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(repoAdapter);
    }

    @Override
    public void setHeader(String name, String url) {
        ((TextView)findViewById(R.id.repos_user_name)).setText(name);
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into((ImageView)findViewById(R.id.repos_user_img));
    }

    @Override
    public void startBrowser(String url) {
        Uri address = Uri.parse(url);
        Intent linkInet = new Intent(Intent.ACTION_VIEW, address);
        startActivity(linkInet);

    }
}
