package com.example.osbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "passwords", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "password_id"),
        @UniqueConstraint(columnNames = "registration_number_card_payer_taxes")
})

public class Password {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "password_id", nullable = false, unique = true)
    private String passwordID;
    @Column(name = "number_entry", nullable = false)
    private String numberEntry;
    @Column(name = "date_issue", nullable = false)
    private LocalDate dateIssue;
    @Column(name = "issuing_authority", nullable = false)
    private String issuingAuthority;
    @Column(name = "registration_number_card_payer_taxes", nullable = false, unique = true)
    private String registrationNumberCardPayerTaxes;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "password")
    @JsonIgnore
    private Owner owner;
}
