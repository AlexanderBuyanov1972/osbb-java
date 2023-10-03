package com.example.osbb.dto.pojo;

import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;
import com.example.osbb.entity.Share;
import com.example.osbb.enums.TypeOfBeneficiary;
import com.example.osbb.enums.TypeOfFamilyStatus;
import com.example.osbb.enums.TypeOfGender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    private long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private TypeOfGender gender;
    private String email;
    private String phoneNumber;
    private LocalDate dateBirth;
    private TypeOfFamilyStatus familyStatus;
    private TypeOfBeneficiary beneficiary;
    private String photoName;
    private Double share;

    public Client(Owner one, Double share) {
        this.id = one.getId();
        this.firstName = one.getFirstName();
        this.secondName = one.getSecondName();
        this.lastName = one.getLastName();
        this.gender = one.getGender();
        this.email = one.getEmail();
        this.phoneNumber = one.getPhoneNumber();
        this.dateBirth = one.getDateBirth();
        this.familyStatus = one.getFamilyStatus();
        this.beneficiary = one.getBeneficiary();
        this.photoName = one.getPhoto().getName();
        this.share = share;
    }
}
