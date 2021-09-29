package com.example.finalproject.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String username;
    public String email;
    public String website;
    public String image;
    public Address address;

    public User(int id, String name, String username, String email, String website, String image, Address address) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.website = website;
        this.image = image;
        this.address = address;
    }

    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.username = user.username;
        this.email = user.email;
        this.website = user.website;
        this.address = new Address(user.address.street, user.address.geo.lat, user.address.geo.lng);
    }

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getUsername() { return username; }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", address=" + address +
                '}';
    }

    public LinkedHashMap<String, String> getRepresentation() {
        return new LinkedHashMap<String, String>(){{
            put("Image", getImage());
            put("Name", getName());
            put("Email", getEmail());
            put("Website", getWebsite());
            put("Street", getAddress().street);
            put("Latitude", String.valueOf(getAddress().getGeo().lat));
            put("Longitude", String.valueOf(getAddress().getGeo().lng));
        }};
    }

    public void addFromRep(String key, String value) {
        switch (key){
            case "Image":
                this.image = value;
                break;
            case "Name":
                this.name = value;
                break;
            case "Email":
                this.email = value;
                break;
            case "Website":
                this.website = value;
                break;
            case "Street":
                this.address.street = value;
                break;
            case "Latitude":
                try{
                    this.address.geo.lat = Float.parseFloat(value);
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case "Longitude":
                try{
                    this.address.geo.lng = Float.parseFloat(value);
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}

