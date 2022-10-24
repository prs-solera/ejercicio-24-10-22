package com.questionnaire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.MultipleChoiceChoice;

@Repository
public interface MultipleChoiceChoiceRepository extends JpaRepository<MultipleChoiceChoice, Integer> {
    
}
