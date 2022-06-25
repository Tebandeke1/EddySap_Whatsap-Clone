package com.tabutech.eddysap.Model.ContactsModel;

public class ContactsModel {
    private String Number;
    private String Name;

    public ContactsModel() {

    }

    public ContactsModel(String number, String name) {
        Number = number;
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
