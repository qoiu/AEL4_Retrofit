package com.geekbrains.ael4_retrofit.database;

import android.util.Log;

import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;

import java.util.List;

public class SugarModelActions extends DatabaseClass {

    public SugarModelActions(MainPresenterInterface presenter, Action action) {
        super(presenter, action);
    }

    @Override
    public void dbSave() {
        for (RepoUsers user : presenter.getUsersList()) {
            SugarModel sugarModel = new SugarModel(user);
            sugarModel.save();
        }
    }

    @Override
    public void dbUpdate() {
        for (SugarModel sugar:SugarModel.listAll(SugarModel.class) ) {
            sugar.update();
        }
    }

    @Override
    public void dbSelect() {
        List<SugarModel> sugarModel=SugarModel.listAll(SugarModel.class);
        Log.e("save",sugarModel.get(1).getId().toString());
    }

    @Override
    public void dbDelete() {
        SugarModel.deleteAll(SugarModel.class);
    }

    @Override
    public long dbCount() {
        return SugarModel.listAll(SugarModel.class).size();
    }

}
