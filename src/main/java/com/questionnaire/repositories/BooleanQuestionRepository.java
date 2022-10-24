package com.questionnaire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.BooleanQuestion;

@Repository
public interface BooleanQuestionRepository extends JpaRepository<BooleanQuestion, Integer>  {
    
}
