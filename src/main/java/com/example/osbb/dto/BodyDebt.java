package com.example.osbb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
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
