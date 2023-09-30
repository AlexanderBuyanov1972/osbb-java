package com.example.osbb.service;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;

import com.example.osbb.dao.QuestionnaireDAO;
import com.example.osbb.dto.pojo.*;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;

import com.example.osbb.entity.Questionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServicePojo {
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private QuestionnaireDAO questionnaireDAO;

    // на одно помешение колличество клиентов (собственников)
    //check
    public PojoRoomAndListClients getPojoRoomAndListClients(String apartment) {
        Ownership one = ownershipDAO.findByAddressApartment(apartment).stream().findFirst().get();
        List<Client> clients = ownershipDAO.findAll()
                .stream()
                .filter(s -> hasApartment(s, apartment))
                .map(Ownership::getOwner)
                .map(Client::new)
                .toList();
        return new PojoRoomAndListClients(new Room(one), clients);

    }

    // на одного клиента (собственника) количество помещений
    // check
    public PojoClientAndListRooms getPojoClientAndListRooms(String fullName) {
        String[] fios = fullName.trim().split(" ");
        Owner one = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);
        List<Room> rooms = ownershipDAO.findAll()
                .stream()
                .filter(s -> hasFullName(s, fullName))
                .map(Room::new)
                .toList();
        return new PojoClientAndListRooms(new Client(one), rooms);

    }

    // подготовка листа для расчёта опроса по фио
    // check
    public Object getListQuestionnaireByFullName(String fullName) {
        return ownershipDAO.findAll()
                .stream()
                .filter(s -> hasFullName(s, fullName))
                .map(s -> s.getAddress().getApartment() + " --->" + s.getOwner().getShareInRealEstate() + " ---> " + s.getTotalArea())
                .toList();


    }

    // доля собственника в м2 в доме
    //check
    public Double getShareAreaFromHouseByFIO(String fullName) {
        return ownershipDAO.findAll()
                .stream()
                .filter(s -> hasFullName(s, fullName))
                .map(s -> s.getTotalArea() * s.getOwner().getShareInRealEstate())
                .reduce(0.00, Double::sum);
    }

    public Object getListClientAndTotalArea() {
        Map<String, Double> map = ownershipDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(s -> s.getOwner().getLastName() + " " + s.getOwner().getFirstName() + " " + s.getOwner().getSecondName()
                                + " ---> apartment --->" + s.getAddress().getApartment(),
                        Collectors.summingDouble(s -> s.getOwner().getShareInRealEstate() * s.getTotalArea()
                        )));
        return map;
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
