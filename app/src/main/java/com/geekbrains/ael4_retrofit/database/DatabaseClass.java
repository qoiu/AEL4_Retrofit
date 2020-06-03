package com.geekbrains.ael4_retrofit.database;

import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;

abstract class DatabaseClass implements Database {

    MainPresenterInterface presenter;
    Database.Action action;

    DatabaseClass(MainPresenterInterface presenter, Action action) {
        this.action = action;
        this.presenter = presenter;
    }

    @Override
    public void action() {
        switch (action) {
            case SAVE:
                dbSave();
                break;
            case DELETE:
                dbDelete();
                break;
            case UPDATE:
                dbUpdate();
                break;
            case SELECT:
                dbSelect();
        }
    }

}
