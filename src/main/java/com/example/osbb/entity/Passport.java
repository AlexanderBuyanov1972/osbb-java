package com.example.osbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "passports")

public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "number_passport", nullable = false)
    private String numberPassport;
    @Column(name = "number_entry", nullable = false)
    private String numberEntry;
    @Column(name = "date_issue", nullable = false)
    private LocalDate dateIssue;
    @Column(name = "issuing_authority", nullable = false)
    private String issuingAuthority;
    @Column(name = "registration_number_card_payer_taxes", nullable = false)
    private String registrationNumberCardPayerTaxes;


    @OneToOne(mappedBy = "passport")
    @JsonIgnore
    private Owner owner;

}
