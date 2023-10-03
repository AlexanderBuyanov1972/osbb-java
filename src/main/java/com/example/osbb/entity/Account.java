package com.example.osbb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "opening_balances")
    private Double openingBalances;
    @Column(name = "debt")
    private Double debt;
    @Column(name = "credit")
    private Double credit;
    @Column(name = "balances_end_period")
    private Double balancesEndPeriod;
    @Column(name = "start_time")
    private LocalDate startTime;
    @Column(name = "end_time")
    private LocalDate endTime;

}

//    id
//    openingBalances
//    debt
//    credit
//    balancesEndPeriod
//    startTime
//    endTime
