package com.example.osbb.dto;

public class EntryBalanceHouse {
    private String bill;
    private String apartment;
    private Double summa;

    public EntryBalanceHouse() {
    }

    public EntryBalanceHouse(String bill, String apartment, Double summa) {
        this.bill = bill;
        this.apartment = apartment;
        this.summa = summa;
    }

    public String getBill() {
        return bill;
    }

    public String getApartment() {
        return apartment;
    }

    public Double getSumma() {
        return summa;
    }

    public EntryBalanceHouse setBill(String bill) {
        this.bill = bill;
        return this;
    }

    public EntryBalanceHouse setApartment(String apartment) {
        this.apartment = apartment;
        return this;
    }

    public EntryBalanceHouse setSumma(Double summa) {
        this.summa = summa;
        return this;
    }

    @Override
    public String toString() {
        return "EntryBalanceHouse = { bill = " + bill + ", apartment = " + apartment + ", summa=" + summa + " }";
    }
}
