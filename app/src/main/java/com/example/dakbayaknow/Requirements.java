package com.example.dakbayaknow;

public class Requirements {
    private String city;
    private String policy;
    private String fVaccReq1, fVaccReq2, fVaccReq3, fVaccReq4, fVaccReq5,
            uVaccReq1, uVaccReq2, uVaccReq3, uVaccReq4, uVaccReq5;

    public Requirements() {

    }

    public Requirements(String city, String policy,
                        String fVaccReq1, String fVaccReq2, String fVaccReq3, String fVaccReq4, String fVaccReq5,
                        String uVaccReq1, String uVaccReq2, String uVaccReq3, String uVaccReq4, String uVaccReq5) {

        this.city = city;
        this.policy = policy;
        this.fVaccReq1 = fVaccReq1;
        this.fVaccReq2 = fVaccReq2;
        this.fVaccReq3 = fVaccReq3;
        this.fVaccReq4 = fVaccReq4;
        this.fVaccReq5 = fVaccReq5;
        this.uVaccReq1 = uVaccReq1;
        this.uVaccReq2 = uVaccReq2;
        this.uVaccReq3 = uVaccReq3;
        this.uVaccReq4 = uVaccReq4;
        this.uVaccReq5 = uVaccReq5;
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

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getfVaccReq1() {
        return fVaccReq1;
    }

    public void setfVaccReq1(String fVaccReq1) {
        this.fVaccReq1 = fVaccReq1;
    }

    public String getfVaccReq2() {
        return fVaccReq2;
    }

    public void setfVaccReq2(String fVaccReq2) {
        this.fVaccReq2 = fVaccReq2;
    }

    public String getfVaccReq3() {
        return fVaccReq3;
    }

    public void setfVaccReq3(String fVaccReq3) {
        this.fVaccReq3 = fVaccReq3;
    }

    public String getfVaccReq4() {
        return fVaccReq4;
    }

    public void setfVaccReq4(String fVaccReq4) {
        this.fVaccReq4 = fVaccReq4;
    }

    public String getfVaccReq5() {
        return fVaccReq5;
    }

    public void setfVaccReq5(String fVaccReq5) {
        this.fVaccReq5 = fVaccReq5;
    }

    public String getuVaccReq1() {
        return uVaccReq1;
    }

    public void setuVaccReq1(String uVaccReq1) {
        this.uVaccReq1 = uVaccReq1;
    }

    public String getuVaccReq2() {
        return uVaccReq2;
    }

    public void setuVaccReq2(String uVaccReq2) {
        this.uVaccReq2 = uVaccReq2;
    }

    public String getuVaccReq3() {
        return uVaccReq3;
    }

    public void setuVaccReq3(String uVaccReq3) {
        this.uVaccReq3 = uVaccReq3;
    }

    public String getuVaccReq4() {
        return uVaccReq4;
    }

    public void setuVaccReq4(String uVaccReq4) {
        this.uVaccReq4 = uVaccReq4;
    }

    public String getuVaccReq5() {
        return uVaccReq5;
    }

    public void setuVaccReq5(String uVaccReq5) {
        this.uVaccReq5 = uVaccReq5;
    }
}
