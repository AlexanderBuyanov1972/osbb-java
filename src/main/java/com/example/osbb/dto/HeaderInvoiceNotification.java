package com.example.osbb.dto;

import com.example.osbb.entity.ownership.Address;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeaderInvoiceNotification {
    private Address address;
    private String bill;
    private Double area;
    private LocalDateTime currentTime;
}