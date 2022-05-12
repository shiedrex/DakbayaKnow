package com.example.dakbayaknow;

public class Applications {
    private  String fullname;
    private String destination;
    private String status;
    private String health;

    public Applications() {

    }

    public Applications(String fullname, String destination, String status, String health) {
        this.fullname = fullname;
        this.destination = destination;
        this.status = status;
        this.health = health;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }
}
