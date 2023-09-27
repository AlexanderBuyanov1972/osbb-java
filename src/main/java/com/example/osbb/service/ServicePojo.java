package com.example.osbb.service;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;

import com.example.osbb.dto.pojo.*;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class ServicePojo {
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private OwnerDAO ownerDAO;

    // на одно помешение колличество клиентов (собственников)
    public PojoRoomAndListClients getPojoRoomAndListOwners(String apartment) {
        Ownership one = ownershipDAO.findByAddressApartment(apartment);
        List<Client> clients = ownershipDAO.findAll()
                .stream()
                .filter(s -> hasApartment(s, apartment))
                .map(Ownership::getOwner)
                .map(Client::new)
                .toList();
        return new PojoRoomAndListClients(new Room(one), clients);

    }

    // на одного клиента (собственника) количество помещений
    public PojoClientAndListRooms getPojoRoomAndListOwners(String fullName, LocalDate date) {
        String[] fios = fullName.trim().split(" ");
        Owner one = ownerDAO.findByLastNameAndFirstNameAndSecondNameAndDateBirth(fios[0], fios[1], fios[2], date);
        List<Room> rooms = ownershipDAO.findAll()
                .stream()
                .filter(s -> hasFullName(s, fullName) && hasDate(s, date))
                .map(Room::new)
                .toList();
        return new PojoClientAndListRooms(new Client(one), rooms);

    }

    // доля собственника в м2 в доме
    public Double getShareAreaFromHouseByFIOAndDateBirth(String fullName, LocalDate date) {
        return ownershipDAO.findAll()
                .stream()
                .filter(s -> hasFullName(s, fullName) && hasDate(s, date))
                .map(s -> s.getTotalArea() * s.getOwner().getShareInRealEstate())
                .reduce(0.00, Double::sum);
    }

    // список для подсчёта при опросе (голосовании)
//    Map<String, Map<TypeOfAnswer, Double>> mapCountArea =
//            generateShareTotalAreaQuestionAnswer(baseList)
//                    .stream()
//                    .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
//                            Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
//                                    Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));

    public Object getListForQuestionnaire() {
//        return ownershipDAO.findAll()
//                .stream()
//                .filter(s -> hasFullName(s, "Иванова Лидия Петровна"))
//                .collect(Collectors.groupingBy(s -> s.getOwner().getLastName() + " " + s.getOwner().getFirstName() + " " + s.getOwner().getSecondName(),
//                        Collectors.groupingBy(s -> s.getOwner().getDateBirth(),
//                                Collectors.groupingBy(s -> s.getAddress().getApartment(),
//                                        Collectors.summingDouble(s -> s.getOwner().getShareInRealEstate() * s.getTotalArea()
//                                        )))));

        return ownershipDAO.findAll()
                .stream()
                .filter(s -> hasFullName(s, "Иванова Лидия Петровна"))
                .map(s -> s.getAddress().getApartment() + " --->" + s.getOwner().getShareInRealEstate() + " ---> " + s.getTotalArea())
                .toList();


    }


    // filters ----------------------
    public boolean hasFullName(Ownership s, String fullName) {
        return (s.getOwner().getLastName() + " " +
                s.getOwner().getFirstName() + " " +
                s.getOwner().getSecondName()).equals(fullName);
    }

    public boolean hasDate(Ownership s, LocalDate date) {
        return s.getOwner().getDateBirth().isEqual(date);
    }

    public boolean hasApartment(Ownership s, String apartment) {
        return s.getAddress().getApartment().equals(apartment);
    }


}
