package com.example.dakbayaknow;

public class Contacts {
    private String city;
    private String contact;

    public Contacts() {

    }

    public Contacts(String city, String contact) {
        this.city = city;
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setProvince(String province) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
