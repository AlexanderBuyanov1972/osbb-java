package com.example.osbb.service.rate;

import com.example.osbb.dao.RateDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
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
    @Autowired
    private RateDAO rateDAO;

    // ---------------- one -----------------
    @Override
    public Object createRate(Rate rate) {
        log.info("Method createRate : enter");
        try {
            List<String> errors = new ArrayList<>();
            if (rateDAO.existsByDate(rate.getDate())) {
                log.info("Тариф с датой : " + rate.getDate() + " уже существует.");
                errors.add("Тариф с датой : " + rate.getDate() + " уже существует.");
            }
            rate = rateDAO.save(rate);
            log.info("Тариф создан успешно.");
            log.info("Method createRate : exit");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(rate)
                    .messages(List.of("Тариф создан успешно."))
                    .build() : new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object updateRate(Rate rate) {
        log.info("Method updateRate : enter");
        try {
            List<String> errors = new ArrayList<>();
            if (!rateDAO.existsById(rate.getId())) {
                log.info("Тариф с ID : " + rate.getId() + " не существует.");
                errors.add("Тариф с ID : " + rate.getId() + " не существует.");
            }
            rateDAO.delete(rate);
            rate = rateDAO.save(rate);
            log.info("Тариф обновлён успешно.");
            log.info("Method updateRate : exit");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(rate)
                    .messages(List.of("Тариф обновлён успешно."))
                    .build() : new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getRate(Long id) {
        log.info("Method getRate : enter");
        try {
            if (rateDAO.existsById(id)) {
                log.info("Method getRate : exit");
                return Response
                        .builder()
                        .data(rateDAO.findById(id).get())
                        .messages(List.of("Тариф получен успешно."))
                        .build();
            }
            log.info("Method getRate : exit");
            return new ResponseMessages(List.of("Тариф с ID : " + id + " не существует."));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object deleteRate(Long id) {
        log.info("Method deleteRate : enter");
        try {
            if (rateDAO.existsById(id)) {
                rateDAO.deleteById(id);
                log.info("Тариф удален успешно.");
                log.info("Method deleteRate : exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Тариф удален успешно."))
                        .build();
            }
            log.info("Тариф с ID : " + id + " уже существует.");
            log.info("Method deleteRate : exit");
            return new ResponseMessages(List.of("Тариф с ID : " + id + " уже существует."));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    // ------------------ all -----------------------
    @Override
    public Object createAllRate(List<Rate> rates) {
        log.info("Method createAllRate : enter");
        try {
            List<Rate> result = new ArrayList<>();
            for (Rate rate : rates) {
                if (!rateDAO.existsById(rate.getId()) &&
                        !rateDAO.existsByDate(rate.getDate())) {
                    rate = rateDAO.save(rate);
                    log.info("Тариф с ID : " + rate.getId() + " создан успешно");
                    log.info("Тариф сохранён успешно");
                    result.add(rate);
                }
            }
            log.info("Ни один из тарифов создан не был");
            log.info("Тарифы с такими ID уже существуют или...");
            log.info("Тарифы с такими датами уже существуют");
            log.info("Method createAllRate : exit");
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из тарифов создан не был",
                            "Тарифы с такими ID уже существуют или...",
                            "Тарифы с такими датами уже существуют"))
                    : Response
                    .builder()
                    .data(listSortedByLocalDate(result))
                    .messages(List.of("Успешно создано " + result.size() + " тарифов из " + rates.size()))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllRate(List<Rate> rates) {
        log.info("Method updateAllRate : enter");
        try {
            List<Rate> result = new ArrayList<>();
            for (Rate rate : rates) {
                if (rateDAO.existsById(rate.getId())) {
                    rate = rateDAO.save(rate);
                    log.info("Тариф с ID : " + rate.getId() + " обновлён успешно");
                    result.add(rate);
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлён ни один тариф");
                log.info("Method updateAllRate : exit");
                return new ResponseMessages(List.of("Не обновлён ни один тариф"));
            }
            log.info("Успешно обновлено " + result.size() + " тарифов из " + rates.size());
            log.info("Method updateAllRate : exit");
            return Response
                    .builder()
                    .data(listSortedByLocalDate(result))
                    .messages(List.of("Успешно обновлено " + result.size() + " тарифов из " + rates.size()))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getAllRate() {
        log.info("Method getAllRate : enter");
        try {
            List<Rate> result = rateDAO.findAll();
            log.info("Tарифы получены успешно");
            log.info("Method getAllRate : exit");
            return Response
                    .builder()
                    .data(listSortedByLocalDate(result))
                    .messages(List.of("Tарифы получены успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteAllRate() {
        log.info("Method deleteAllRate : enter");
        try {
            rateDAO.deleteAll();
            log.info("Все тарифы успешно удалены.");
            log.info("Method deleteAllRate : exit");
            return new ResponseMessages(List.of("Все тарифы успешно удалены."));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    private List<Rate> listSortedById(List<Rate> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Rate> listSortedByLocalDate(List<Rate> list) {
        return list.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).collect(Collectors.toList());
    }
}
