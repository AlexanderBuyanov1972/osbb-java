package com.example.osbb.service.passport;

import com.example.osbb.dao.owner.PassportDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Passport;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassportService implements IPassportService {
    private static final Logger log = Logger.getLogger(PassportService.class);
    @Autowired
    private PassportDAO passportDAO;

    // ----------------- one -------------------------------

    @Override
    @Transactional
    public Object createPassport(Passport passport) {
        log.info("Method createPassport : enter");
        try {
            passport = passportDAO.save(passport);
            log.info("Паспорт создан успешно");
            log.info("Method createPassport : exit");
            return Response.builder()
                    .data(passport)
                    .messages(List.of("Паспорт создан успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updatePassport(Passport passport) {
        log.info("Method updatePassport : enter");
        try {
            if (!passportDAO.existsById(passport.getId())) {
                log.info("Паспорт с ID : " + passport.getId() + " не существует");
                log.info("Method updatePassport : exit");
                return new ResponseMessages(List.of("Паспорт с ID : " + passport.getId() + " не существует"));
            }
            passport = passportDAO.save(passport);
            log.info("Паспорт обновлён успешно");
            log.info("Method updatePassport : exit");
            return Response.builder()
                    .data(passport)
                    .messages(List.of("Паспорт обновлён успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getPassport(Long id) {
        log.info("Method getPassport : enter");
        try {
            if (!passportDAO.existsById(id)) {
                log.info("Паспорт с ID : " + id + " не существует");
                log.info("Method getPassport : exit");
                return new ResponseMessages(List.of("Паспорт с ID : " + id + " не существует"));
            }
            Passport passport = passportDAO.findById(id).get();
            log.info("Паспорт получен успешно");
            log.info("Method getPassport : exit");
            return Response.builder()
                    .data(passport)
                    .messages(List.of("Паспорт получен успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePassport(Long id) {
        log.info("Method deletePassport : enter");
        try {
            if (passportDAO.existsById(id)) {
                passportDAO.deleteById(id);
                log.info("Паспорт удалён успешно");
                log.info("Method deletePassport : exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Паспорт удалён успешно"))
                        .build();
            }
            log.info("Паспорт с ID : " + id + " не существует");
            log.info("Method deletePassport : exit");
            return new ResponseMessages(List.of("Паспорт с ID : " + id + " не существует"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    // ---------------- all ----------------

    @Override
    @Transactional
    public Object createAllPassport(List<Passport> passports) {
        log.info("Method createAllPassport : enter");
        try {
            List<Passport> result = new ArrayList<>();
            for (Passport one : passports) {
                if (!passportDAO.existsById(one.getId())) {
                    one = passportDAO.save(one);
                    log.info("Паспорт с ID : " + one.getId() + " создан успешно");
                    result.add(one);
                }
            }
            if (result.isEmpty()) {
                log.info("Не создан ни один паспорт");
                log.info("Method createAllPassport : exit");
                return new ResponseMessages(List.of("Не создан ни один паспорт"));
            }
            log.info("Создано " + result.size() + " паспортов");
            log.info("Method createAllPassport : exit");
            return Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Создано " + result.size() + " паспортов"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllPassport(List<Passport> passports) {
        log.info("Method updateAllPassport : enter");
        try {
            List<Passport> result = new ArrayList<>();
            for (Passport one : passports) {
                if (passportDAO.existsById(one.getId())) {
                    one = passportDAO.save(one);
                    log.info("Паспорт с ID : " + one.getId() + " обновлён успешно");
                    result.add(one);
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлён ни один паспорт");
                log.info("Method updateAllPassport : exit");
                return new ResponseMessages(List.of("Не обновлён ни один паспорт"));
            }
            log.info("Обновлено " + result.size() + " паспортов");
            log.info("Method updateAllPassport : exit");
            return Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Обновлено " + result.size() + " паспортов"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getAllPassport() {
        log.info("Method getAllPassport : enter");
        try {
            List<Passport> result = passportDAO.findAll();
            log.info("Получено " + result.size() + " паспортов");
            log.info("Method getAllPassport : exit");
            return Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Получено " + result.size() + " паспортов"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteAllPassport() {
        log.info("Method deleteAllPassport : enter");
        try {
            passportDAO.deleteAll();
            log.info("Удалены все паспорта");
            log.info("Method deleteAllPassport : exit");
            return new ResponseMessages(List.of("Удалены все паспорта"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    // ИНН -----------------------------------
    @Override
    public Object findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        log.info("Method findByRegistrationNumberCardPayerTaxes : enter");
        try {
            if (!passportDAO.existsByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes)) {
                log.info("По данному ИНН " + registrationNumberCardPayerTaxes + " паспорт не найден");
                log.info("Method findByRegistrationNumberCardPayerTaxes : exit");
                return new ResponseMessages(List.of("По данному ИНН " + registrationNumberCardPayerTaxes + " паспорт не найден"));
            }
            log.info("Паспорт найден успешно");
            log.info("Method findByRegistrationNumberCardPayerTaxes : exit");
            return Response.builder()
                    .data(passportDAO.findByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes))
                    .messages(List.of("Паспорт найден успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    // sorted ------------------------------

    private List<Passport> returnListSorted(List<Passport> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
