package com.questionnaire.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.MultipleChoiceAnswer;
import com.questionnaire.model.User;

@Repository
public interface MultipleChoiceAnswerRepository extends JpaRepository<MultipleChoiceAnswer, Integer> {
    List<MultipleChoiceAnswer> findByUser(User user);
}
