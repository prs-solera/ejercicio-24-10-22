package com.questionnaire.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.model.User;
import com.questionnaire.repositories.UserRepository;

@Component
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }
}
