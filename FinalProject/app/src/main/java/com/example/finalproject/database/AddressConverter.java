package com.example.finalproject.database;

import androidx.room.TypeConverter;

import com.example.finalproject.user.Address;
import com.google.gson.Gson;

public class AddressConverter {
    Gson gson = new Gson();

    @TypeConverter
    public Address fromString(String addressJson) {
        return addressJson == null ? null: gson.fromJson(addressJson, Address.class);
    }

    @TypeConverter
    public String addressToString(Address address) {
        return address == null ? null: gson.toJson(address, Address.class);
    }
}
