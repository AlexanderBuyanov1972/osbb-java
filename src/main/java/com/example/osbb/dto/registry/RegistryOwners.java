package com.example.osbb.dto.registry;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistryOwners {
    private List<FullNameOwnerAndListOwnership> setFullNameOwnerAndListOwnership;
    private BuildingCharacteristics buildingCharacteristics;
}
