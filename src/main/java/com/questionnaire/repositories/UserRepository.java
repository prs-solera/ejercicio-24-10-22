package com.questionnaire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.questionnaire.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
