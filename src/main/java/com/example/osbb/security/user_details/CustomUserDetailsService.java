package com.example.osbb.security.user_details;


import com.example.osbb.security.dao.UserDAO;
import com.example.osbb.security.entity.User;
import com.example.osbb.security.jwt.filters.AuthenticationFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = Logger.getLogger(AuthenticationFilter.class);
    @Autowired
    private UserDAO userDAO;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        User user = userDAO.findByEmail(email);
        log.info("User user = " + user);
        log.info(messageExit(methodName));
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }


}
