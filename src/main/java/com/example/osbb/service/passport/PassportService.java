package com.example.osbb.service.passport;

import com.example.osbb.dao.owner.PassportDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Passport;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassportService implements IPassportService {
    @Autowired
    private PassportDAO passportDAO;

    // ----------------- one -------------------------------

    @Override
    @Transactional
    public Object createPassport(Passport passport) {
        List<String> errors = new ArrayList<>();
        try {
            if (passportDAO.existsById(passport.getId()))
                errors.add(ServiceMessages.ALREADY_EXISTS);
            return errors.isEmpty() ?
                    Response.builder()
                            .data(passportDAO.save(passport))
                            .messages(List.of(ServiceMessages.OK))
                            .build()
                    : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updatePassport(Passport passport) {
        List<String> errors = new ArrayList<>();
        try {
            if (!passportDAO.existsById(passport.getId()))
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ? Response.builder()
                    .data(passportDAO.save(passport))
                    .messages(List.of(ServiceMessages.OK))
                    .build()
                    : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object getPassport(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (!passportDAO.existsById(id)) {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response.builder()
                    .data(passportDAO.findById(id).get())
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }


    @Override
    @Transactional
    public Object deletePassport(Long id) {
        try {
            if (passportDAO.existsById(id)) {
                passportDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of(ServiceMessages.OK))
                        .build();
            }
            return new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    // ---------------- all ----------------

    @Override
    @Transactional
    public Object createAllPassport(List<Passport> passports) {
        try {
            List<Passport> result = new ArrayList<>();
            for (Passport one : passports) {
                if (!passportDAO.existsById(one.getId())) {
                    passportDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(
                    List.of(ServiceMessages.NOT_CREATED))
                    : Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllPassport(List<Passport> passports) {
        try {
            List<Passport> result = new ArrayList<>();
            for (Passport one : passports) {
                if (passportDAO.existsById(one.getId())) {
                    passportDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(
                    List.of(ServiceMessages.NOT_UPDATED))
                    : Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllPassport() {
        try {
            List<Passport> result = passportDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteAllPassport() {
        try {
            passportDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // ИНН -----------------------------------
    @Override
    public Object findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        try {
            if (!passportDAO.existsByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes))
                return new ResponseMessages(List
                        .of(ServiceMessages.NOT_EXISTS));
            return Response.builder()
                    .data(passportDAO.findByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // sorted ------------------------------

    private List<Passport> returnListSorted(List<Passport> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
