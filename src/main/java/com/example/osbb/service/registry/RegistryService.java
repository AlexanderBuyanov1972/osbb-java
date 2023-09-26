package com.example.osbb.service.registry;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.*;
import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RegistryService implements IRegistryService {

    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;


    @Override
    public Object getRegistryOwners() {
        Map<String, List<String>> map = ownerDAO.findAll().stream()
                .collect(Collectors.groupingBy(s -> s.getLastName() + " " + s.getFirstName() + " " + s.getSecondName(),
                        Collectors.mapping(a -> a.getOwnership().getAddress().getApartment(),
                                Collectors.toList())));
        return Response
                .builder()
                .data(List.of(map, getBuildingCharacteristics()))
                .messages(List.of("Реестр собственников отправлен успешно.", "Удачного дня!"))
                .build();
    }

    @Override
    public Object getRegistryOwnerships() {
        Map<String, List<String>> map = ownershipDAO.findAll().stream()
                .collect(Collectors.groupingBy(s -> s.getAddress().getApartment(),
                        Collectors.mapping(Ownership::getOwner,
                                Collectors.mapping(s -> s.getLastName() + " " + s.getFirstName() + " " + s.getSecondName(),
                                        Collectors.toList()))));
        return Response
                .builder()
                .data(List.of(map, getBuildingCharacteristics()))
                .messages(List.of("Реестр собственников отправлен успешно.", "Удачного дня!"))
                .build();
    }


    // ------------------- for all ---------------------
    private BuildingCharacteristics getBuildingCharacteristics() {
        return BuildingCharacteristics
                .builder()
                .countRooms(ownershipDAO.count())
                .countApartment(ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT))
                .countNonResidentialRoom(ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                .summaAreaRooms(ownershipDAO.findAll().stream().mapToDouble(Ownership::getTotalArea).sum())
                .summaAreaApartment(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                        .mapToDouble(Ownership::getTotalArea).sum())
                .summaAreaLivingApartment(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                        .mapToDouble(Ownership::getLivingArea).sum())
                .summaAreaNonResidentialRoom(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                        .mapToDouble(Ownership::getTotalArea).sum())
                .addressDto(AddressDto.getAddressDto())
                .build();
    }

}
