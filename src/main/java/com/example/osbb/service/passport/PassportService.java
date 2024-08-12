package com.example.osbb.service.passport;

import com.example.osbb.dao.owner.PassportDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.Passport;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PassportService implements IPassportService {

    @Autowired
    private PassportDAO passportDAO;

    // ----------------- one -------------------------------

    @Override
    @Transactional
    public ResponseEntity<?> createPassport(Passport passport) {
        passport = passportDAO.save(passport);
        String message = "Паспорт с ID : " + passport.getId() + " создан успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(passport, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePassport(Passport passport) {
        String message = "Паспорт с ID : " + passport.getId() + " не существует";
        if (passportDAO.existsById(passport.getId())) {
            passport = passportDAO.save(passport);
            message = "Паспорт с ID : " + passport.getId() + " обновлён успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(passport, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getPassport(Long id) {
        String message = "Паспорт с ID : " + id + " не существует";
        Passport passport = passportDAO.findById(id).orElse(null);
        if (passport != null) {
            message = "Паспорт с ID : " + id + " получен успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(passport, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));

    }

    @Override
    @Transactional
    public ResponseEntity<?> deletePassport(Long id) {
        String message = "Паспорт с ID : " + id + " не существует";
        if (passportDAO.existsById(id)) {
            passportDAO.deleteById(id);
            message = "Паспорт с ID : " + id + " удалён успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    // ---------------- all ----------------

    @Override
    @Transactional
    public ResponseEntity<?> createAllPassport(List<Passport> passports) {
        String message = "Не создан ни один паспорт";
        List<Passport> result = new ArrayList<>();
        for (Passport one : passports) {
            if (!passportDAO.existsById(one.getId())) {
                one = passportDAO.save(one);
                log.info("Паспорт с ID : {} создан успешно", one.getId());
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " паспортов";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllPassport(List<Passport> passports) {
        String message = "Не обновлён ни один паспорт";
        List<Passport> result = new ArrayList<>();
        for (Passport one : passports) {
            if (passportDAO.existsById(one.getId())) {
                one = passportDAO.save(one);
                log.info("Паспорт с ID : " + one.getId() + " обновлён успешно");
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " паспортов";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllPassport() {
        List<Passport> result = passportDAO.findAll();
        String message = "Получено " + result.size() + " паспортов";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllPassport() {
        String message = "Удалены все паспорта";
        passportDAO.deleteAll();
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    // ИНН -----------------------------------
    @Override
    public ResponseEntity<?> findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        String message = "По ИНН " + registrationNumberCardPayerTaxes + " паспорт не найден";
        Passport passport = passportDAO.findByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes);
        if (passport == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "По ИНН " + registrationNumberCardPayerTaxes + " паспорт найден успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(passport, List.of(message)));


    }

    // sorted ------------------------------

    private List<Passport> listSorted(List<Passport> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
