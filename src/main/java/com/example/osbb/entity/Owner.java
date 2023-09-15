package com.example.osbb.entity;

import com.example.osbb.enums.FamilyStatus;
import com.example.osbb.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "owners", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
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
    private Gender gender;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    @Column(name = "date_birth")
    private LocalDate dateBirth;
    @Column(name = "share_estate")
    private Double shareInRealEstate;
    @Column(name = "family_status")
    @Enumerated(EnumType.STRING)
    private FamilyStatus familyStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", referencedColumnName = "id")
    private Passport passport;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private List<Photo> photos;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "owner_questionnaire",
            joinColumns = @JoinColumn(name = "questionnaire_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
    List<Questionnaire> questionnaires = new ArrayList<>();

//    @ManyToMany()
//    @JoinTable(
//            name = "owner_ownership",
//            joinColumns = @JoinColumn(name = "ownership_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
//    @JsonIgnore
//    private List<Ownership> ownerships = new ArrayList<>();


    public void setDateBirth(LocalDate dateBirth) {
        if (dateBirth.toString().equals("нет"))
            this.dateBirth = null;
        this.dateBirth = dateBirth;
    }
}
