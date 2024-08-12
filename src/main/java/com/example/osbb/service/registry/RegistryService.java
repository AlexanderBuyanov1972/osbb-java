package com.example.osbb.service.registry;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.*;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Record;
import com.example.osbb.enums.TypeOfRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

@Slf4j
@Service
public class RegistryService implements IRegistryService {
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private RecordDAO recordDAO;

    @Override
    public ResponseEntity<?> getRegistryOwners() {
        Map<Long, List<Record>> map = recordDAO.findAll()
                .stream()
                .collect(Collectors.filtering(s -> s.getOwner() != null && s.getOwner().isActive(),
                        Collectors.filtering(s -> s.getOwnership() != null,
                                Collectors.groupingBy(s -> s.getOwner().getId(),
                                        Collectors.toList()))));
        String message = "Реестр объектов недвижимости получен успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(map.values(), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getRegistryOwnerships() {
        Map<String, List<Record>> map = recordDAO.findAll()
                .stream()
                .collect(
                        Collectors.filtering(s -> s.getOwner() != null && s.getOwner().isActive(),
                                Collectors.filtering(s -> s.getOwnership() != null,
                                        Collectors.groupingBy(s -> s.getOwnership().getAddress().getApartment(),
                                                Collectors.toList()))));
        List<List<Record>> lists = map.values().stream().sorted(comparatorRecordByApartment()).toList();
        String message = "Реестр собственников получен успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(lists, List.of(message)));
    }

    @Override
    public ResponseEntity<?> getBuildingCharacteristics() {
        BuildingCharacteristics bc = new BuildingCharacteristics(
                ownerDAO.count(),
                ownershipDAO.count(),
                ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT),
                ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM),

                formatDoubleValue(ownershipDAO.findAll().stream().mapToDouble(Ownership::getTotalArea).sum()),
                formatDoubleValue(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                        .mapToDouble(Ownership::getTotalArea).sum()),
                formatDoubleValue(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                        .mapToDouble(Ownership::getLivingArea).sum()),
                formatDoubleValue(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                        .mapToDouble(Ownership::getTotalArea).sum()),
                AddressDto.getAddressDto());
        String message = "Характеристики здания получены успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(bc, List.of(message)));
    }

    public Comparator<List<Record>> comparatorRecordByApartment() {
        return (a, b) -> Integer.parseInt(a.get(0).getOwnership().getAddress().getApartment())
                - Integer.parseInt(b.get(0).getOwnership().getAddress().getApartment());
    }

    public Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }
}
