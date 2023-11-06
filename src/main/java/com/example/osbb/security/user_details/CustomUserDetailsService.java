package com.example.osbb.security.user_details;


import com.example.osbb.dao.UserDAO;
import com.example.osbb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDAO userDAO;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAO.findByEmail(email);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }


}
