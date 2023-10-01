package com.example.osbb.service.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.service.ServiceMessages;
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
                errors.add(ServiceMessages.ALREADY_EXISTS);
            if (userDAO.existsByUsername(user.getUsername()))
                errors.add(ServiceMessages.USERNAME_ALREADY_EXISTS);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            return errors.isEmpty() ? Response
                    .builder()
                    .data(userDAO.save(user))
                    .messages(List.of(ServiceMessages.OK))
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
                errors.add(ServiceMessages.NOT_EXISTS);
            if (!userDAO.existsByUsername(user.getUsername()))
                errors.add(ServiceMessages.USERNAME_NOT_EXISTS);
            user.setUpdatedAt(LocalDateTime.now());
            return errors.isEmpty() ? Response
                    .builder()
                    .data(userDAO.save(user))
                    .messages(List.of(ServiceMessages.OK))
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
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
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
                        .messages(List.of(ServiceMessages.OK))
                        .build();
            }
            return new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
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
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
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
                    .of(ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
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
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of(ServiceMessages.OK))
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
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public User getUser(String username) {
        return userDAO.existsByUsername(username) ? userDAO.findByUsername(username) : null;
    }

    // sorted ------------------------
    private List<User> returnListSorted(List<User> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
