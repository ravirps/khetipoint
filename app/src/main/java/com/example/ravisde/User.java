package com.example.ravisde;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public String data;
    public User(String r)
    {
        this.data=r;
    }
}