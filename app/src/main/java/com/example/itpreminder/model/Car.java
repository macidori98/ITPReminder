package com.example.itpreminder.model;

public class Car {
    private String id, phoneNumber, carType, plateNumber, doneDate, expireDate;
    private int itpPeriod;

    public Car(String id, String phoneNumber, String carType, String plateNumber, String doneDate, String expireDate, int itpPeriod) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.carType = carType;
        this.plateNumber = plateNumber;
        this.doneDate = doneDate;
        this.expireDate = expireDate;
        this.itpPeriod = itpPeriod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getItpPeriod() {
        return itpPeriod;
    }

    public void setItpPeriod(int itpPeriod) {
        this.itpPeriod = itpPeriod;
    }
}
