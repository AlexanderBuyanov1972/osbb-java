package com.example.osbb.service.user;

import java.util.List;

import com.example.osbb.entity.authorization.User;

public interface IUserService {

    // ---------- one --------

    public Object createUser(User user);

    public Object updateUser(User user);

    public Object getUser(Long id);

    public Object deleteUser(Long id);

    // --------- all ---------

    public Object createAllUser(List<User> users);

    public Object updateAllUser(List<User> users);

    public Object getAllUser();

    public Object deleteAllUser();

    // ------- for dao -------

    public User getUser(String username);

}
