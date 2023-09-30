package com.example.osbb.service.registry;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.*;
import com.example.osbb.dto.pojo.*;
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
        List<PojoClientAndListRooms> result = new ArrayList<>();
        Map<String, List<Room>> map = recordDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(s -> s.getOwner().getLastName() + " " + s.getOwner().getFirstName() + " " + s.getOwner().getSecondName(),
                        Collectors.filtering(s -> s.getOwnership() != null,
                                Collectors.mapping(s -> new Room(s.getOwnership()),
                                        Collectors.toList()))));
        for (String key : map.keySet()) {
            String[] fios = key.split(" ");
            result.add(new PojoClientAndListRooms(new Client(ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2])), map.get(key)));
        }
        return Response
                .builder()
                .data(result)
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    @Override
    public Object getRegistryOwnerships() {
        List<PojoRoomAndListClients> result = new ArrayList<>();
        Map<String, List<Client>> map = recordDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(s -> s.getOwnership().getAddress().getApartment(),
                        Collectors.filtering(s -> s.getOwner() != null,
                        Collectors.mapping(s -> new Client(s.getOwner()),
                                Collectors.toList()))));
        for (String key : map.keySet())
            result.add(new PojoRoomAndListClients(new Room(ownershipDAO.findByAddressApartment(key).stream().findFirst().get()), map.get(key)));
        return Response
                .builder()
                .data(sortRoomsByApartment(result))
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

    private List<PojoClientAndListRooms> returnListSortedByLastName(List<PojoClientAndListRooms> list) {
        return list.stream().sorted((a, b) -> a.getClient().getLastName().compareTo(b.getClient().getLastName())).collect(Collectors.toList());
    }

    private List<PojoRoomAndListClients> sortRoomsByApartment(List<PojoRoomAndListClients> list) {
        return list.stream().sorted((a, b) -> Integer.parseInt(a.getRoom().getApartment())
                - Integer.parseInt(b.getRoom().getApartment())).collect(Collectors.toList());
    }
}
