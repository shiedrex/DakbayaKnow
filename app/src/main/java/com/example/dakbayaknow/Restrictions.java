package com.example.dakbayaknow;

public class Restrictions {
    private String city;
    private String alert;

    public Restrictions() {

    }

    public Restrictions(String city, String alert) {
        this.city = city;
        this.alert = alert;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }
}
