package com.questionnaire.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.repositories.MultipleChoiceQuestionRepository;

@Component
public class MultipleChoiceQuestionService {
    
    @Autowired
    private MultipleChoiceQuestionRepository repository;

    public MultipleChoiceQuestion save(MultipleChoiceQuestion q) throws IllegalModelException {
        if (q.getTitle() == null || q.getTitle().equals("")) {
            throw new IllegalModelException("Title cannot be null");
        }
        return repository.save(q);
    }

    public void delete(MultipleChoiceQuestion q) {
        repository.delete(q);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public MultipleChoiceQuestion findById(Integer id) {
        Optional<MultipleChoiceQuestion> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public List<MultipleChoiceQuestion> findAll() {
        return repository.findAll();
    }
}
