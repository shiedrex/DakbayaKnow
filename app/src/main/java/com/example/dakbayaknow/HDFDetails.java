package com.example.dakbayaknow;

public class HDFDetails {
    private String gender, firstname, middlename, lastname, nationality, age, email, contactNumber, presentAddress, country, city, arrival, symptoms, sick, covid, animal;

    public HDFDetails(String gender, String firstname, String middlename, String lastname, String nationality, String age, String email, String contactNumber, String presentAddress, String country, String city, String arrival, String symptoms, String sick, String covid, String animal) {
        this.gender = gender;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.nationality  = nationality;
        this.age = age;
        this.email = email;
        this.contactNumber = contactNumber;
        this.country = country;
        this.city = city;
        this.arrival = arrival;
        this.symptoms = symptoms;
        this.sick = sick;
        this.covid = covid;
        this.animal = animal;

    }

    public HDFDetails(){

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }

    public String getCovid() {
        return covid;
    }

    public void setCovid(String covid) {
        this.covid = covid;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
