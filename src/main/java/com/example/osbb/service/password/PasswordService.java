package com.example.osbb.service.password;

import com.example.osbb.dao.PasswordDAO;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Password;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasswordService implements IPasswordService {
    @Autowired
    private PasswordDAO passwordDAO;

    // ----------------- one -------------------------------

    @Override
    public Object createPassword(Password password) {
        List<String> list = new ArrayList<>();
        try {
            if (passwordDAO.existsById(password.getId()))
                list.add("Паспорт с таким ID уже существует.");
            if (passwordDAO.existsByRegistrationNumberCardPayerTaxes(password.getRegistrationNumberCardPayerTaxes()))
                list.add("Паспорт с таким ИНН уже существует.");
            return list.isEmpty() ? List.of(passwordDAO.save(password)) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updatePassword(Password password) {
        List<String> list = new ArrayList<>();
        try {
            if (!passwordDAO.existsById(password.getId()))
                list.add("Паспорт с таким ID не существует.");
            if (!passwordDAO.existsByRegistrationNumberCardPayerTaxes(password.getRegistrationNumberCardPayerTaxes()
            ))
                list.add("Паспорт с таким ИНН не существует.");
            return list.isEmpty() ? List.of(passwordDAO.save(password)) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object getPassword(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (!passwordDAO.existsById(id)) {
                list.add("Паспорт с таким ID не существует.");
            }
            return list.isEmpty() ? List.of(passwordDAO.findById(id).get()) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }


    @Override
    public Object deletePassword(Long id) {
        try {
            if (passwordDAO.existsById(id)) {
                passwordDAO.deleteById(id);
                return new ResponseMessages(List.of("Паспорт удалён успешно."));
            }
            return new ErrorResponseMessages(List.of("Паспорт с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    // ---------------- all ----------------444

    @Override
    public Object createAllPassword(List<Password> list) {
        try {
            List<Password> result = new ArrayList<>();
            for (Password one : list) {
                if (!passwordDAO.existsById(one.getId()) &&
                        !passwordDAO.existsByRegistrationNumberCardPayerTaxes(one.getRegistrationNumberCardPayerTaxes())) {
                    passwordDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ErrorResponseMessages(List
                    .of("Ни один из паспортов создан не был. Паспорта с такими ID уже существуют."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateAllPassword(List<Password> list) {
        try {
            List<Password> result = new ArrayList<>();
            for (Password one : list) {
                if (passwordDAO.existsById(one.getId())) {
                    passwordDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ErrorResponseMessages(List
                    .of("Ни один из паспортов обновлён не был. Паспорта с такими ID не существуют."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllPassword() {
        try {
            List<Password> result = passwordDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных паспортов не существует."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object deleteAllPassword() {
        try {
            passwordDAO.deleteAll();
            return new ResponseMessages(List.of("Паспорта удалены успешно."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        try {
            if (!passwordDAO.existsByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes))
                return new ErrorResponseMessages(List
                        .of("Паспорт с таким ИНН не существует."));
            return passwordDAO.findByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Password> returnListSorted(List<Password> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
