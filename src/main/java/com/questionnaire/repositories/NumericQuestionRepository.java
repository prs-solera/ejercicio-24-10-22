package com.questionnaire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.NumericQuestion;

@Repository
public interface NumericQuestionRepository extends JpaRepository<NumericQuestion, Integer> {
    
}
