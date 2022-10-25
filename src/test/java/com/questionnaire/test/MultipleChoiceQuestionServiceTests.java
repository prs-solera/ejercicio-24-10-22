package com.questionnaire.test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.services.MultipleChoiceQuestionService;

@SpringBootTest
public class MultipleChoiceQuestionServiceTests {
    
    @Autowired
    private MultipleChoiceQuestionService service;

    @BeforeEach
    public void before() {
        service.deleteAll();
    }

    @Test
    public void save_normal() throws IllegalModelException {
        MultipleChoiceQuestion q = new MultipleChoiceQuestion();

        q.setTitle("Title");

        q = service.save(q);

        assertEquals(q.getTitle(), "Title");
    }

    @Test
    public void save_abnormal_nullTitle() throws IllegalModelException {
        MultipleChoiceQuestion q = new MultipleChoiceQuestion();

        q.setTitle(null);

        assertThrows(IllegalModelException.class, () -> {service.save(q);});
    }

    @Test
    public void save_abnormal_emptyTitle() throws IllegalModelException {
        MultipleChoiceQuestion q = new MultipleChoiceQuestion();

        q.setTitle("");

        assertThrows(IllegalModelException.class, () -> {service.save(q);});
    }

}