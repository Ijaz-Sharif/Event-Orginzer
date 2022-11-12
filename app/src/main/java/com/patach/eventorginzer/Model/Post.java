package com.patach.eventorginzer.Model;

public class Post {
    String image,about,name,userImage,userName,postId;
    public Post(String image, String about, String name,String userImage,String userName,String postId) {
        this.image = image;
        this.about = about;
        this.name = name;
        this.userName=userName;
        this.userImage=userImage;
        this.postId=postId;
    }

    public String getPostId() {
        return postId;
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
