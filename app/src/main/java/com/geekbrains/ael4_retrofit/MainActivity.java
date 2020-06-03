package com.geekbrains.ael4_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geekbrains.ael4_retrofit.database.DBHelperAction;
import com.geekbrains.ael4_retrofit.database.DBHeper;
import com.geekbrains.ael4_retrofit.database.Database;
import com.geekbrains.ael4_retrofit.database.RealmActions;
import com.geekbrains.ael4_retrofit.database.RealmUser;
import com.geekbrains.ael4_retrofit.database.SugarModelActions;
import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.MainPresenter;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;
import com.orm.SugarContext;

import java.util.Date;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private EditText editText;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MainPresenterInterface presenter;
    private RecyclerUserAdapter recyclerDataAdapter;
    private TextView sugarRes, sqliteRes, realmRes;
    private SQLiteDatabase db;



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
        intent.putExtra("name", name);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(getApplicationContext());
        presenter = MainPresenter.get();
        presenter.bindView(this);
        DBHeper sqlite = new DBHeper(this);
        db= sqlite.getWritableDatabase();
        Realm.init(getApplicationContext());

        if (isConnected()) {
            presenter.requestUserList();
        } else {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
        }
        initView();
        setRecyclerView();
        btnInit();
    }

    private void initView() {
        sugarRes = findViewById(R.id.sugar_result);
        sqliteRes = findViewById(R.id.sqlite_result);
        realmRes = findViewById(R.id.realm_result);
        editText = findViewById(R.id.edit_query);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Button btnLoad = findViewById(R.id.btnApply);
        btnLoad.setOnClickListener((v) -> {
            presenter.addUser(editText.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
        });
    }


    private void btnInit() {
        Button sugarSave = (Button) findViewById(R.id.sugar_btn_save);
        Button sugarDelete = (Button) findViewById(R.id.sugar_btn_delete);
        Button sugarUpdate = (Button) findViewById(R.id.sugar_btn_update);
        Button sqliteSave = (Button) findViewById(R.id.sqlite_btn_save);
        Button sqliteDelete = (Button) findViewById(R.id.sqlite_btn_delete);
        Button sqliteUpdate = (Button) findViewById(R.id.sqlite_btn_update);
        Button realmSave = (Button) findViewById(R.id.realm_btn_save);
        Button realmDelete = (Button) findViewById(R.id.realm_btn_delete);
        Button realmUpdate = (Button) findViewById(R.id.realm_btn_update);
        Button realmAsync = (Button) findViewById(R.id.realm_btn_async);
        sugarSave.setOnClickListener((v) ->
                actionSingle(new SugarModelActions(presenter,Database.Action.SAVE))
                .subscribeWith(createObserver(sugarRes)));
        sugarDelete.setOnClickListener((v)->
                actionSingle(new SugarModelActions(presenter,Database.Action.DELETE))
                .subscribeWith(createObserver(sugarRes)));
        sugarUpdate.setOnClickListener((v)->
                actionSingle(new SugarModelActions(presenter,Database.Action.UPDATE))
                .subscribeWith(createObserver(sugarRes)));
        sqliteSave.setOnClickListener((v) ->
                actionSingle(new DBHelperAction(presenter,Database.Action.SAVE,db))
                        .subscribeWith(createObserver(sqliteRes)));
        sqliteDelete.setOnClickListener((v)->
                actionSingle(new DBHelperAction(presenter,Database.Action.DELETE,db))
                        .subscribeWith(createObserver(sqliteRes)));
        sqliteUpdate.setOnClickListener((v)->
                actionSingle(new DBHelperAction(presenter,Database.Action.UPDATE,db))
                        .subscribeWith(createObserver(sqliteRes)));
        realmUpdate.setOnClickListener((v)->
                actionSingle(new RealmActions(presenter,Database.Action.UPDATE))
                        .subscribeWith(createObserver(realmRes)));
        realmSave.setOnClickListener((v) ->
                actionSingle(new RealmActions(presenter,Database.Action.SAVE))
                       .subscribeWith(createObserver(realmRes)));
        realmDelete.setOnClickListener((v)->
                actionSingle(new RealmActions(presenter,Database.Action.DELETE))
                        .subscribeWith(createObserver(realmRes)));
        realmAsync.setOnClickListener((v)->asyncRealm());

    }

    private void asyncRealm(){
        Bundle bundle = new Bundle();
        Realm realm1=Realm.getDefaultInstance();
            realm1.executeTransactionAsync(realm -> {
                try {

                    Date first = new Date();
                    for (RepoUsers user : presenter.getUsersList()) {
                        RealmUser realmUser = realm.createObject(RealmUser.class);
                        realmUser.setUser(user.getUser());
                        realmUser.setAvatarUrl(user.getImg_url());
                    }
                    RealmResults<RealmUser> list= realm.where(RealmUser.class).findAll();
                    Date second = new Date();

                    bundle.putLong("count", list.size());
                    bundle.putLong("msek", second.getTime() - first.getTime());
                } catch (Exception e) {
                    Log.e("save",e.getMessage());
                }
            }, () -> realmRes.setText("Количество = " + bundle.getLong("count") +
                    "\n милисекунд = " + bundle.getLong("msek")));
            realm1.close();
    }

    private Single<Bundle> actionSingle(Database db){
        return Single.create((SingleOnSubscribe<Bundle>) emitter -> {
            try {
                Date first = new Date();
                db.action();
                Date second = new Date();
                Bundle bundle = new Bundle();
                bundle.putLong("count", db.dbCount());
                bundle.putLong("msek", second.getTime() - first.getTime());
                emitter.onSuccess(bundle);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<Bundle> createObserver(TextView textView) {
        return new DisposableSingleObserver<Bundle>() {
            @Override
            protected void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Bundle bundle) {
                progressBar.setVisibility(View.GONE);
                textView.setText("Количество = " + bundle.getLong("count") +
                        "\n милисекунд = " + bundle.getLong("msek"));
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.GONE);
                textView.setText("Error: " + e.getLocalizedMessage());
                Toast.makeText(getBaseContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                Log.e("save",e.getLocalizedMessage());
                Log.e("save",e.getMessage());
            }
        };
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
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }
}
