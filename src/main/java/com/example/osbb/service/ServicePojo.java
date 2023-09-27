package com.example.osbb.service;

import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.PojoRoomAndListOwners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicePojo {
    @Autowired
    private OwnershipDAO ownershipDAO;

    public List<PojoRoomAndListOwners> getPojoRoomAndListOwners(){
        List<PojoRoomAndListOwners>  result = new ArrayList<>();
        ownershipDAO.findAll().stream().

    }
}
