package com.example.osbb.dto;

import java.time.LocalDate;

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

    public BodyDebt(LocalDate beginningPeriod, Double debtAtBeginningPeriod, Double rate, Double accrued, Double recalculationForServicesNotReceived, Double subsidyMonetization, Double monetizationBenefits, Double paid, Double debtAtFinalizingPeriod, LocalDate finalizingPeriod) {
        this.beginningPeriod = beginningPeriod;
        this.debtAtBeginningPeriod = debtAtBeginningPeriod;
        this.rate = rate;
        this.accrued = accrued;
        this.recalculationForServicesNotReceived = recalculationForServicesNotReceived;
        this.subsidyMonetization = subsidyMonetization;
        this.monetizationBenefits = monetizationBenefits;
        this.paid = paid;
        this.debtAtFinalizingPeriod = debtAtFinalizingPeriod;
        this.finalizingPeriod = finalizingPeriod;
    }

    public BodyDebt() {
    }

    public LocalDate getBeginningPeriod() {
        return beginningPeriod;
    }

    public Double getDebtAtBeginningPeriod() {
        return debtAtBeginningPeriod;
    }

    public Double getRate() {
        return rate;
    }

    public Double getAccrued() {
        return accrued;
    }

    public Double getRecalculationForServicesNotReceived() {
        return recalculationForServicesNotReceived;
    }

    public Double getSubsidyMonetization() {
        return subsidyMonetization;
    }

    public Double getMonetizationBenefits() {
        return monetizationBenefits;
    }

    public Double getPaid() {
        return paid;
    }

    public Double getDebtAtFinalizingPeriod() {
        return debtAtFinalizingPeriod;
    }

    public LocalDate getFinalizingPeriod() {
        return finalizingPeriod;
    }

    public BodyDebt setBeginningPeriod(LocalDate beginningPeriod) {
        this.beginningPeriod = beginningPeriod;
        return this;
    }

    public BodyDebt setDebtAtBeginningPeriod(Double debtAtBeginningPeriod) {
        this.debtAtBeginningPeriod = debtAtBeginningPeriod;
        return this;
    }

    public BodyDebt setRate(Double rate) {
        this.rate = rate;
        return this;
    }

    public BodyDebt setAccrued(Double accrued) {
        this.accrued = accrued;
        return this;
    }

    public BodyDebt setRecalculationForServicesNotReceived(Double recalculationForServicesNotReceived) {
        this.recalculationForServicesNotReceived = recalculationForServicesNotReceived;
        return this;
    }

    public BodyDebt setSubsidyMonetization(Double subsidyMonetization) {
        this.subsidyMonetization = subsidyMonetization;
        return this;
    }

    public BodyDebt setMonetizationBenefits(Double monetizationBenefits) {
        this.monetizationBenefits = monetizationBenefits;
        return this;
    }

    public BodyDebt setPaid(Double paid) {
        this.paid = paid;
        return this;
    }

    public BodyDebt setDebtAtFinalizingPeriod(Double debtAtFinalizingPeriod) {
        this.debtAtFinalizingPeriod = debtAtFinalizingPeriod;
        return this;
    }

    public BodyDebt setFinalizingPeriod(LocalDate finalizingPeriod) {
        this.finalizingPeriod = finalizingPeriod;
        return this;
    }

    @Override
    public String toString() {
        return "BodyDebt = { beginningPeriod = " + beginningPeriod + ", debtAtBeginningPeriod = " + debtAtBeginningPeriod +
                ", rate = " + rate + ", accrued = " + accrued +
                ", recalculationForServicesNotReceived = " + recalculationForServicesNotReceived +
                ", subsidyMonetization = " + subsidyMonetization + ", monetizationBenefits = " + monetizationBenefits +
                ", paid = " + paid + ", debtAtFinalizingPeriod = " + debtAtFinalizingPeriod +
                ", finalizingPeriod = " + finalizingPeriod + " }";
    }
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
