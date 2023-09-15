package com.example.osbb.service.select;

import com.example.osbb.dao.SelectDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Address;
import com.example.osbb.entity.Select;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectService implements ISelectService {
    @Autowired
    SelectDAO selectDAO;

    @Override
    @Transactional
    public Object createSelect(Select select) {
        List<String> errors = new ArrayList<>();
        try {
            if (selectDAO.existsById(select.getId())) {
                errors.add("Выбор с таким ID уже существует.");
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(selectDAO.save(select))
                    .messages(List.of("Создание выбора прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateSelect(Select select) {
        List<String> errors = new ArrayList<>();
        try {
            if (!selectDAO.existsById(select.getId())) {
                errors.add("Выбор с таким ID не существует.");
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(selectDAO.save(select))
                    .messages(List.of("Обновление выбора прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getSelect(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (!selectDAO.existsById(id)) {
                errors.add("Выбор с таким ID не существует.");
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(selectDAO.findById(id).get())
                    .messages(List.of("Получение выбора прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteSelect(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (selectDAO.existsById(id)) {
                selectDAO.deleteById(id);
            } else {
                list.add("Выбор с таким ID не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of("Удаление выбора прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object createAllSelect(List<Select> selects) {
        List<Select> result = new ArrayList<>();
        try {
            for (Select one : selects) {
                if (!selectDAO.existsById(one.getId())) {
                    result.add(selectDAO.save(one));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из выборов создан не был. Выборы с такими ID уже существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создан " + result.size() + " выбор из " + selects.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllSelect(List<Select> selects) {
        List<Select> result = new ArrayList<>();
        try {
            for (Select one : selects) {
                if (selectDAO.existsById(one.getId())) {
                    result.add(selectDAO.save(one));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из выборов обновлён не был. Выборы с такими ID не существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлён " + result.size() + " выбор из " + selects.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllSelect() {
        try {
            List<Select> result = selectDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of("В базе данных нет ни одного опросного листа по вашему запросу."))
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
    public Object deleteAllSelect() {
        try {
            selectDAO.deleteAll();
            return new ResponseMessages(List.of("Все опросные листы удалены успешно.", "Удачного дня!"));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    private List<Select> returnListSorted(List<Select> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
