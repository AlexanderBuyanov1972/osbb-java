package com.example.osbb.entity.owner;

import com.example.osbb.enums.TypeOfColor;
import com.example.osbb.enums.TypeOfManufacturer;
import com.example.osbb.enums.TypeOfVehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_vehicle")
    @Enumerated(EnumType.STRING)
    private TypeOfVehicle typeVehicle;

    @Column(name = "number_vehicle")
    private String numberVehicle;

    @Column(name = "year_of_issue")
    private String yearOfIssue;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private TypeOfColor typeColor;

    @Column(name = "type_manufacturer")
    @Enumerated(EnumType.STRING)
    private TypeOfManufacturer typeManufacturer;

    // --------- many to one ---------------

    @OneToOne(mappedBy = "vehicle")
    @JsonIgnore
    private Owner owner;

    public Vehicle() {
    }

    public Vehicle(Long id, TypeOfVehicle typeVehicle, String numberVehicle, String yearOfIssue, TypeOfColor typeColor, TypeOfManufacturer typeManufacturer, Owner owner) {
        this.id = id;
        this.typeVehicle = typeVehicle;
        this.numberVehicle = numberVehicle;
        this.yearOfIssue = yearOfIssue;
        this.typeColor = typeColor;
        this.typeManufacturer = typeManufacturer;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public TypeOfVehicle getTypeVehicle() {
        return typeVehicle;
    }

    public String getNumberVehicle() {
        return numberVehicle;
    }

    public String getYearOfIssue() {
        return yearOfIssue;
    }

    public TypeOfColor getTypeColor() {
        return typeColor;
    }

    public TypeOfManufacturer getTypeManufacturer() {
        return typeManufacturer;
    }

    public Owner getOwner() {
        return owner;
    }

    public Vehicle setId(Long id) {
        this.id = id;
        return this;
    }

    public Vehicle setTypeVehicle(TypeOfVehicle typeVehicle) {
        this.typeVehicle = typeVehicle;
        return this;
    }

    public Vehicle setNumberVehicle(String numberVehicle) {
        this.numberVehicle = numberVehicle;
        return this;
    }

    public Vehicle setYearOfIssue(String yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
        return this;
    }

    public Vehicle setTypeColor(TypeOfColor typeColor) {
        this.typeColor = typeColor;
        return this;
    }

    public Vehicle setTypeManufacturer(TypeOfManufacturer typeManufacturer) {
        this.typeManufacturer = typeManufacturer;
        return this;
    }

    public Vehicle setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "Vehicle = { id = " + id +", typeVehicle = " + typeVehicle +", numberVehicle = " + numberVehicle +
                ", yearOfIssue='" + yearOfIssue + ", typeColor=" + typeColor +", typeManufacturer=" + typeManufacturer +
                ", owner=" + owner +" }";
    }
}

//    id
//    typeVehicle
//    numberVehicle
//    yearOfIssue
//    typeColor
//    typeManufacturer
//    owner
