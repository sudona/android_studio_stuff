package com.example.finalproject;

public class User {
    public int id;

    public String name, username, email, website;
    public Address address;

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public class Address {

        String street;

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    '}';
        }
        public String getStreet() {
            return street;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", address=" + address +
                '}';
    }
}
