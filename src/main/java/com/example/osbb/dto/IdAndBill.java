package com.example.osbb.dto;

public class IdAndBill {
    private Long id;
    private String bill;

    public IdAndBill() {
    }

    public IdAndBill(Long id, String bill) {
        this.id = id;
        this.bill = bill;
    }

    public Long getId() {
        return id;
    }

    public String getBill() {
        return bill;
    }

    public IdAndBill setId(Long id) {
        this.id = id;
        return this;
    }

    public IdAndBill setBill(String bill) {
        this.bill = bill;
        return this;
    }

    @Override
    public String toString() {
        return "IdAndBill = { id = " + id + ", bill = " + bill + "}";
    }
}
