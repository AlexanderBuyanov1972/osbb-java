package com.example.osbb.dto;

public class ApartmentBillFullNamePhoneNumber {
    private String apartment;
    private String bill;
    private String fullName;
    private String phoneNumber;

    public ApartmentBillFullNamePhoneNumber() {
    }

    public ApartmentBillFullNamePhoneNumber(String apartment, String bill, String fullName, String phoneNumber) {
        this.apartment = apartment;
        this.bill = bill;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getApartment() {
        return apartment;
    }

    public String getBill() {
        return bill;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ApartmentBillFullNamePhoneNumber setApartment(String apartment) {
        this.apartment = apartment;
        return this;
    }

    public ApartmentBillFullNamePhoneNumber setBill(String bill) {
        this.bill = bill;
        return this;
    }

    public ApartmentBillFullNamePhoneNumber setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public ApartmentBillFullNamePhoneNumber setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public String toString() {
        return "ApartmentBillFullNamePhoneNumber = { apartment = " + apartment + ", bill = " + bill +
                ", fullName = " + fullName + ", phoneNumber = " + phoneNumber + "}";
    }
}
