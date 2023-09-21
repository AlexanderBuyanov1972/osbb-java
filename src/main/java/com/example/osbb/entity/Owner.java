package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfFamilyStatus;
import com.example.osbb.enums.TypeOfGender;
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
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true)
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
    private TypeOfFamilyStatus familyStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", referencedColumnName = "id")
    private Passport passport;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private List<Photo> photos;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "owner_vehicle",
            joinColumns = @JoinColumn(name = "vehicle_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
    List<Vehicle> vehicles = new ArrayList<>();

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
