package com.example.osbb.service.rate;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.RateDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.Rate;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RateService implements IRateService {
    private static final Logger log = Logger.getLogger(RateService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private RateDAO rateDAO;

    // ---------------- one -----------------
    @Override
    public Object createRate(Rate rate) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Тариф с датой : " + rate.getDate() + " уже существует.";
        try {
            if (!rateDAO.existsByDate(rate.getDate())) {
                rate = rateDAO.save(rate);
                messageResponse = "Тариф с ID : " + rate.getId() + " создан успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(rate, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object updateRate(Rate rate) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Тариф с ID : " + rate.getId() + " не существует";
        try {
            if (rateDAO.existsById(rate.getId())) {
                rateDAO.delete(rate);
                rate = rateDAO.save(rate);
                messageResponse = "Тариф с ID : " + rate.getId() + " обновлён успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(rate, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getRate(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Тариф с ID : " + id + " не существует.";
        try {
            Rate rate = rateDAO.findById(id).orElse(null);
            if (rate != null)
                messageResponse = "Тариф с ID : " + id + " получен успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(rate, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object deleteRate(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Тариф с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (rateDAO.existsById(id)) {
                rateDAO.deleteById(id);
                messageResponse = "Тариф с ID : " + id + " удалён успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(id, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // ------------------ all -----------------------
    @Override
    public Object createAllRate(List<Rate> rates) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Ни один из тарифов создан не был";
        log.info(messageEnter(methodName));
        try {
            List<Rate> result = new ArrayList<>();
            for (Rate rate : rates) {
                if (!rateDAO.existsById(rate.getId()) &&
                        !rateDAO.existsByDate(rate.getDate())) {
                    rate = rateDAO.save(rate);
                    log.info("Тариф с ID : " + rate.getId() + " создан успешно");
                    result.add(rate);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse :
                    "Успешно создано " + result.size() + " тарифов из " + rates.size();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(sortedByLocalDate(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllRate(List<Rate> rates) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Не обновлён ни один тариф";
        try {
            List<Rate> result = new ArrayList<>();
            for (Rate rate : rates) {
                if (rateDAO.existsById(rate.getId())) {
                    rate = rateDAO.save(rate);
                    log.info("Тариф с ID : " + rate.getId() + " обновлён успешно");
                    result.add(rate);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse :
                    "Успешно обновлено " + result.size() + " тарифов из " + rates.size();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(sortedByLocalDate(result), List.of(messageResponse));
        } catch (
                Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAllRate() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Rate> result = rateDAO.findAll();
            String messageResponse = "Tарифы получены успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(sortedByLocalDate(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteAllRate() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            rateDAO.deleteAll();
            String messageResponse = "Все тарифы успешно удалены";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    private List<Rate> sortedById(List<Rate> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Rate> sortedByLocalDate(List<Rate> list) {
        return list.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).collect(Collectors.toList());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
