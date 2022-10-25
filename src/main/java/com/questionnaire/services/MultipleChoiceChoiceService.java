package com.questionnaire.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.repositories.MultipleChoiceChoiceRepository;

@Component
public class MultipleChoiceChoiceService {
    
    @Autowired
    private MultipleChoiceChoiceRepository repository;

    public MultipleChoiceChoice save(MultipleChoiceChoice c) throws IllegalModelException {
        if (c.getTitle() == null || c.getTitle().equals("")) {
            throw new IllegalModelException("Title may not be null");
        }
        if (c.getQuestion() == null || c.getQuestion().getId().equals(0)) {
            throw new IllegalModelException("Question may not be null or unpersisted");
        }

        return repository.save(c);
    }

    public void delete(MultipleChoiceChoice c) {
        repository.delete(c);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public MultipleChoiceChoice findById(Integer id) {
        Optional<MultipleChoiceChoice> c = repository.findById(id);
        if (c.isPresent()) {
            return c.get();
        } else {
            return null;
        }
    }

    public List<MultipleChoiceChoice> findAll() {
        return repository.findAll();
    }

    public List<MultipleChoiceChoice> findByQuestion(MultipleChoiceQuestion q) {
        return repository.findByQuestion(q);
    }
}
