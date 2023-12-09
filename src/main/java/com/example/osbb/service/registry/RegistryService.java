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
import com.example.osbb.service.Comparators;
import com.example.osbb.service.FunctionHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.apache.log4j.Logger;

import java.util.stream.Collectors;

@Service
public class RegistryService implements IRegistryService {
    private static final Logger log = Logger.getLogger(RegistryService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private RecordDAO recordDAO;
    @Autowired
    private FunctionHelp functionHelp;
    @Autowired
    private Comparators comparators;


    @Override
    public Object getRegistryOwners() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Map<Long, List<Record>> map = recordDAO.findAll()
                    .stream()
                    .collect(Collectors.filtering(s -> s.getOwner() != null && s.getOwner().isActive(),
                            Collectors.filtering(s -> s.getOwnership() != null,
                                    Collectors.groupingBy(s -> s.getOwner().getId(),
                                            Collectors.toList()))));
            String messageResponse = "Реестр объектов недвижимости получен успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(map.values(), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getRegistryOwnerships() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Map<String, List<Record>> map = recordDAO.findAll()
                    .stream()
                    .collect(
                            Collectors.filtering(s -> s.getOwner() != null && s.getOwner().isActive(),
                                    Collectors.filtering(s -> s.getOwnership() != null,
                                            Collectors.groupingBy(s -> s.getOwnership().getAddress().getApartment(),
                                                    Collectors.toList()))));
            List<List<Record>> lists = map.values().stream().sorted(comparators.comparatorRecordByApartment()).toList();
            String messageResponse = "Реестр собственников получен успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(lists, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getBuildingCharacteristics() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));

        try {
            BuildingCharacteristics bc = BuildingCharacteristics
                    .builder()
                    .countOwners(ownerDAO.count())
                    .countRooms(ownershipDAO.count())
                    .countApartment(ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT))
                    .countNonResidentialRoom(ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                    .summaTotalArea(functionHelp.formatDoubleValue(ownershipDAO.findAll().stream().mapToDouble(Ownership::getTotalArea).sum()))
                    .summaTotalAreaApartment(functionHelp.formatDoubleValue(ownershipDAO.findAll().stream()
                            .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                            .mapToDouble(Ownership::getTotalArea).sum()))
                    .summaLivingAreaApartment(functionHelp.formatDoubleValue(ownershipDAO.findAll().stream()
                            .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                            .mapToDouble(Ownership::getLivingArea).sum()))
                    .summaTotalAreaNonResidentialRoom(functionHelp.formatDoubleValue(ownershipDAO.findAll().stream()
                            .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                            .mapToDouble(Ownership::getTotalArea).sum()))
                    .addressDto(AddressDto.getAddressDto())
                    .build();
            String messageResponse = "Характеристики здания получены успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(bc, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }


    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
