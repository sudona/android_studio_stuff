package com.example.app5;

public class CardInfo {
    String info, picture, type, actors, year;

    public CardInfo(String info, String picture, String type, String actors, String year) {
        this.info = info;
        this.picture = picture;
        this.type = type;
        this.actors = actors;
        this.year = year;
    }

    public String getInfo() {
        return info;
    }

    public String getPicture() {
        return picture;
    }

    public String getType() {
        return type;
    }

    public String getActors() {
        return actors;
    }

    public String getYear() {
        return year;
    }
}
