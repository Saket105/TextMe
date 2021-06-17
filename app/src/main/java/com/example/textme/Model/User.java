package com.example.textme.Model;

public class User {

    private String id;
    private String username;
    private String imageUrl;
    private String gender;
    private String phoneNumber;

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User(String id, String username, String imageUrl, String gender, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
}
