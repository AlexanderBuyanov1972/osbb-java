package com.example.osbb.dto.pojo;

import com.example.osbb.entity.Owner;
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
    private Double shareInRealEstate;
    private TypeOfFamilyStatus familyStatus;
    private TypeOfBeneficiary beneficiary;

    public Client(Owner one) {
        this.id = one.getId();
        this.firstName = one.getFirstName();
        this.secondName = one.getSecondName();
        this.lastName = one.getLastName();
        this.gender = one.getGender();
        this.email = one.getEmail();
        this.phoneNumber = one.getPhoneNumber();
        this.dateBirth = one.getDateBirth();
        this.shareInRealEstate = one.getShareInRealEstate();
        this.familyStatus = one.getFamilyStatus();
        this.beneficiary = one.getBeneficiary();
    }
}
