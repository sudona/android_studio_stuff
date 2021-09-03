package com.example.finalproject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListUsers {
    private static ListUsers instance;
    List<Map<String, String>> userList = new ArrayList<>();

    private ListUsers() {}

    public static ListUsers getInstance() {
        if (instance == null) {
            instance = new ListUsers();
        }
        return instance;
    }

    public void addUser(Map<String, String> user) {
        userList.add(user);
    }

    public List<Map<String, String>> getUserList() {
        return userList;
    }

    public void replaceUserList(List<Map<String,String>> users) {
        userList.clear();
        for (Map<String, String> user : users){
            userList.add(new LinkedHashMap<>(user));
        }
    }
}
