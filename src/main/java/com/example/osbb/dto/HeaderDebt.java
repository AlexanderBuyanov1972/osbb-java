package com.example.osbb.dto;

import com.example.osbb.entity.ownership.Address;
import lombok.*;

import java.time.LocalDateTime;

public class HeaderDebt {
    private Address address;
    private String bill;
    private Double area;
    private LocalDateTime currentTime;

    public HeaderDebt(Address address, String bill, Double area, LocalDateTime currentTime) {
        this.address = address;
        this.bill = bill;
        this.area = area;
        this.currentTime = currentTime;
    }

    public Address getAddress() {
        return address;
    }

    public String getBill() {
        return bill;
    }

    public Double getArea() {
        return area;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public HeaderDebt setAddress(Address address) {
        this.address = address;
        return this;
    }

    public HeaderDebt setBill(String bill) {
        this.bill = bill;
        return this;
    }

    public HeaderDebt setArea(Double area) {
        this.area = area;
        return this;
    }

    public HeaderDebt setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    @Override
    public String toString() {
        return "HeaderDebt = { address = " + address + ", bill = " + bill + ", area = " + area +
                ", currentTime=" + currentTime + "}";
    }
}