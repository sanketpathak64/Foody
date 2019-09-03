package com.example.foody.model;

public class mem {
String name,phone,uid,profilepic;

    public mem(String name, String phone, String uid,String profilepic) {
        this.name = name;
        this.phone = phone;
        this.uid = uid;
        this.profilepic=profilepic;
    }

    public mem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
