package com.devtwist.serviceshub.Models;

public class PostData {

    private String postId, serviceBudget, serviceName, serviceTime, serviceWorkplace, userCity, userId;
    private double timestamp;

    public PostData() {

    }

    public PostData(String postId, String serviceBudget, String serviceName, String serviceTime, String serviceWorkplace, String userCity, String userId, double timestamp) {
        this.postId = postId;
        this.serviceBudget = serviceBudget;
        this.serviceName = serviceName;
        this.serviceTime = serviceTime;
        this.serviceWorkplace = serviceWorkplace;
        this.userCity = userCity;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getServiceBudget() {
        return serviceBudget;
    }

    public void setServiceBudget(String serviceBudget) {
        this.serviceBudget = serviceBudget;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceWorkplace() {
        return serviceWorkplace;
    }

    public void setServiceWorkplace(String serviceWorkplace) {
        this.serviceWorkplace = serviceWorkplace;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
