package com.example.osbb.service.registry;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dao.ShareDAO;
import com.example.osbb.dto.*;
import com.example.osbb.dto.pojo.*;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Record;
import com.example.osbb.entity.Share;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import org.apache.log4j.Logger;
import java.util.stream.Collectors;

@Service
public class RegistryService implements IRegistryService {
    private static final Logger log = Logger.getLogger(RegistryService.class);
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private RecordDAO recordDAO;
    @Autowired
    private ShareDAO shareDAO;


    @Override
    public Object getRegistryOwners() {
        log.info("Method getRegistryOwners - enter");
        try {
            List<ListRoomAndListClient> result = new ArrayList<>();
            Map<Long, List<Room>> map = recordDAO.findAll()
                    .stream()
                    .collect(Collectors.filtering(s -> s.getOwner().isActive(),
                            Collectors.groupingBy(s -> s.getOwner().getId(),
                                    Collectors.filtering(s -> s.getOwnership() != null,
                                            Collectors.mapping(s ->
                                                            new Room(s.getOwnership(),
                                                                    getShareFromRecord(s)),
                                                    Collectors.toList())))));
            for (Long key : map.keySet()) {
                result.add(new ListRoomAndListClient(
                        map.get(key),
                        List.of(new Client(
                                ownerDAO.findById(key).orElse(new Owner()),
                                0.00))));
            }
            log.info("Операция прошла успешно");
            log.info("Method getRegistryOwners - exit");
            return Response
                    .builder()
                    .data(result)
                    .messages(List.of("Операция прошла успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getRegistryOwnerships() {
        log.info("Method getRegistryOwnerships - enter");
        try {
            List<ListRoomAndListClient> result = new ArrayList<>();
            Map<Long, List<Client>> map = recordDAO.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(s -> s.getOwnership().getId(),
                            Collectors.filtering(s -> s.getOwner() != null,
                                    Collectors.filtering(s -> s.getOwner().isActive(),
                                            Collectors.mapping(s -> new Client(s.getOwner(),
                                                            getShareFromRecord(s)),
                                                    Collectors.toList())))));
            for (Long key : map.keySet())
                result.add(new ListRoomAndListClient(List.of(new Room(
                        ownershipDAO.findById(key).orElse(new Ownership())
                        , 0.00)), map.get(key)));
            log.info("Операция прошла успешно");
            log.info("Method getRegistryOwnerships - exit");
            return Response
                    .builder()
                    .data(result)
                    .messages(List.of("Операция прошла успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    private Double getShareFromRecord(Record r) {
        return shareDAO.findAll()
                .stream()
                .filter(s -> s.getOwnership().equals(r.getOwnership()))
                .filter(s -> s.getOwner().equals(r.getOwner()))
                .map(Share::getValue).findAny().orElse(0.00);
    }

    @Override
    public BuildingCharacteristics getBuildingCharacteristics() {
        log.info("Method getBuildingCharacteristics - enter");
        try {
            return BuildingCharacteristics
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

}
