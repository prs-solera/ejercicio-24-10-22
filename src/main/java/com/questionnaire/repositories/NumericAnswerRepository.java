package com.questionnaire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.NumericAnswer;

@Repository
public interface NumericAnswerRepository extends JpaRepository<NumericAnswer, Integer> {
    
}
