package com.questionnaire.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.MultipleChoiceAnswer;
import com.questionnaire.model.User;
import com.questionnaire.repositories.MultipleChoiceAnswerRepository;

@Component
public class MultipleChoiceAnswerService {
    @Autowired
    private MultipleChoiceAnswerRepository repository;

    public MultipleChoiceAnswer save(MultipleChoiceAnswer a) throws IllegalModelException {
        if (a.getChoice() == null || a.getChoice().getId().equals(0)) {
            throw new IllegalModelException("Choice may not be null or unpersisted");
        }
        if (a.getQuestion() == null || a.getQuestion().getId().equals(0)) {
            throw new IllegalModelException("Question may not be null or unpersisted");
        }
        if (a.getUser() == null || a.getUser().getId().equals(0)) {
            throw new IllegalModelException("User may not be null or unpersisted");
        }
        if (!a.getId().equals(0)) {
            throw new IllegalModelException("Cannot edit answers!");
        }
        a.setAnswerDatetime(LocalDate.now());

        return repository.save(a);
    }

    public void delete(MultipleChoiceAnswer a) {
        repository.delete(a);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public MultipleChoiceAnswer findById(Integer id) {
        Optional<MultipleChoiceAnswer> opt = repository.findById(id);
        if (opt.isPresent()){
            return opt.get();
        } else {
            return null;
        }
    }

    public List<MultipleChoiceAnswer> findAll() {
        return repository.findAll();
    }

    public List<MultipleChoiceAnswer> findByUser(User user) {
        return repository.findByUser(user);
    }
}
