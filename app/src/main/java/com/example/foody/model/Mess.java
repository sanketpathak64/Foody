package com.example.foody.model;

public class Mess {
    String name;
    String phone;
    String lattitude;
    String longitude;
    String profilepic;
    public Mess(String name, String phone,String lattitude,String longitude,String profilepic) {
        this.name = name;
        this.phone = phone;
        this.lattitude=lattitude;
        this.longitude=longitude;
        this.profilepic=profilepic;
    }

    public Mess() {

    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }


}
