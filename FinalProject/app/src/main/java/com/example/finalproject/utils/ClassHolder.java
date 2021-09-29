package com.example.finalproject.utils;

import java.util.HashMap;

public class ClassHolder {
    private static ClassHolder instance;
    public Class classAt;
    public HashMap<String, Object> extras;
    private Boolean singlePass = false;

    public static ClassHolder getInstance() {
        if (instance == null) {
            instance = new ClassHolder();
        }
        return instance;
    }

    public void addSinglePass() {
        singlePass = true;
    }

    public Boolean checkSinglePass() {
        Boolean singlePassHold = singlePass;
        singlePass = false;
        return singlePassHold;
    }

    public void addIntentInfo(Class classAt) {
        this.classAt = classAt;
        this.extras = null;
    }

    public void addIntentInfo(Class classAt, HashMap<String, Object> extras) {
        this.classAt = classAt;
        this.extras = extras;
    }
}
