package com.example.osbb.service;

import com.example.osbb.entity.owner.Owner;
import org.springframework.stereotype.Service;

@Service
public class FunctionHelp {
    public Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

    public String mapOwnerToFullName(Owner o) {
        return o.getLastName() + " " + o.getFirstName() + " " + o.getSecondName();
    }

}
