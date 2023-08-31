package com.example.osbb.service.password;

import com.example.osbb.dao.PasswordDAO;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Password;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.consts.ObjectMessages;
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
                list.add(ObjectMessages.withSuchIdAlreadyExists("Password"));
            if (passwordDAO.existsByRegistrationNumberCardPayerTaxes(password.getRegistrationNumberCardPayerTaxes()))
                list.add(ObjectMessages.passwordWithSuchRegistrationNumberCardPayerTaxesAlreadyExists());
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
                list.add(ObjectMessages.withSuchIdNotExists("Password"));
            if (!passwordDAO.existsByRegistrationNumberCardPayerTaxes(password.getRegistrationNumberCardPayerTaxes()
            ))
                list.add(ObjectMessages.passwordWithSuchRegistrationNumberCardPayerTaxesNotExists());
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
                list.add(ObjectMessages.withSuchIdNotExists("Password"));
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
                return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
            }
            return new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Password")));
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
            return result.isEmpty() ? new ErrorResponseMessages(List.of(ObjectMessages.noObjectCreated("Password")))
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
            return result.isEmpty() ? new ErrorResponseMessages(List.of(ObjectMessages.noObjectUpdated("Password")))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllPassword() {
        try {
            List<Password> result = passwordDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of(ObjectMessages.listEmpty()))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object deleteAllPassword() {
        try {
            passwordDAO.deleteAll();
            return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes) {
        try {
            if (!passwordDAO.existsByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes))
                return new ErrorResponseMessages(List.of(ObjectMessages.passwordWithSuchRegistrationNumberCardPayerTaxesNotExists()));
            return passwordDAO.findByRegistrationNumberCardPayerTaxes(registrationNumberCardPayerTaxes);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Password> returnListSorted(List<Password> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
