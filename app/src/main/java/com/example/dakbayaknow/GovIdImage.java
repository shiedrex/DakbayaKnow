package com.example.dakbayaknow;

public class GovIdImage {
    private String fullname, destination, origin, departure, arrival, email, travellerType, govId, status, govIdImage;

    public GovIdImage(){

    }

    public GovIdImage(String fullname, String destination, String origin, String departure, String arrival, String email, String travellerType, String govId, String status, String govIdImage) {
        this.fullname = fullname;
        this.destination = destination;
        this.origin = origin;
        this.departure = departure;
        this.arrival = arrival;
        this.email = email;
        this.travellerType = travellerType;
        this.govId = govId;
        this.status = status;
        this.govIdImage = govIdImage;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTravellerType() {
        return travellerType;
    }

    public void setTravellerType(String travellerType) {
        this.travellerType = travellerType;
    }



    public String getGovIdImage() {
        return govIdImage;
    }

    public void setGovIdImage(String govIdImage) {
        this.govIdImage = govIdImage;
    }
}
