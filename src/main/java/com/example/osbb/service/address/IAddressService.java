package com.example.osbb.service.address;

import com.example.osbb.entity.Address;

import java.util.List;

public interface IAddressService {

    // ------------------- one -----------------------
    public Object createAddress(Address address);

    public Object updateAddress(Address address);

    public Object getAddress(Long id);
    public Object getAddressStart();

    public Object deleteAddress(Long id);

    // ------------------ all ----------------

    public Object createAllAddress(List<Address> list);

    public Object updateAllAddress(List<Address> list);

    public Object getAllAddress();

    public Object deleteAllAddress();

     //------------- street, house and apartment ----------------

    public Object getAddress(String street, String house, String apartment);

}
