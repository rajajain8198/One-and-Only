package com.example.rajajainofficalproject.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User_Details")
public class UserDetails {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "unique_ID")
    private String unique_ID;


    @ColumnInfo(name = "userID")
    private String userID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "number")
    private String number;

    @ColumnInfo(name = "password")
    private String password;


    public UserDetails() {
    }


    public UserDetails(@NonNull String unique_ID, String userID, String name, String image, String email, String number, String password) {
        this.email = email;
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.number = number;
        this.image = image;
        this.unique_ID = unique_ID;

    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnique_ID() {
        return unique_ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUnique_ID(String unique_ID) {
        this.unique_ID = unique_ID;
    }




    @Override
    public String
    toString() {
        return "UserDetails{" +
                "unique_ID='" + unique_ID + '\'' +
                ", userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
