package com.geekbrains.ael4_retrofit.database;

import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmActions extends DatabaseClass {


    public RealmActions(MainPresenterInterface presenter, Action action) {
        super(presenter, action);
    }

    @Override
    public void dbSave() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (RepoUsers user : presenter.getUsersList()) {
            RealmUser realmUser = realm.createObject(RealmUser.class);
            realmUser.setUser(user.getUser());
            realmUser.setAvatarUrl(user.getImg_url());
        }
        realm.commitTransaction();
    }

    @Override
    public void dbUpdate() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmUser> list= realm.where(RealmUser.class).findAll();
        realm.beginTransaction();
        list.get(1).setUser("changename");
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void dbSelect() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmUser> list= realm.where(RealmUser.class).findAll();
        list.get(1).getUser();
        realm.close();
    }

    @Override
    public void dbDelete() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<RealmUser> list= realm.where(RealmUser.class).findAll();
        for (int i=list.size()-1;i>=0;i--){
            list.get(i).deleteFromRealm();
        }
        realm.commitTransaction();
    }

    @Override
    public long dbCount() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmUser> list= realm.where(RealmUser.class).findAll();
        realm.close();
        return list.size();
    }
}
