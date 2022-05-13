package com.example.dakbayaknow;

public class Feedback {
    private String rating;
    private String username;
    private Float star;
    private String comment;
    public Feedback() {

    }

    public Feedback(String rating, String username, Float star, String comment) {
        this.rating = rating;
        this.username = username;
        this.star = star;
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }


    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }
}
