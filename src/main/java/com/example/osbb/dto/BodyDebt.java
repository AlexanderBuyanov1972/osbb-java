package com.example.osbb.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BodyDebt {
    private LocalDate beginningPeriod;
    private Double debtAtBeginningPeriod;
    private Double rate;
    private Double accrued;
    private Double recalculationForServicesNotReceived;
    private Double subsidyMonetization;
    private Double monetizationBenefits;
    private Double paid;
    private Double debtAtFinalizingPeriod;
    private LocalDate finalizingPeriod;
}

//    beginningPeriod
//    debtAtBeginningPeriod
//    rate
//    accrued
//    recalculationForServicesNotReceived
//    subsidyMonetization
//    monetizationBenefits
//    paid
//    debtAtFinalizingPeriod
//    finalizingPeriod
