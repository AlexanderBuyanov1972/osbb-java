package com.example.osbb.service.password;

import com.example.osbb.entity.Password;

import java.util.List;
import java.util.Set;

public interface IPasswordService {

    // ------------------- one -----------------------
    public Object createPassword(Password password);

    public Object updatePassword(Password password);

    public Object getPassword(Long id);

    public Object deletePassword(Long id);

    // ------------------ all ----------------

    public Object createAllPassword(List<Password> list);

    public Object updateAllPassword(List<Password> list);

    public Object getAllPassword();

    public Object deleteAllPassword();

    // ------------- street, house and apartment ----------------

    public Object findByRegistrationNumberCardPayerTaxes(String number);


}
