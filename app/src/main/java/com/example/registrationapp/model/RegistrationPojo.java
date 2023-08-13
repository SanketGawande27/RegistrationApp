package com.example.registrationapp.model;

public class RegistrationPojo {
    private int id;
    private String userName;
    private String email;
    private String password;
    private String confirmPassword;

    public RegistrationPojo(int id, String userName, String email, String password, String confirmPassword) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public RegistrationPojo(String userName, String email, String password, String confirmPassword) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public RegistrationPojo() {    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
