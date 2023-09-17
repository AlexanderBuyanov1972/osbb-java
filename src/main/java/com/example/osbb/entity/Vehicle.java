package com.example.osbb.entity;

import com.example.osbb.enums.TypeColor;
import com.example.osbb.enums.TypeManufacturer;
import com.example.osbb.enums.TypeVehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_vehicle")
    @Enumerated(EnumType.STRING)
    private TypeVehicle typeVehicle;

    @Column(name = "number_vehicle")
    private String numberVehicle;

    @Column(name = "year_of_issue")
    private String yearOfIssue;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private TypeColor color;

    @Column(name = "type_manufacturer")
    @Enumerated(EnumType.STRING)
    private TypeManufacturer manufacturer;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "owner_vehicle",
            joinColumns = @JoinColumn(name = "vehicle_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
    @JsonIgnore
    List<Owner> owners = new ArrayList<>();
}
