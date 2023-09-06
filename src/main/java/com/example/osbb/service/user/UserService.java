package com.example.osbb.service.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.service.address.IAddressService;
import com.example.osbb.service.owner.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.osbb.dao.UserDAO;
import com.example.osbb.entity.authorization.User;
import com.example.osbb.dto.messages.ErrorResponseMessages;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IAddressService iAddressService;

    // ----------- one -------------------
    @Override
    public Object createUser(User user) {
        try {
            List<String> list = new ArrayList<>();
            if (userDAO.existsById(user.getId()))
                list.add("Пользователь с таким ID уже существует.");
            if (userDAO.existsByUsername(user.getUsername()))
                list.add("Пользователь с таким USERNAME уже существует.");
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            return list.isEmpty() ? List.of(userDAO.save(user)) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateUser(User user) {
        try {
            List<String> list = new ArrayList<>();
            if (!userDAO.existsById(user.getId()))
                list.add("Пользователь с таким ID не существует.");
            if (!userDAO.existsByUsername(user.getUsername()))
                list.add("Пользователь с таким USERNAME не существует.");
            user.setUpdatedAt(LocalDateTime.now());
            return list.isEmpty() ? List.of(userDAO.save(user)) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getUser(Long id) {
        try {
            if (userDAO.existsById(id)) {
                return List.of(userDAO.findById(id).get());
            }
            return new ErrorResponseMessages(List.of("Пользователь с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object deleteUser(Long id) {
        try {
            if (userDAO.existsById(id)) {
                userDAO.deleteById(id);
                return new ResponseMessages(List.of("Пользователь удалён успешно."));
            }
            return new ErrorResponseMessages(List.of("Пользователь с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // ------------- all ------------------

    @Override
    public Object createAllUser(List<User> users) {
        try {
            List<User> result = new ArrayList<>();
            for (User user : users) {
                if (!userDAO.existsById(user.getId()) && !userDAO.existsByUsername(user.getUsername())) {
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());
                    user = userDAO.save(user);
                    result.add(user);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из пользователей создан не был. Пользователей с такими ID уже существуют."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }


    @Override
    public Object updateAllUser(List<User> users) {
        try {
            List<User> result = new ArrayList<>();
            for (User user : users) {
                if (userDAO.existsById(user.getId()) && userDAO.existsByUsername(user.getUsername())) {
                    user.setUpdatedAt(LocalDateTime.now());
                    userDAO.save(user);
                    result.add(user);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из пользователей обновлён не был. Пользователей с такими ID не существует."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllUser() {
        try {
            List<User> result = userDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных пользователей не существует."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object deleteAllUser() {
        try {
            userDAO.deleteAll();
            return new ResponseMessages(List.of("Пользователи успешно удалены."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public User getUser(String username) {
        return userDAO.existsByUsername(username) ? userDAO.findByUsername(username) : null;
    }

    private List<User> returnListSorted(List<User> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
