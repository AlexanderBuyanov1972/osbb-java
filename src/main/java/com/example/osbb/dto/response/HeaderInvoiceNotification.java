package com.example.osbb.dto.response;

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
    private String personalAccount;
    private Double totalArea;
    private LocalDateTime currentDateTime;
}
