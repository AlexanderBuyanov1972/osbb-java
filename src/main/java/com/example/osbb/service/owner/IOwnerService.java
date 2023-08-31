package com.example.osbb.service.owner;

import com.example.osbb.entity.Owner;

import java.util.List;

public interface IOwnerService {

    // ----- one -----
    public Object createOwner(Owner owner);

    public Object updateOwner(Owner owner);


    public Object getOwner(Long id);

    public Object deleteOwner(Long id);

    // ----- all -----

    public Object createAllOwner(List<Owner> owners);

    public Object updateAllOwner(List<Owner> owners);

    public Object getAllOwner();

    public Object deleteAllOwner();

    public Object getListOwnerByListId(List<Long> ids);


}
