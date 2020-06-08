package com.geekbrains.ael4_retrofit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private Map<String,RepoUsers> userMap=new HashMap<>();

    public List<RepoUsers> getUsersList() {
        return new ArrayList<>(userMap.values());
    }

    public boolean addUserRepos(List<RepoUserInfo> info,String name){
        if(!userMap.containsKey(name))return false;
        userMap.get(name).setRepository(info);
        return true;
    }

    public void addUser(RepoUsers user){
        userMap.put(user.getUser(),user);
    }

    public RepoUsers getUser(String name){
        return userMap.get(name);
    }

    public void setUsersList(List<RepoUsers> usersList) {
        for (RepoUsers user:usersList)
            userMap.put(user.getUser(),user);
    }
}
