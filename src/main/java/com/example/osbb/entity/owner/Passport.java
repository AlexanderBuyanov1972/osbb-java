package com.example.osbb.entity.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;


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

    public Passport() {
    }

    public Passport(Long id, String numberPassport, String numberEntry, LocalDate dateIssue, String issuingAuthority, String registrationNumberCardPayerTaxes, Owner owner) {
        this.id = id;
        this.numberPassport = numberPassport;
        this.numberEntry = numberEntry;
        this.dateIssue = dateIssue;
        this.issuingAuthority = issuingAuthority;
        this.registrationNumberCardPayerTaxes = registrationNumberCardPayerTaxes;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getNumberPassport() {
        return numberPassport;
    }

    public String getNumberEntry() {
        return numberEntry;
    }

    public LocalDate getDateIssue() {
        return dateIssue;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public String getRegistrationNumberCardPayerTaxes() {
        return registrationNumberCardPayerTaxes;
    }

    public Owner getOwner() {
        return owner;
    }

    public Passport setId(Long id) {
        this.id = id;
        return this;
    }

    public Passport setNumberPassport(String numberPassport) {
        this.numberPassport = numberPassport;
        return this;
    }

    public Passport setNumberEntry(String numberEntry) {
        this.numberEntry = numberEntry;
        return this;
    }

    public Passport setDateIssue(LocalDate dateIssue) {
        this.dateIssue = dateIssue;
        return this;
    }

    public Passport setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
        return this;
    }

    public Passport setRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        this.registrationNumberCardPayerTaxes = registrationNumberCardPayerTaxes;
        return this;
    }

    public Passport setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "Passport = { id = " + id + ", numberPassport = " + numberPassport + ", numberEntry = " + numberEntry +
                ", dateIssue = " + dateIssue + ", issuingAuthority = " + issuingAuthority +
                ", registrationNumberCardPayerTaxes = " + registrationNumberCardPayerTaxes +
                ", owner = " + owner + " }";
    }
}

//    id
//    numberPassport
//    numberEntry
//    dateIssue
//    issuingAuthority
//    owner
