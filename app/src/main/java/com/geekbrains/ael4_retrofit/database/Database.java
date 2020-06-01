package com.geekbrains.ael4_retrofit.database;


public interface Database {
    enum Action {SAVE, UPDATE, SELECT, DELETE}
    Database.Action action = null;
    void action();
    void dbSave();
    void dbUpdate();
    void dbSelect();
    void dbDelete();
    long dbCount();

 }
