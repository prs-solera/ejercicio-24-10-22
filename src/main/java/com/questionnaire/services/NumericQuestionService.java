package com.questionnaire.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.repositories.NumericQuestionRepository;

@Component
public class NumericQuestionService {
    
    @Autowired
    private NumericQuestionRepository repository;

    public NumericQuestion save(NumericQuestion q) throws IllegalModelException {
        if (q.getTitle() == null || q.getTitle().equals("")) {
            throw new IllegalModelException("Tittle cannot be null");
        }
        return repository.save(q);
    }

    public void delete(NumericQuestion q) {
        repository.delete(q);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public NumericQuestion findById(Integer id) {
        Optional<NumericQuestion> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public List<NumericQuestion> findAll() {
        return repository.findAll();
    }
}
