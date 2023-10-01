package com.example.osbb.service;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;

import com.example.osbb.dao.QuestionnaireDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.pojo.*;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;

import com.example.osbb.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServicePojo {
    @Autowired
    private RecordDAO recordDAO;
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private QuestionnaireDAO questionnaireDAO;


    // по конкретному Ф.И.О. перечень владения в доме :
    //  квартира ---> доля ---> общая площадь
    //  (apartment ---> share ---> total area)
    // check
    public Object getListQuestionnaireByFullName(String fullName) {
        return recordDAO.findAll()
                .stream()
                .filter(s -> isEqualsRecordByFullname(s, fullName))
                .map(s -> s.getOwnership().getAddress().getApartment()
                        + " --->" + s.getOwner().getShareInRealEstate()
                        + " ---> " + s.getOwnership().getTotalArea())
                .toList();


    }

    // Доля собственника по Ф.И.О. в м2 в доме.
    // Проход по всем записям, фильтруем по конкретному  Ф.И.О.,
    // получаем общую площадь и долю, перемножаем по каждой записи и
    // суммируем
    // check
    public Double getShareAreaFromHouseByFullName(String fullName) {
        return recordDAO.findAll()
                .stream()
                .filter(s -> isEqualsRecordByFullname(s, fullName))
                .map(s -> s.getOwnership().getTotalArea() * s.getOwner().getShareInRealEstate())
                .reduce(0.00, Double::sum);
    }

    // мапа ( ключ : Ф.И.О. и номер квартиры,  значение : сумма произведений  доли на общую площадь  по каждому помещению
    public Object getListClientAndTotalArea() {
        Map<String, Double> map = recordDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(s -> s.getOwner().getLastName()
                                + " " + s.getOwner().getFirstName()
                                + " " + s.getOwner().getSecondName()
                                + " ---> apartment --->" + s.getOwnership().getAddress().getApartment(),
                        Collectors.summingDouble(s -> s.getOwner().getShareInRealEstate() * s.getOwnership().getTotalArea()
                        )));
        return map;
    }

    // help functions -----------------------------
    boolean isEqualsRecordByFullname(Record s, String fullName) {
        return (s.getOwner().getLastName()
                + " " + s.getOwner().getFirstName()
                + " " + s.getOwner().getSecondName()).equals(fullName);
    }
}
