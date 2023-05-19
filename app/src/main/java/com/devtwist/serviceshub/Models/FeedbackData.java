package com.devtwist.serviceshub.Models;

public class FeedbackData {
    private String senderId, rating, comment;
    private double timestamp;

    public FeedbackData(String senderId, String rating, String comment, double timestamp) {
        this.senderId = senderId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public FeedbackData() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
