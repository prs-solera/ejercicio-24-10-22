package com.questionnaire.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.BooleanAnswer;
import com.questionnaire.model.User;
import com.questionnaire.repositories.BooleanAnswerRepository;

@Component
public class BooleanAnswerService {
    
    @Autowired
    private BooleanAnswerRepository repository;

    public BooleanAnswer save(BooleanAnswer a) throws IllegalModelException {
        if (a.getQuestion() == null ) {
            throw new IllegalModelException("Question is null");
        }  
        if (a.getUser() == null ) {
            throw new IllegalModelException("User is null");
        }  
        if (a.getQuestion().getId().equals(0) || a.getUser().getId().equals(0)) {
            throw new IllegalModelException("User and answer must be persisted");
        }
        if (!a.getId().equals(0)) {
            throw new IllegalModelException("Answers cannot be updated: create a new answer");
        }
        if(a.getAnswerval() == null) {
            throw new IllegalModelException("Answer must have a value");
        }

        a.setAnswerDatetime(LocalDate.now());
        
        return repository.save(a);
    }

    public void delete(BooleanAnswer a) {
        repository.delete(a);
    }

    public BooleanAnswer findById(Integer id) {
        Optional<BooleanAnswer> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public List<BooleanAnswer> findAll() {
        return repository.findAll();
    }

    public List<BooleanAnswer> findByUser(User user) {
        return repository.findByUser(user);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
