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
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.MultipleChoiceChoiceService;
import com.questionnaire.services.MultipleChoiceQuestionService;
import com.questionnaire.services.UserService;

@SpringBootTest
public class MultipleChoiceChoiceServiceTests {
    
    @Autowired
    private MultipleChoiceChoiceService service;

    @Autowired
    private MultipleChoiceQuestionService questionService;

    @Autowired
    private UserService userService;

    private User mockUser;

    private MultipleChoiceQuestion mockQuestion;

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

        mockQuestion = new MultipleChoiceQuestion();
        mockQuestion.setTitle("Test question");
        mockQuestion = questionService.save(mockQuestion);
    }

    @Test
    public void save_normal() throws IllegalModelException {
        MultipleChoiceChoice a = new MultipleChoiceChoice();
        a.setTitle("title");
        a.setQuestion(mockQuestion);

        a = service.save(a);

        assertEquals(a.getTitle(), "title");
    }

    @Test
    public void save_abnormal_nullTitle() throws IllegalModelException {
        MultipleChoiceChoice a = new MultipleChoiceChoice();
        a.setTitle(null);
        a.setQuestion(mockQuestion);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }


    @Test
    public void save_abnormal_nullQuestion() throws IllegalModelException {
        MultipleChoiceChoice a = new MultipleChoiceChoice();
        a.setTitle("title");
        a.setQuestion(null);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }
}
