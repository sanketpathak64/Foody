package com.example.foody.model;

public class curmes {
    String address,evenfrom,evento,mornfrom,mornto,fees,name,nameofowner,phone,profilepic;

    public curmes(String address, String evenfrom, String evento, String mornfrom, String mornto, String fees, String name, String nameofowner, String phone,String profilepic) {
        this.address = address;
        this.evenfrom = evenfrom;
        this.evento = evento;
        this.mornfrom = mornfrom;
        this.mornto = mornto;
        this.fees = fees;
        this.name = name;
        this.nameofowner = nameofowner;
        this.phone = phone;
        this.profilepic=profilepic;
    }

    public curmes() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEvenfrom() {
        return evenfrom;
    }

    public void setEvenfrom(String evenfrom) {
        this.evenfrom = evenfrom;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getMornfrom() {
        return mornfrom;
    }

    public void setMornfrom(String mornfrom) {
        this.mornfrom = mornfrom;
    }

    public String getMornto() {
        return mornto;
    }

    public void setMornto(String mornto) {
        this.mornto = mornto;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameofowner() {
        return nameofowner;
    }

    public void setNameofowner(String nameofowner) {
        this.nameofowner = nameofowner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }


}
