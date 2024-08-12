package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfBill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}

//id
//description
//remark
//bill
//summa
//typeBill

