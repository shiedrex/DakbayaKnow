package com.example.dakbayaknow;

public class VaccCard {
    private String imageUrl;

    public VaccCard(){

    }

    public VaccCard(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
