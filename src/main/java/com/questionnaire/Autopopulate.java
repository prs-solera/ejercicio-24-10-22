package com.questionnaire;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.questionnaire.model.User;
import com.questionnaire.services.UserService;

@Component
public class Autopopulate {
    
    @Autowired
    private UserService userService;

    @PostConstruct
    public void auto() {
        User user = new User();

        List<String> roles = new ArrayList<String>();
        roles.add("CUSTOMER");
        roles.add("ADMIN");

        user.setId(0);
        user.setUsername("dave");
        user.setPassword("1234");
        user.setRoles(roles);

        userService.save(user);
    }
}
