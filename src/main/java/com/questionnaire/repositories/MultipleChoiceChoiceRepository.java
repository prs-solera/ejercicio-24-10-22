package com.questionnaire.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;

@Repository
public interface MultipleChoiceChoiceRepository extends JpaRepository<MultipleChoiceChoice, Integer> {
    List<MultipleChoiceChoice> findByQuestion(MultipleChoiceQuestion q);
}
