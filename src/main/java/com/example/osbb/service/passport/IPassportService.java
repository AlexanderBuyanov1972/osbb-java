package com.example.osbb.service.passport;

import com.example.osbb.entity.Passport;

import java.util.List;

public interface IPassportService {

    // ------------------- one -----------------------
    public Object createPassport(Passport passport);

    public Object updatePassport(Passport passport);

    public Object getPassport(Long id);

    public Object deletePassport(Long id);

    // ------------------ all ----------------

    public Object createAllPassport(List<Passport> list);

    public Object updateAllPassport(List<Passport> list);

    public Object getAllPassport();

    public Object deleteAllPassport();

    // ------------- street, house and apartment ----------------

    public Object findByRegistrationNumberCardPayerTaxes(String number);


}
