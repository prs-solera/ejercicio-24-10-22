package com.questionnaire.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.NumericAnswer;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.NumericAnswerService;
import com.questionnaire.services.NumericQuestionService;
import com.questionnaire.services.UserService;

@SpringBootTest
public class NumericAnswerServiceTests {
    
    @Autowired
    private NumericAnswerService service;

    @Autowired
    private NumericQuestionService questionService;

    @Autowired
    private UserService userService;

    private User mockUser;

    private NumericQuestion mockQuestion;

    @BeforeEach
    public void before() throws IllegalModelException {
        service.deleteAll();
        questionService.deleteAll();
        userService.deleteAll();

        List<String> roles = new ArrayList<String>();
        roles.add("CUSTOMER");

        mockUser = new User();
        mockUser.setUsername("pepe");
        mockUser.setPassword("pass");
        mockUser.setRoles(roles);
        mockUser = userService.save(mockUser);

        mockQuestion = new NumericQuestion();
        mockQuestion.setTitle("Test question");
        mockQuestion = questionService.save(mockQuestion);
    }

    @Test
    public void save_normal() throws IllegalModelException {
        NumericAnswer a = new NumericAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(mockUser);
        a.setAnswerval(2);

        a = service.save(a);

        assertEquals(a.getUser(), mockUser);
        assertEquals(a.getQuestion(), mockQuestion);
    }

    @Test
    public void save_abnormal_nullValue() throws IllegalModelException {
        NumericAnswer a = new NumericAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(mockUser);
        a.setAnswerval(null);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }

    @Test
    public void save_abnormal_nullUser() throws IllegalModelException {
        NumericAnswer a = new NumericAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(null);
        a.setAnswerval(2);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }

    @Test
    public void save_abnormal_nullQuestion() throws IllegalModelException {
        NumericAnswer a = new NumericAnswer();
        a.setQuestion(null);
        a.setUser(mockUser);
        a.setAnswerval(2);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }
}
