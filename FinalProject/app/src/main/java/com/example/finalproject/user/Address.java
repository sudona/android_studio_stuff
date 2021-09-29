package com.example.finalproject.user;

public class Address {

    public String street;
    public Geo geo;

    public Address(String street) {
        this.street = street;
        this.geo = new Geo();
    }

    public Address(String street, double lat, double lng) {
        this.street = street;
        this.geo = new Geo(lat, lng);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                '}';
    }

    public Geo getGeo() {
        return geo;
    }

    public String getStreet() {
        return street;
    }
}
