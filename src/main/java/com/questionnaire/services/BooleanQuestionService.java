package com.questionnaire.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.BooleanQuestion;
import com.questionnaire.repositories.BooleanQuestionRepository;

@Component
public class BooleanQuestionService {
    
    @Autowired
    private BooleanQuestionRepository repository;

    public BooleanQuestion save(BooleanQuestion q) throws IllegalModelException {
        if (q.getTitle() == null || q.getTitle().equals("")) {
            throw new IllegalModelException("Tittle cannot be null");
        }
        return repository.save(q);
    }

    public void delete(BooleanQuestion q) {
        repository.delete(q);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public BooleanQuestion findById(Integer id) {
        Optional<BooleanQuestion> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public List<BooleanQuestion> findAll() {
        return repository.findAll();
    }
}
