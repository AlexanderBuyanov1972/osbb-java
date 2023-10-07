package com.example.osbb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceNotification {
    private Double debtAtBeginningPeriod;
    private Double rate;
    private Double accrued;
    private Double recalculationForServicesNotReceived;
    private Double subsidyMonetization;
    private Double monetizationBenefits;
    private Double paid;
    private Double finalDebtForPeriod;

}
