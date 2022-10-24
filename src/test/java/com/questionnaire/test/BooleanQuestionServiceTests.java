package com.questionnaire.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.BooleanQuestion;
import com.questionnaire.services.BooleanQuestionService;

@SpringBootTest
public class BooleanQuestionServiceTests {
    
    @Autowired
    private BooleanQuestionService service;

    @BeforeEach
    public void before() {
        service.deleteAll();
    }

    public void save_normal() throws IllegalModelException {
        BooleanQuestion q = new BooleanQuestion();

        q.setTitle("Title");

        q = service.save(q);

        assertEquals(q.getTitle(), "Title");
    }

    public void save_abnormal_nullTitle() throws IllegalModelException {
        BooleanQuestion q = new BooleanQuestion();

        q.setTitle(null);

        assertThrows(IllegalModelException.class, () -> {service.save(q);});
    }

    public void save_abnormal_emptyTitle() throws IllegalModelException {
        BooleanQuestion q = new BooleanQuestion();

        q.setTitle("");

        assertThrows(IllegalModelException.class, () -> {service.save(q);});
    }

}
