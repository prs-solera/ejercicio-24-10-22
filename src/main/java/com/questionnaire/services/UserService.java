package com.questionnaire.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.User;
import com.questionnaire.repositories.UserRepository;

@Component
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public User save(User user) throws IllegalModelException {
        if (user.getPassword() == null ||user.getPassword().equals("")) {
            throw new IllegalModelException("Password cannot be empty");
        }
        if (user.getUsername() == null || user.getUsername().equals("")) {
            throw new IllegalModelException("Username cannot be empty");
        }
        return repository.save(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public User findById(Integer id) {
        Optional<User> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
