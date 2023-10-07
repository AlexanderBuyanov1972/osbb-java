package com.example.osbb.service.rate;

import com.example.osbb.dao.RateDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.Rate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RateService implements IRateService {
    @Autowired
    private RateDAO rateDAO;

    // ---------------- one -----------------

    @Override
    public Object createRate(Rate rate) {
        try {
            List<String> errors = new ArrayList<>();
            if (rateDAO.existsById(rate.getId()))
                errors.add("Тариф с таким ID уже существует.");
            if (rateDAO.existsByDate(rate.getDate()))
                errors.add("Тариф с такой датой уже существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(rateDAO.save(rate))
                    .messages(List.of("Тариф создан успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateRate(Rate rate) {
        try {
            List<String> errors = new ArrayList<>();
            if (!rateDAO.existsById(rate.getId()))
                errors.add("Тариф с таким ID не существует.");
            if (!rateDAO.existsByDate(rate.getDate()))
                errors.add("Тариф с такой датой не существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(rateDAO.save(rate))
                    .messages(List.of("Тариф создан успешно."))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object getRate(Long id) {
        try {
            if (rateDAO.existsById(id)) {
                return Response
                        .builder()
                        .data(rateDAO.findById(id).orElse(new Rate()))
                        .messages(List.of("Тариф получен успешно."))
                        .build();
            }
            return new ResponseMessages(List.of("Тариф с таким ID уже существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object deleteRate(Long id) {
        try {
            if (rateDAO.existsById(id)) {
                rateDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Тариф удален успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Роль с таким ID уже существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // ------------------ all -----------------------

    @Override
    @Transactional
    public Object createAllRate(List<Rate> rates) {
        try {
            List<Rate> result = new ArrayList<>();
            for (Rate rate : rates) {
                if (!rateDAO.existsById(rate.getId())) {
                    rateDAO.save(rate);
                    result.add(rate);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из тарифов создан не был. Тарифы с такими ID уже существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создано " + result.size() + " тарифов из " + rates.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllRate(List<Rate> rates) {
        try {
            List<Rate> result = new ArrayList<>();
            for (Rate rate : rates) {
                if (rateDAO.existsById(rate.getId())) {
                    rateDAO.save(rate);
                    result.add(rate);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из тарифов обновлён не был. Тарифы с такими ID не существует."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлено " + result.size() + " тарифов из " + rates.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllRate() {
        try {
            List<Rate> result = rateDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных тарифов не существует."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Все тарифы получены успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteAllRate() {
        try {
            rateDAO.deleteAll();
            return new ResponseMessages(List.of("Все тарифы успешно удалены."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Rate> returnListSorted(List<Rate> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
