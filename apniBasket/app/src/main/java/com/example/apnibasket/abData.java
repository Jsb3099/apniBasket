package com.example.apnibasket;

public class abData {
    private String fullName;
    private String number;
    private String password;

    public abData(){
    }
    public abData(String fullName,String number,String password){
        this.fullName = fullName;
        this.number = number;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
