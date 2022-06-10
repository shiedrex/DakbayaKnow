package com.example.dakbayaknow;

public class Requirement {
    private String imageUrl;

    public Requirement(){

    }

    public Requirement(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
