package com.example.finalproject.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.user.Address;
import com.example.finalproject.user.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    //delete all query
    @Delete
    void reset(List<User> users);

    @Query("UPDATE users SET name = :sname, username = :susername, email = :semail, " +
            "website = :swebsite, image = :simage, address = :saddress WHERE id = :sid")
    void update(int sid, String sname, String susername, String semail,
                String swebsite, String simage, Address saddress);

    @Query("SELECT * from users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id=:sid")
    List<User> get(int sid);

    @Query("SELECT COUNT(id) FROM users")
    int size();
}
