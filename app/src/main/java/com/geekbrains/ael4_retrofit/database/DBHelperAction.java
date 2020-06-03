package com.geekbrains.ael4_retrofit.database;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;

public class DBHelperAction extends DatabaseClass {
    private SQLiteDatabase db;
    public DBHelperAction(MainPresenterInterface presenter, Action action,SQLiteDatabase db) {
        super(presenter, action);
        this.db=db;
    }

    @Override
    public void dbSave() {
        ContentValues cv=new ContentValues();
        for (RepoUsers user : presenter.getUsersList()) {
            cv.put("login",user.getUser());
            cv.put("url",user.getImg_url());
            db.insert("users",null,cv);
        }
    }

    @Override
    public void dbUpdate() {
        ContentValues cv=new ContentValues();
        for (RepoUsers user : presenter.getUsersList()) {
            cv.put("login",user.getUser());
            cv.put("url",user.getImg_url());
            db.update("users",cv,"login=?",new String[]{user.getUser()});
        }
    }

    @Override
    public void dbSelect() {

    }

    @Override
    public void dbDelete() {
        db.delete("users",null,null);
    }

    @Override
    public long dbCount() {
        return DatabaseUtils.queryNumEntries(db,"users");
    }
}
