package com.example.dakbayaknow;

public class Docx {
    private String govId, govIdNumber, status;

    public Docx(){

    }

    public Docx(String govId, String govIdNumber, String status) {
        this.govId = govId;
        this.govIdNumber = govIdNumber;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
