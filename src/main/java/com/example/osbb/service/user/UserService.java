package com.example.osbb.service.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.osbb.dto.ErrorResponseMessages;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ResponseMessages;
import com.example.osbb.service.address.IAddressService;
import com.example.osbb.service.owner.IOwnerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.osbb.dao.UserDAO;
import com.example.osbb.entity.authorization.User;

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
    @Transactional
    public Object createUser(User user) {
        try {
            List<String> errors = new ArrayList<>();
            if (userDAO.existsById(user.getId()))
                errors.add("Пользователь с таким ID уже существует.");
            if (userDAO.existsByUsername(user.getUsername()))
                errors.add("Пользователь с таким USERNAME уже существует.");
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            return errors.isEmpty() ? Response
                    .builder()
                    .data(userDAO.save(user))
                    .messages(List.of("Пользователь создан успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateUser(User user) {
        try {
            List<String> errors = new ArrayList<>();
            if (!userDAO.existsById(user.getId()))
                errors.add("Пользователь с таким ID не существует.");
            if (!userDAO.existsByUsername(user.getUsername()))
                errors.add("Пользователь с таким USERNAME не существует.");
            user.setUpdatedAt(LocalDateTime.now());
            return errors.isEmpty() ? Response
                    .builder()
                    .data(userDAO.save(user))
                    .messages(List.of("Пользователь обновлён успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getUser(Long id) {
        try {
            return userDAO.existsById(id) ? Response
                    .builder()
                    .data(userDAO.findById(id).get())
                    .messages(List.of("Пользователь получен успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(List.of("Пользователь с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteUser(Long id) {
        try {
            if (userDAO.existsById(id)) {
                userDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Пользователь удалён успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Пользователь с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // ------------- all ------------------

    @Override
    @Transactional
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
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создано " + result.size() + " пользователя из " + users.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }


    @Override
    @Transactional
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
                    .of("Ни один из пользователей обновлён не был. Пользователей с такими ID не существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создано " + result.size() + " пользователя из " + users.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllUser() {
        try {
            List<User> result = userDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of("В базе данных нет ни одного пользователя по вашему запросу."))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of("Все пользователи получены успешно.", "Удачного дня!"))
                            .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllUser() {
        try {
            userDAO.deleteAll();
            return new ResponseMessages(List.of("Все пользователи успешно удалены."));
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
