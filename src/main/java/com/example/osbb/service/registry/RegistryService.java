package com.example.osbb.service.registry;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.*;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Record;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.apache.log4j.Logger;

import java.util.stream.Collectors;

@Service
public class RegistryService implements IRegistryService {
    private static final Logger log = Logger.getLogger(RegistryService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    private final String SUCCESSFULLY = "Операция прошла успешно";
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private RecordDAO recordDAO;


    @Override
    public Object getRegistryOwners() {
        String methodName = "getRegistryOwnerships";
        log.info(messageEnter(methodName));
        try {
            Map<Long, List<Record>> map = recordDAO.findAll()
                    .stream()
                    .collect(Collectors.filtering(s -> s.getOwner() != null && s.getOwner().isActive(),
                            Collectors.filtering(s -> s.getOwnership() != null,
                                    Collectors.groupingBy(s -> s.getOwner().getId(),
                                            Collectors.toList()))));
            log.info(SUCCESSFULLY);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(map.values())
                    .messages(List.of(SUCCESSFULLY))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getRegistryOwnerships() {
        String methodName = "getRegistryOwnerships";
        log.info(messageEnter(methodName));
        try {
            Map<String, List<Record>> map = recordDAO.findAll()
                    .stream()
                    .collect(
                            Collectors.filtering(s -> s.getOwner() != null && s.getOwner().isActive(),
                                    Collectors.filtering(s -> s.getOwnership() != null,
                                            Collectors.groupingBy(s -> s.getOwnership().getAddress().getApartment(),
                                                    Collectors.toList()))));
            log.info(SUCCESSFULLY);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(map.values().stream().sorted(comparatorListRecordByApartment()).collect(Collectors.toList()))
                    .messages(List.of(SUCCESSFULLY))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public BuildingCharacteristics getBuildingCharacteristics() {
        String methodName = "getBuildingCharacteristics";
        log.info(messageEnter(methodName));

        try {
            BuildingCharacteristics bc = BuildingCharacteristics
                    .builder()
                    .countOwners(ownerDAO.count())
                    .countRooms(ownershipDAO.count())
                    .countApartment(ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT))
                    .countNonResidentialRoom(ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                    .summaTotalArea(formatDoubleValue(ownershipDAO.findAll().stream().mapToDouble(Ownership::getTotalArea).sum()))
                    .summaTotalAreaApartment(formatDoubleValue(ownershipDAO.findAll().stream()
                            .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                            .mapToDouble(Ownership::getTotalArea).sum()))
                    .summaLivingAreaApartment(formatDoubleValue(ownershipDAO.findAll().stream()
                            .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                            .mapToDouble(Ownership::getLivingArea).sum()))
                    .summaTotalAreaNonResidentialRoom(formatDoubleValue(ownershipDAO.findAll().stream()
                            .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                            .mapToDouble(Ownership::getTotalArea).sum()))
                    .addressDto(AddressDto.getAddressDto())
                    .build();
            log.info(SUCCESSFULLY);
            log.info(messageExit(methodName));
            return bc;
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

    private Comparator<List<Record>> comparatorListRecordByApartment() {
        return (a, b) -> Integer.parseInt(a.get(0).getOwnership().getAddress().getApartment())
                - Integer.parseInt(b.get(0).getOwnership().getAddress().getApartment());
    }


    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
