package com.example.osbb.service.passport;

import com.example.osbb.dao.PassportDAO;
import com.example.osbb.dto.ErrorResponseMessages;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ResponseMessages;
import com.example.osbb.entity.Passport;
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
                errors.add("Паспорт с таким ID уже существует.");
            return errors.isEmpty() ?
                    Response.builder()
                            .data(passportDAO.save(passport))
                            .messages(List.of("Паспорт успешно создан.", "Удачного дня!"))
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
                errors.add("Паспорт с таким ID не существует.");
            return errors.isEmpty() ? Response.builder()
                    .data(passportDAO.save(passport))
                    .messages(List.of("Паспорт успешно обновлён.", "Удачного дня!"))
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
                errors.add("Паспорт с таким ID не существует.");
            }
            return errors.isEmpty() ? Response.builder()
                    .data(passportDAO.findById(id).get())
                    .messages(List.of("Паспорт успешно получен.", "Удачного дня!"))
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
                        .messages(List.of("Паспорт удалён успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Паспорт с таким ID не существует."));
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
                    List.of("Ни один из паспортов создан не был. Паспорта с такими ID уже существуют."))
                    : Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создан " + result.size() + " паспорт из " + passports.size() + ".", "Удачного дня!"))
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
                    List.of("Ни один из паспортов обновлён не был. Паспорта с такими ID не существуют."))
                    : Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлён " + result.size() + " паспорт из " + passports.size() + ".", "Удачного дня!"))
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
                    new ResponseMessages(List.of("В базе данных нет ни одного паспорта по вашему запросу."))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of("Запрос выполнен успешно.", "Удачного дня!"))
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
            return new ResponseMessages(List.of("Все паспорта удалены успешно.", "Удачного дня!"));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        try {
            if (!passportDAO.existsByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes))
                return new ResponseMessages(List
                        .of("Паспорт с таким ИНН не существует."));
            return Response.builder()
                    .data(passportDAO.findByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes))
                    .messages(List.of("Запрос паспорта по ИНН выполнен успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Passport> returnListSorted(List<Passport> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
