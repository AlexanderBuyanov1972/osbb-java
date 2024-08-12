package com.example.osbb.service.rate;

import com.example.osbb.dao.RateDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.Rate;
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
public class RateService implements IRateService {

    @Autowired
    private RateDAO rateDAO;

    // ---------------- one -----------------
    @Override
    public ResponseEntity<?> createRate(Rate rate) {
        String message = "Тариф с датой : " + rate.getDate() + " уже существует.";
        if (!rateDAO.existsByDate(rate.getDate())) {
            rate = rateDAO.save(rate);
            message = "Тариф с ID : " + rate.getId() + " создан успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(rate, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> updateRate(Rate rate) {
        String message = "Тариф с ID : " + rate.getId() + " не существует";
        if (rateDAO.existsById(rate.getId())) {
            rateDAO.delete(rate);
            rate = rateDAO.save(rate);
            message = "Тариф с ID : " + rate.getId() + " обновлён успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(rate, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getRate(Long id) {
        String message = "Тариф с ID : " + id + " не существует.";
        Rate rate = rateDAO.findById(id).orElse(null);
        if (rate == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Тариф с ID : " + id + " получен успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(rate, List.of(message)));
    }

    @Override
    public ResponseEntity<?> deleteRate(Long id) {
        String message = "Тариф с ID : " + id + " не существует";
        if (rateDAO.existsById(id)) {
            rateDAO.deleteById(id);
            message = "Тариф с ID : " + id + " удалён успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    // ------------------ all -----------------------
    @Override
    public ResponseEntity<?> createAllRate(List<Rate> rates) {
        String message = "Ни один из тарифов создан не был";
        List<Rate> result = new ArrayList<>();
        for (Rate rate : rates) {
            if (!rateDAO.existsById(rate.getId()) &&
                    !rateDAO.existsByDate(rate.getDate())) {
                rate = rateDAO.save(rate);
                log.info("Тариф с ID : {} создан успешно", rate.getId());
                result.add(rate);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Успешно создано " + result.size() + " тарифов из " + rates.size();
        log.info(message);
        return ResponseEntity.ok(new Response(sortedByLocalDate(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllRate(List<Rate> rates) {
        String message = "Не обновлён ни один тариф";
        List<Rate> result = new ArrayList<>();
        for (Rate rate : rates) {
            if (rateDAO.existsById(rate.getId())) {
                rate = rateDAO.save(rate);
                log.info("Тариф с ID : {} обновлён успешно", rate.getId());
                result.add(rate);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Успешно обновлено " + result.size() + " тарифов из " + rates.size();
        log.info(message);
        return ResponseEntity.ok(new Response(sortedByLocalDate(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllRate() {
        List<Rate> result = rateDAO.findAll();
        String message = "Tарифы получены успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedByLocalDate(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllRate() {
        rateDAO.deleteAll();
        String message = "Все тарифы успешно удалены";
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    private List<Rate> sortedById(List<Rate> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Rate> sortedByLocalDate(List<Rate> list) {
        return list.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).collect(Collectors.toList());
    }
}
