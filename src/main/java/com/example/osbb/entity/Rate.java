package com.example.osbb.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rates")
public class Rate {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "value")
    private Double value;

    public Rate() {
    }

    public Rate(Long id, LocalDate date, Double value) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getValue() {
        return value;
    }

    public Rate setId(Long id) {
        this.id = id;
        return this;
    }

    public Rate setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Rate setValue(Double value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Rate = { id = " + id + ", date = " + date + ", value = " + value + " }";
    }
}