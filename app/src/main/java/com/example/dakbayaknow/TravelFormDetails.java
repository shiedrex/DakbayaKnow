package com.example.dakbayaknow;

public class TravelFormDetails {
    private String travellerType, title,firstname, middleinitial, lastname, suffixname, email, contactNumber, gender,
            emergencyContactPerson, emergencyContactNumber,
            cRegion, cProvince, cMunicipality, cAddress,
            dRegion, dProvince, dMunicipality, dAddress,
            departure, arrival, status, vaccineStatus;

    public TravelFormDetails() {

    }

    public TravelFormDetails(String travellerType, String title, String firstname, String middleinitial, String lastname,
                             String suffixname, String email, String contactNumber, String gender, String emergencyContactPerson,
                             String emergencyContactNumber, String cRegion, String cProvince, String cMunicipality, String cAddress,
                             String dRegion, String dProvince, String dMunicipality, String dAddress, String departure, String arrival,
                             String status, String vaccineStatus) {

        this.travellerType = travellerType;
        this.title = title;
        this.firstname = firstname;
        this.middleinitial = middleinitial;
        this.lastname = lastname;
        this.suffixname = suffixname;
        this.email = email;
        this.contactNumber = contactNumber;
        this.gender = gender;
        this.emergencyContactPerson = emergencyContactPerson;
        this.emergencyContactNumber = emergencyContactNumber;
        this.cRegion = cRegion;
        this.cProvince = cProvince;
        this.cMunicipality = cMunicipality;
        this.cAddress = cAddress;
        this.dRegion = dRegion;
        this.dProvince = dProvince;
        this.dMunicipality = dMunicipality;
        this.dAddress = dAddress;
        this.departure = departure;
        this.arrival = arrival;
        this.status = status;
        this.vaccineStatus = vaccineStatus;
    }

    public String getTravellerType() {
        return travellerType;
    }

    public void setTravellerType(String travellerType) {
        this.travellerType = travellerType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddleinitial() {
        return middleinitial;
    }

    public void setMiddleinitial(String middleinitial) {
        this.middleinitial = middleinitial;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSuffixname() {
        return suffixname;
    }

    public void setSuffixname(String suffixname) {
        this.suffixname = suffixname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmergencyContactPerson() {
        return emergencyContactPerson;
    }

    public void setEmergencyContactPerson(String emergencyContactPerson) {
        this.emergencyContactPerson = emergencyContactPerson;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public String getcRegion() {
        return cRegion;
    }

    public void setcRegion(String cRegion) {
        this.cRegion = cRegion;
    }

    public String getcProvince() {
        return cProvince;
    }

    public void setcProvince(String cProvince) {
        this.cProvince = cProvince;
    }

    public String getcMunicipality() {
        return cMunicipality;
    }

    public void setcMunicipality(String cMunicipality) {
        this.cMunicipality = cMunicipality;
    }

    public String getcAddress() {
        return cAddress;
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getdRegion() {
        return dRegion;
    }

    public void setdRegion(String dRegion) {
        this.dRegion = dRegion;
    }

    public String getdProvince() {
        return dProvince;
    }

    public void setdProvince(String dProvince) {
        this.dProvince = dProvince;
    }

    public String getdMunicipality() {
        return dMunicipality;
    }

    public void setdMunicipality(String dMunicipality) {
        this.dMunicipality = dMunicipality;
    }

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(String vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }
}
