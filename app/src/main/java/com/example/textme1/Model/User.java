package com.example.textme1.Model;

public class User {

    private String id;
    private String username;
    private String imageUrl;
    private String gender;
    private String phoneNumber;

    public User() {
    }

    public User(String id, String username, String imageUrl, String gender, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
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

}
