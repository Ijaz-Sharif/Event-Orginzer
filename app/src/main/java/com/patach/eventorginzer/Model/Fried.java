package com.patach.eventorginzer.Model;

public class Fried {
    String name,pic,email,id;

    public Fried(String name, String pic, String email,String id) {
        this.name = name;
        this.pic = pic;
        this.email = email;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public String getEmail() {
        return email;
    }
}
