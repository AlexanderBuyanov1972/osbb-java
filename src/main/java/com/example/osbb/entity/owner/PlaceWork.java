package com.example.osbb.entity.owner;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "places_work")
public class PlaceWork {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;
    @Column(name = "business_name")
    private String businessName;
    @Column(name = "address")
    private String address;
    @Column(name = "number_phone")
    private String numberPhone;
    @Column(name = "position")
    private String position;
    @Column(name = "addition")
    private String addition;

    @OneToOne(mappedBy = "placeWork")
    @JsonIgnore
    private Owner owner;

    public PlaceWork() {
    }

    public PlaceWork(long id, String businessName, String address, String numberPhone, String position, String addition, Owner owner) {
        this.id = id;
        this.businessName = businessName;
        this.address = address;
        this.numberPhone = numberPhone;
        this.position = position;
        this.addition = addition;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getAddress() {
        return address;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public String getPosition() {
        return position;
    }

    public String getAddition() {
        return addition;
    }

    public Owner getOwner() {
        return owner;
    }

    public PlaceWork setId(long id) {
        this.id = id;
        return this;
    }

    public PlaceWork setBusinessName(String businessName) {
        this.businessName = businessName;
        return this;
    }

    public PlaceWork setAddress(String address) {
        this.address = address;
        return this;
    }

    public PlaceWork setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
        return this;
    }

    public PlaceWork setPosition(String position) {
        this.position = position;
        return this;
    }

    public PlaceWork setAddition(String addition) {
        this.addition = addition;
        return this;
    }

    public PlaceWork setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "PlaceWork = { id = " + id + ", businessName = " + businessName + ", address = " + address +
                ", numberPhone = " + numberPhone + ", position = " + position + ", addition = " + addition +
                ", owner = " + owner + " }";
    }
}

//    id
//    businessName
//    address
//    numberPhone
//    position
//    addition
//    owner
