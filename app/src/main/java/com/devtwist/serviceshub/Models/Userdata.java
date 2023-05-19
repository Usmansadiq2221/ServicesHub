package com.devtwist.serviceshub.Models;

public class Userdata {
    private String uId, cnicNo, username, lName, eMail,  gender, phoneNo,province, city, address, profession, skillDetails, profilePic,  rating, ratePerDay, earned, token;
    private int badReview;
    private double timestamp, longitude, latitude;

    public Userdata() {

    }

    public Userdata(String uId, String cnicNo, String username, String lName, String eMail, String gender, String phoneNo, String province, String city, String address, String profession, String skillDetails, String profilePic, String rating, String ratePerDay, String earned, String token, int badReview, double timestamp, double longitude, double latitude) {
        this.uId = uId;
        this.cnicNo = cnicNo;
        this.username = username;
        this.lName = lName;
        this.eMail = eMail;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.province = province;
        this.city = city;
        this.address = address;
        this.profession = profession;
        this.skillDetails = skillDetails;
        this.profilePic = profilePic;
        this.rating = rating;
        this.ratePerDay = ratePerDay;
        this.earned = earned;
        this.token = token;
        this.badReview = badReview;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSkillDetails() {
        return skillDetails;
    }

    public void setSkillDetails(String skillDetails) {
        this.skillDetails = skillDetails;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(String ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public String getEarned() {
        return earned;
    }

    public void setEarned(String earned) {
        this.earned = earned;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getBadReview() {
        return badReview;
    }

    public void setBadReview(int badReview) {
        this.badReview = badReview;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
