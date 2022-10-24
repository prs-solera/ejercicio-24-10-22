package com.questionnaire.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.questionnaire.model.NumericAnswer;
import com.questionnaire.model.User;

@Repository
public interface NumericAnswerRepository extends JpaRepository<NumericAnswer, Integer> {
    List<NumericAnswer> findByUser(User user);
}
