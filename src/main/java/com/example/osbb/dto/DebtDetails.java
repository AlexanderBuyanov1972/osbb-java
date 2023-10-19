package com.example.osbb.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DebtDetails {
    private HeaderInvoiceNotification header;
    private List<BodyInvoiceNotification> list;

}
