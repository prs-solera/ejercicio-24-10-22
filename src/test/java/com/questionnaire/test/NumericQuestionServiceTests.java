package com.questionnaire.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.services.NumericQuestionService;

@SpringBootTest
public class NumericQuestionServiceTests {
    
    @Autowired
    private NumericQuestionService service;

    @BeforeEach
    public void before() {
        service.deleteAll();
    }

    @Test
    public void save_normal() throws IllegalModelException {
        NumericQuestion q = new NumericQuestion();

        q.setTitle("Title");

        q = service.save(q);

        assertEquals(q.getTitle(), "Title");
    }

    @Test
    public void save_abnormal_nullTitle() throws IllegalModelException {
        NumericQuestion q = new NumericQuestion();

        q.setTitle(null);

        assertThrows(IllegalModelException.class, () -> {service.save(q);});
    }

    @Test
    public void save_abnormal_emptyTitle() throws IllegalModelException {
        NumericQuestion q = new NumericQuestion();

        q.setTitle("");

        assertThrows(IllegalModelException.class, () -> {service.save(q);});
    }

}
