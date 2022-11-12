package com.patach.eventorginzer.Model;

public class Event {
    String image,about,name,userImage,userName;

    public Event(String image, String about, String name,String userImage,String userName) {
        this.image = image;
        this.about = about;
        this.name = name;
        this.userName=userName;
        this.userImage=userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getImage() {
        return image;
    }

    public String getAbout() {
        return about;
    }

    public String getName() {
        return name;
    }
}
