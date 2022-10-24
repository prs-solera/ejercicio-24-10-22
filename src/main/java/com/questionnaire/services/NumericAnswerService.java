package com.questionnaire.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.NumericAnswer;
import com.questionnaire.model.User;
import com.questionnaire.repositories.NumericAnswerRepository;

@Component
public class NumericAnswerService {
    
    @Autowired
    private NumericAnswerRepository repository;

    public NumericAnswer save(NumericAnswer a) throws IllegalModelException {
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
        if (a.getAnswerval() < 1 || a.getAnswerval() > 10) {
            throw new IllegalModelException("Answer must be between 1 and 10");
        }

        a.setAnswerDatetime(LocalDate.now());
        
        return repository.save(a);
    }

    public void delete(NumericAnswer a) {
        repository.delete(a);
    }

    public NumericAnswer findById(Integer id) {
        Optional<NumericAnswer> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public List<NumericAnswer> findAll() {
        return repository.findAll();
    }

    public List<NumericAnswer> findByUser(User user) {
        return repository.findByUser(user);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}