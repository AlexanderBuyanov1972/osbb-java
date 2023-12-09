package com.example.osbb.service;

import com.example.osbb.entity.Record;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Address;
import com.example.osbb.entity.ownership.Ownership;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Comparators {
    // records  ---------------------------------------
    public Comparator<List<Record>> comparatorRecordByApartment() {
        return (a, b) -> Integer.parseInt(a.get(0).getOwnership().getAddress().getApartment())
                - Integer.parseInt(b.get(0).getOwnership().getAddress().getApartment());
    }

    // addresses --------------------------------------
    public List<Address> sortedAddressById(List<Address> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }


    public List<Address> sortedByApartment(List<Address> list) {
        return list.stream()
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .collect(Collectors.toList());
    }

    // owners ------------------------------------------
    public List<Owner> sortedByLastName(List<Owner> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareTo(b.getLastName())).collect(Collectors.toList());
    }

    public Comparator<Owner> comparatorByLastName() {
        return (a, b) -> a.getLastName().compareTo(b.getLastName());
    }

    // ownerships -----------------------------------
    public List<Ownership> sortedOwnershipById(List<Ownership> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    public Comparator<Ownership> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getAddress().getApartment())
                - Integer.parseInt(b.getAddress().getApartment());
    }

    public Comparator<Ownership> comparatorByBill() {
        return (a, b) -> a.getBill().compareTo(b.getBill());
    }

}
