package com.example.osbb.entity.owner;

import com.example.osbb.entity.Record;
import com.example.osbb.enums.TypeOfBeneficiary;
import com.example.osbb.enums.TypeOfFamilyStatus;
import com.example.osbb.enums.TypeOfGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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
    @Column(name = "family_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfFamilyStatus familyStatus;
    @Column(name = "beneficiary", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfBeneficiary beneficiary;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "additional_information")
    private String additionalInformation;


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

    // ----------- one to many -----------------

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<Record> records;

    public Owner() {
    }

    public Owner(long id, String firstName, String secondName, String lastName, TypeOfGender gender, String email, String phoneNumber, LocalDate dateBirth, TypeOfFamilyStatus familyStatus, TypeOfBeneficiary beneficiary, boolean isActive, String additionalInformation, Passport passport, PlaceWork placeWork, Photo photo, Vehicle vehicle, List<Record> records) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateBirth = dateBirth;
        this.familyStatus = familyStatus;
        this.beneficiary = beneficiary;
        this.isActive = isActive;
        this.additionalInformation = additionalInformation;
        this.passport = passport;
        this.placeWork = placeWork;
        this.photo = photo;
        this.vehicle = vehicle;
        this.records = records;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public TypeOfGender getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public TypeOfFamilyStatus getFamilyStatus() {
        return familyStatus;
    }

    public TypeOfBeneficiary getBeneficiary() {
        return beneficiary;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public Passport getPassport() {
        return passport;
    }

    public PlaceWork getPlaceWork() {
        return placeWork;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Record> getRecords() {
        return records;
    }

    public Owner setId(long id) {
        this.id = id;
        return this;
    }

    public Owner setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Owner setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public Owner setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Owner setGender(TypeOfGender gender) {
        this.gender = gender;
        return this;
    }

    public Owner setEmail(String email) {
        this.email = email;
        return this;
    }

    public Owner setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Owner setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
        return this;
    }

    public Owner setFamilyStatus(TypeOfFamilyStatus familyStatus) {
        this.familyStatus = familyStatus;
        return this;
    }

    public Owner setBeneficiary(TypeOfBeneficiary beneficiary) {
        this.beneficiary = beneficiary;
        return this;
    }

    public Owner setActive(boolean active) {
        isActive = active;
        return this;
    }


    public Owner setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public Owner setPassport(Passport passport) {
        this.passport = passport;
        return this;
    }

    public Owner setPlaceWork(PlaceWork placeWork) {
        this.placeWork = placeWork;
        return this;
    }

    public Owner setPhoto(Photo photo) {
        this.photo = photo;
        return this;
    }

    public Owner setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public Owner setRecords(List<Record> records) {
        this.records = records;
        return this;
    }

    @Override
    public String toString() {
        return "Owner = { id = " + id + ", firstName = " + firstName + ", secondName = " + secondName +
                ", lastName = " + lastName + ", gender = " + gender + ", email = " + email +
                ", phoneNumber = " + phoneNumber + ", dateBirth = " + dateBirth + ", familyStatus = " + familyStatus +
                ", beneficiary=" + beneficiary + ", isActive = " + isActive + ", additionalInformation =" + additionalInformation +
                ", passport = " + passport + ", placeWork = " + placeWork + ", photo = " + photo + ", vehicle = " + vehicle +
                ", records = " + records + " }";
    }
}
// деактивация собственника - это удаление всех записей и долей (доли равны 0) с его участием, но он остаётся в базе данных

//    id
//    firstName
//    secondName
//    lastName
//    gender
//    email
//    phoneNumber
//    dateBirth
//    familyStatus
//    beneficiary
//    passport
//    placeWork
//    photo
//    vehicle
//    records
//    shares
