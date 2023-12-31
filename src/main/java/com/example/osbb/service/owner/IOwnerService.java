package com.example.osbb.service.owner;

import com.example.osbb.entity.owner.Owner;

import java.util.List;

public interface IOwnerService {

    // one
    public Object createOwner(Owner owner);

    public Object updateOwner(Owner owner);


    public Object getOwner(Long id);

    public Object deleteOwner(Long id);

    // all

    public Object createAllOwner(List<Owner> owners);

    public Object updateAllOwner(List<Owner> owners);

    public Object getAllOwner();

    public Object deleteAllOwner();

    // count
    public Object countOwners();

    // get owner by full name
    public Object getOwnerByFullName(String fullName);


}
