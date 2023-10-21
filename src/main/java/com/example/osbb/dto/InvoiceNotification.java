package com.example.osbb.dto;

import com.example.osbb.dto.BodyInvoiceNotification;
import com.example.osbb.dto.HeaderInvoiceNotification;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceNotification {
    private HeaderInvoiceNotification header;
    private BodyInvoiceNotification body;


}

//address
//bill
//area
//currentTime
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