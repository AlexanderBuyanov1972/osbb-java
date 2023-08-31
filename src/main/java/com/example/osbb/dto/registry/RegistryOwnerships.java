package com.example.osbb.dto.registry;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistryOwnerships {
    private List<OwnershipAndListFullNameOwner> setOwnershipAndListFullNameOwners;
    private BuildingCharacteristics buildingCharacteristics;
}
