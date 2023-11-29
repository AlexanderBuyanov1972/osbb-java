package com.example.osbb.service.passport;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.PassportDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
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
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private PassportDAO passportDAO;

    // ----------------- one -------------------------------

    @Override
    @Transactional
    public Object createPassport(Passport passport) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Паспорт создан успешно";
        log.info(messageEnter(methodName));
        try {
            passport = passportDAO.save(passport);
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response.builder()
                    .data(passport)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updatePassport(Passport passport) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Паспорт с ID : " + passport.getId() + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (passportDAO.existsById(passport.getId())) {
                passport = passportDAO.save(passport);
                messageResponse = "Паспорт с ID : " + passport.getId() + " обновлён успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response.builder()
                    .data(passport)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getPassport(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Паспорт с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            Passport passport = passportDAO.findById(id).orElse(null);
            if (passport != null)
                messageResponse = "Паспорт с ID : " + id + " получен успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response.builder()
                    .data(passport)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePassport(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Паспорт с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (passportDAO.existsById(id)) {
                passportDAO.deleteById(id);
                messageResponse = "Паспорт с ID : " + id + " удалён успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // ---------------- all ----------------

    @Override
    @Transactional
    public Object createAllPassport(List<Passport> passports) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не создан ни один паспорт";
        log.info(messageEnter(methodName));
        try {
            List<Passport> result = new ArrayList<>();
            for (Passport one : passports) {
                if (!passportDAO.existsById(one.getId())) {
                    one = passportDAO.save(one);
                    log.info("Паспорт с ID : " + one.getId() + " создан успешно");
                    result.add(one);
                }
            }
            if (!result.isEmpty())
                messageResponse = "Создано " + result.size() + " паспортов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response.builder()
                    .data(listSorted(result))
                    .messages(List.of())
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllPassport(List<Passport> passports) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не обновлён ни один паспорт";
        log.info(messageEnter(methodName));
        try {
            List<Passport> result = new ArrayList<>();
            for (Passport one : passports) {
                if (passportDAO.existsById(one.getId())) {
                    one = passportDAO.save(one);
                    log.info("Паспорт с ID : " + one.getId() + " обновлён успешно");
                    result.add(one);
                }
            }
            if (!result.isEmpty())
                messageResponse = "Обновлено " + result.size() + " паспортов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response.builder()
                    .data(listSorted(result))
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAllPassport() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Passport> result = passportDAO.findAll();
            String messageResponse = "Получено " + result.size() + " паспортов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteAllPassport() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Удалены все паспорта";
        log.info(messageEnter(methodName));
        try {
            passportDAO.deleteAll();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // ИНН -----------------------------------
    @Override
    public Object findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "По ИНН " + registrationNumberCardPayerTaxes + " паспорт не найден";
        log.info(messageEnter(methodName));
        try {
            Passport passport = passportDAO.findByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes);
            if (passport != null)
                messageResponse = "По ИНН " + registrationNumberCardPayerTaxes + " паспорт найден успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response.builder()
                    .data(passport)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // sorted ------------------------------

    private List<Passport> listSorted(List<Passport> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
