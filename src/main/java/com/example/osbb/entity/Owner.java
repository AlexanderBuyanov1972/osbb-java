package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfBeneficiary;
import com.example.osbb.enums.TypeOfFamilyStatus;
import com.example.osbb.enums.TypeOfGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "second_name", nullable = false)
    private String secondName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfGender gender;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "date_birth", nullable = false)
    private LocalDate dateBirth;
    @Column(name = "share_estate", nullable = false)
    private Double shareInRealEstate;
    @Column(name = "family_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfFamilyStatus familyStatus;
    @Column(name = "beneficiary", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfBeneficiary beneficiary;


    // ----------- one to one -----------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", referencedColumnName = "id")
    private Passport passport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_work_id", referencedColumnName = "id")
    private PlaceWork placeWork;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private Photo photo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Record record;

}
