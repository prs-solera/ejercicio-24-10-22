package com.questionnaire.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.BooleanAnswer;
import com.questionnaire.model.User;

@Repository
public interface BooleanAnswerRepository extends JpaRepository<BooleanAnswer, Integer> {
    List<BooleanAnswer> findByUser(User user);
}
