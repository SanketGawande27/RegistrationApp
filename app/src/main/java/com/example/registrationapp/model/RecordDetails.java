package com.example.registrationapp.model;

public class RecordDetails {
    private String email;
    private String inTimeAndDate;
    private String inTimeLocation;
    private String outTimeAndDate;
    private String outTimeLocation;
    private String totalHr;


    public RecordDetails() {

    }

    public RecordDetails(String email, String inTimeAndDate, String inTimeLocation, String outTimeAndDate, String outTimeLocation, String totalHr) {
        this.email = email;
        this.inTimeAndDate = inTimeAndDate;
        this.inTimeLocation = inTimeLocation;
        this.outTimeAndDate = outTimeAndDate;
        this.outTimeLocation = outTimeLocation;
        this.totalHr = totalHr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInTimeAndDate() {
        return inTimeAndDate;
    }

    public void setInTimeAndDate(String inTimeAndDate) {
        this.inTimeAndDate = inTimeAndDate;
    }

    public String getInTimeLocation() {
        return inTimeLocation;
    }

    public void setInTimeLocation(String inTimeLocation) {
        this.inTimeLocation = inTimeLocation;
    }

    public String getOutTimeAndDate() {
        return outTimeAndDate;
    }

    public void setOutTimeAndDate(String outTimeAndDate) {
        this.outTimeAndDate = outTimeAndDate;
    }

    public String getOutTimeLocation() {
        return outTimeLocation;
    }

    public void setOutTimeLocation(String outTimeLocation) {
        this.outTimeLocation = outTimeLocation;
    }

    public String getTotalHr() {
        return totalHr;
    }

    public void setTotalHr(String totalHr) {
        this.totalHr = totalHr;
    }
}
