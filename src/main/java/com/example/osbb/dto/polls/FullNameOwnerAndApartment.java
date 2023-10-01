package com.example.osbb.dto.polls;

import com.example.osbb.entity.Record;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FullNameOwnerAndApartment {
    private String fullName;
    private Double shareInRealEstate;
    private String apartment;
    private Double totalArea;

    public FullNameOwnerAndApartment(Record r) {
        this.fullName = r.getOwner().getLastName() + " " + r.getOwner().getFirstName() + " " + r.getOwner().getSecondName();
        this.shareInRealEstate = r.getOwner().getShareInRealEstate();
        this.apartment = r.getOwnership().getAddress().getApartment();
        this.totalArea = r.getOwnership().getTotalArea();
    }


}
