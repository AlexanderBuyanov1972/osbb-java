package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfBill;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "bill")
    private String bill;

    @Column(name = "description")
    private String description;

    @Column(name = "remark")
    private String remark;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "summa")
    private Double summa;

    @Column(name = "type_bill", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfBill typeBill;

    public Payment() {
    }

    public Payment(Long id, String bill, String description, String remark, LocalDateTime date, Double summa, TypeOfBill typeBill) {
        this.id = id;
        this.bill = bill;
        this.description = description;
        this.remark = remark;
        this.date = date;
        this.summa = summa;
        this.typeBill = typeBill;
    }

    public Long getId() {
        return id;
    }

    public String getBill() {
        return bill;
    }

    public String getDescription() {
        return description;
    }

    public String getRemark() {
        return remark;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Double getSumma() {
        return summa;
    }

    public TypeOfBill getTypeBill() {
        return typeBill;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payment setBill(String bill) {
        this.bill = bill;
        return this;
    }

    public Payment setDescription(String description) {
        this.description = description;
        return this;
    }

    public Payment setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Payment setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public Payment setSumma(Double summa) {
        this.summa = summa;
        return this;
    }

    public Payment setTypeBill(TypeOfBill typeBill) {
        this.typeBill = typeBill;
        return this;
    }

    @Override
    public String toString() {
        return "Payment = { id = " + id + ", bill = " + bill + ", description = " + description + ", remark = " + remark
                + ", date = " + date + ", summa = " + summa + ", typeBill = " + typeBill + " }";
    }
}

//id
//description
//remark
//bill
//summa
//typeBill

