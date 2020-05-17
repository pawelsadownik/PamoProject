package com.pamo.iparish.model;

public class Parishioner {
    private String parishionerId;
    private String name;
    private String surname;
    private String phoneNumber;
    private String church;

    public Parishioner(String parishionerId){
        this.parishionerId = parishionerId;
    }

    public String getParishionerId() {
        return parishionerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }
}
