package com.example.dakbayaknow;

public class Applications {
    private  String fullname;
    private String destination;
    private String status;
    private String health;
    private String travellerType, origin, travelDate, arrivalDate, dateToday, travelId;

    public Applications() {

    }

    public Applications(String fullname, String destination, String status, String health, String travellerType,
                        String origin, String travelDate, String arrivalDate, String dateToday, String travelId) {
        this.fullname = fullname;
        this.destination = destination;
        this.status = status;
        this.health = health;
        this.travellerType = travellerType;
        this.origin = origin;
        this.travelDate = travelDate;
        this.arrivalDate = arrivalDate;
        this.dateToday = dateToday;
        this.travelId = travelId;
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

    public String getTravellerType() {
        return travellerType;
    }

    public void setTravellerType(String travellerType) {
        this.travellerType = travellerType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDateToday() {
        return dateToday;
    }

    public void setDateToday(String dateToday) {
        this.dateToday = dateToday;
    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }
}
