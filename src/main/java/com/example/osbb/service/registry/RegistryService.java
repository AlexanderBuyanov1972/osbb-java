package com.example.osbb.service.registry;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.*;
import com.example.osbb.dto.pojo.*;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import com.example.osbb.service.ServiceMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistryService implements IRegistryService {

    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private RecordDAO recordDAO;


    @Override
    public Object getRegistryOwners() {
        List<ListRoomAndListClient> result = new ArrayList<>();
        Map<Long, List<Room>> map = recordDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(s -> s.getOwner().getId(),
                        Collectors.filtering(s -> s.getOwnership() != null,
                                Collectors.mapping(s -> new Room(s.getOwnership()),
                                        Collectors.toList()))));
        for (Long key : map.keySet()) {
            result.add(new ListRoomAndListClient(
                    map.get(key),
                    List.of(new Client(ownerDAO.findById(key).get()))));
        }
        return Response
                .builder()
                .data(result)
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    @Override
    public Object getRegistryOwnerships() {
        List<ListRoomAndListClient> result = new ArrayList<>();
        Map<Long, List<Client>> map = recordDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(s -> s.getOwnership().getId(),
                        Collectors.filtering(s -> s.getOwner() != null,
                                Collectors.mapping(s -> new Client(s.getOwner()),
                                        Collectors.toList()))));
        for (Long key : map.keySet())
            result.add(new ListRoomAndListClient(List.of(new Room(ownershipDAO.findById(key).get())), map.get(key)));
        return Response
                .builder()
                .data(result)
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    @Override
    public BuildingCharacteristics getBuildingCharacteristics() {
        return BuildingCharacteristics
                .builder()
                .countOwners(ownerDAO.count())
                .countRooms(ownershipDAO.count())
                .countApartment(ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT))
                .countNonResidentialRoom(ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                .summaTotalArea(ownershipDAO.findAll().stream().mapToDouble(Ownership::getTotalArea).sum())
                .summaTotalAreaApartment(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                        .mapToDouble(Ownership::getTotalArea).sum())
                .summaLivingAreaApartment(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                        .mapToDouble(Ownership::getLivingArea).sum())
                .summaTotalAreaNonResidentialRoom(ownershipDAO.findAll().stream()
                        .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                        .mapToDouble(Ownership::getTotalArea).sum())
                .addressDto(AddressDto.getAddressDto())
                .build();
    }

    // sorted -------------------
    private List<Room> sortRoomsByApartment(List<Room> list) {
        return list.stream().sorted((a, b) -> Integer.parseInt(a.getApartment())
                        - Integer.parseInt(b.getApartment()))
                .collect(Collectors.toList());
    }

    private List<Client> sortClientsByLastName(List<Client> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareToIgnoreCase(b.getLastName()))
                .collect(Collectors.toList());
    }
}
