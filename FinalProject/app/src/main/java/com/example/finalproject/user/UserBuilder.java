package com.example.finalproject.user;

public class UserBuilder {
    private int id = 0;

    private String name = "", username = "", email = "", website = "", image = "";
    private Address address = new Address("");

    public UserBuilder id(int id) {
        this.id = id;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder website(String website) {
        this.website = website;
        return this;
    }

    public UserBuilder image(String image) {
        this.image = image;
        return this;
    }

    public UserBuilder street(String street) {
        this.address.street = street;
        return this;
    }

    public UserBuilder geo(double latitude, double longitude) {
        this.address.geo.lat = latitude;
        this.address.geo.lng = longitude;
        return this;
    }

    public User build() {
        return new User(id, name, username, email, website, image, address);
    }

}
