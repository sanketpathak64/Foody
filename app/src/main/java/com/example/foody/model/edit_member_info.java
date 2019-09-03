package com.example.foody.model;

public class edit_member_info {
    String name,phone,address,workplace,profilepic;

    public edit_member_info(String name, String phone, String address, String workplace,String profilepic) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.workplace = workplace;
        this.profilepic=profilepic;
    }

    public edit_member_info() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
