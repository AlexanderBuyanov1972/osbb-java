package com.example.osbb.service.registry;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.*;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.dto.registry.*;
import com.example.osbb.entity.Ownership;
import com.example.osbb.entity.Owner;
import com.example.osbb.service.ownership.IOwnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RegistryService implements IRegistryService {
    @Autowired
    private IOwnershipService iOwnershipService;
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;

    @Override
    public Object getRegistryOwners() {
        try {
            return RegistryOwners
                    .builder()
                    .setFullNameOwnerAndListOwnership(getListFullNameOwnerAndListOwnership())
                    .buildingCharacteristics(getBuildingCharacteristics())
                    .build();
        } catch (Exception exception) {
            return new ResponseMessages(List.of(exception.getMessage()));
        }
    }

    private List<FullNameOwnerAndListOwnership> getListFullNameOwnerAndListOwnership() {
        List<FullNameOwnerAndListOwnership> result = new ArrayList<>();
        List<Owner> ownerList = ownerDAO.findAll();
        int count = 1;
        for (Owner one : ownerList) {
            result.add(FullNameOwnerAndListOwnership
                    .builder()
                    .id(count)
                    .fullNameOwner(getFullNameOwner(one))
                    .listOwnership(new ArrayList<>(one.getOwnerships()))
                    .build());
            count += 1;
        }

        return result.stream().sorted(comparatorString).collect(Collectors.toList());
    }
    Comparator<FullNameOwnerAndListOwnership> comparatorString =(a, b) -> a.getFullNameOwner().getFullNameOwner()
            .compareTo(b.getFullNameOwner().getFullNameOwner());

    private FullNameOwner getFullNameOwner(Owner owner) {
        return FullNameOwner
                .builder()
                .ownerId(owner.getId())
                .fullNameOwner(owner.getLastName() + " " + owner.getFirstName() + " " + owner.getSecondName())
                .build();
    }

    // --------------------------------------------------------------------------

    @Override
    public Object getRegistryOwnerships() {
        try {
            return RegistryOwnerships
                    .builder()
                    .setOwnershipAndListFullNameOwners(getSetOwnershipAndListFullNameOwner())
                    .buildingCharacteristics(getBuildingCharacteristics())
                    .build();
        } catch (Exception exception) {
            return new ResponseMessages(List.of(exception.getMessage()));
        }
    }

    private List<OwnershipAndListFullNameOwner> getSetOwnershipAndListFullNameOwner() {
        List<OwnershipAndListFullNameOwner> result = new ArrayList<>();
        int count = 1;
        for (Ownership ownership : ownershipDAO.findAll()) {
            result.add(OwnershipAndListFullNameOwner
                    .builder()
                    .id(count)
                    .ownership(ownership)
                    .listFullNameOwner(getListFullNameOwner(ownership))
                    .build());
            count += 1;
        }
        return result.stream()
                .sorted(comparatorInt)
                .collect(Collectors.toList());
    }
    Comparator<OwnershipAndListFullNameOwner> comparatorInt =
            (a, b) -> (int) a.getOwnership().getId() -(int) b.getOwnership().getId();

    // ------------- create strings -----------------------------

    private List<FullNameOwner> getListFullNameOwner(Ownership ownership) {
        return ownership.getOwners()
                .stream()
                .map(this::getFullNameOwner)
                .collect(Collectors.toList());
    }


    // ------------------- for all ---------------------
    private BuildingCharacteristics getBuildingCharacteristics() {
        return BuildingCharacteristics
                .builder()
                .countRooms(String.valueOf(iOwnershipService.countRooms()))
                .countApartment(String.valueOf(iOwnershipService.countApartment()))
                .countNonResidentialRoom(String.valueOf(iOwnershipService.countNonResidentialRoom()))
                .summaAreaRooms(String.valueOf(iOwnershipService.summaAreaRooms()))
                .summaAreaApartment(String.valueOf(iOwnershipService.summaAreaApartment()))
                .summaAreaNonResidentialRoom(String.valueOf(iOwnershipService.summaAreaNonResidentialRoom()))
                .addressDto(AddressDto.getAddressDto())
                .build();
    }

}
