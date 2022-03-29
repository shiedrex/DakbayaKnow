package com.example.dakbayaknow;

public class Requirements {
    private String city;
    private String policy;

    public Requirements() {

    }

    public Requirements(String city, String policy) {
        this.city = city;
        this.policy = policy;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPolicy() {
        return policy;
    }

    public void setAlert(String alert) {
        this.policy = alert;
    }
}
