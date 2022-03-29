package com.example.dakbayaknow;

public class Docx {
    private String govId, govIdNumber;

    public Docx(){

    }

    public Docx(String govId, String govIdNumber) {
        this.govId = govId;
        this.govIdNumber = govIdNumber;
    }

    public String getGovId() {
        return govId;
    }

    public void setGovId(String govId) {
        this.govId = govId;
    }

    public String getGovIdNumber() {
        return govIdNumber;
    }

    public void setGovIdNumber(String govIdNumber) {
        this.govIdNumber = govIdNumber;
    }

}
