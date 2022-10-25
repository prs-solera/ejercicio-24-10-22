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
import com.questionnaire.model.MultipleChoiceAnswer;
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.MultipleChoiceAnswerService;
import com.questionnaire.services.MultipleChoiceChoiceService;
import com.questionnaire.services.MultipleChoiceQuestionService;
import com.questionnaire.services.UserService;

@SpringBootTest
public class MultipleChoiceAnswerServiceTests {
    
    @Autowired
    private MultipleChoiceAnswerService service;

    @Autowired
    private MultipleChoiceQuestionService questionService;

    @Autowired
    private MultipleChoiceChoiceService choiceChoiceService;

    @Autowired
    private UserService userService;

    private User mockUser;

    private MultipleChoiceQuestion mockQuestion;

    private MultipleChoiceChoice mockChoice;

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

        mockChoice = new MultipleChoiceChoice();
        mockChoice.setTitle("Choice");
        mockChoice.setQuestion(mockQuestion);
        mockChoice = choiceChoiceService.save(mockChoice);

        mockQuestion = questionService.findById(mockQuestion.getId());
    }

    @Test
    public void save_normal() throws IllegalModelException {
        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(mockUser);
        a.setChoice(mockChoice);

        a = service.save(a);

        assertEquals(a.getUser(), mockUser);
        assertEquals(a.getQuestion(), mockQuestion);
        assertEquals(a.getChoice(), mockChoice);
    }

    @Test
    public void save_abnormal_nullChoice() throws IllegalModelException {
        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(mockUser);
        a.setChoice(null);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }

    @Test
    public void save_abnormal_nullUser() throws IllegalModelException {
        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(null);
        a.setChoice(mockChoice);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }

    @Test
    public void save_abnormal_nullQuestion() throws IllegalModelException {
        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setQuestion(null);
        a.setUser(mockUser);
        a.setChoice(mockChoice);

        assertThrows(IllegalModelException.class, () -> {service.save(a);});
    }

    @Test
    public void save_abnormal_alreadySaved() throws IllegalModelException {
        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(mockUser);
        a.setChoice(mockChoice);

        MultipleChoiceAnswer b = service.save(a);

        assertThrows(IllegalModelException.class, () -> {service.save(b);});
    }
}
