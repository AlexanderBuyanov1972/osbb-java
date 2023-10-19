package com.example.osbb.dto.response;

import com.example.osbb.dto.response.BodyInvoiceNotification;
import com.example.osbb.dto.response.HeaderInvoiceNotification;
import com.example.osbb.entity.ownership.Address;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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