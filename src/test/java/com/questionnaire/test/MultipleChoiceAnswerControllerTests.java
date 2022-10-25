package com.questionnaire.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.MultipleChoiceAnswer;
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.MultipleChoiceAnswerService;
import com.questionnaire.services.MultipleChoiceChoiceService;
import com.questionnaire.services.MultipleChoiceQuestionService;
import com.questionnaire.services.NumericQuestionService;
import com.questionnaire.services.UserService;

import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.RestAssured.*;
import io.restassured.filter.log.LogDetail;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.matcher.RestAssuredMatchers.*;

import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultipleChoiceAnswerControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private MultipleChoiceAnswerService service;

    @Autowired
    private MultipleChoiceQuestionService multipleChoiceQuestionService;

    @Autowired
    private MultipleChoiceChoiceService choiceChoiceService;

    @Autowired
    private UserService userService;

    private User mockCustomer;

    private User mockAdmin;

    private MultipleChoiceQuestion mockQuestion;

    private MultipleChoiceChoice mockChoice;

    @BeforeEach
    void before() throws IllegalModelException {
        service.deleteAll();
        userService.deleteAll();
        multipleChoiceQuestionService.deleteAll();

        List<String> customer = new ArrayList<String>();
        customer.add("CUSTOMER");

        List<String> admin = new ArrayList<String>();
        admin.add("ADMIN");

        mockCustomer = new User();
        mockCustomer.setRoles(customer);
        mockCustomer.setUsername("Pepe");
        mockCustomer.setPassword("pass");
        mockCustomer = userService.save(mockCustomer);

        mockAdmin = new User();
        mockAdmin.setRoles(admin);
        mockAdmin.setUsername("Pepe2");
        mockAdmin.setPassword("pass");
        mockAdmin = userService.save(mockAdmin);

        mockQuestion = new MultipleChoiceQuestion();
        mockQuestion.setTitle("title");
        mockQuestion = multipleChoiceQuestionService.save(mockQuestion);

        mockChoice = new MultipleChoiceChoice();
        mockChoice.setTitle("Choice");
        mockChoice.setQuestion(mockQuestion);
        mockChoice = choiceChoiceService.save(mockChoice);

        mockQuestion = multipleChoiceQuestionService.findById(mockQuestion.getId());
    }

    @Test
    void no_permission() {
        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post("/api/choicea/create")
        .then()
        .assertThat()
        .statusCode(401)
        ;
    }

    @Test
    void create_normal() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("choice", mockChoice.getId().toString());
        body.put("question", mockQuestion.getId().toString());

        Integer id = (Integer) RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/choicea/create")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(Map.class).get("id")
        ;

        MultipleChoiceAnswer a = service.findById(id);

        assertEquals(mockChoice, a.getChoice());
        assertEquals(mockQuestion, a.getQuestion());
    }

    @Test
    void create_abnormal_nullQuestion() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("choice", mockChoice.getId().toString());
        body.put("question", "-38");

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/choicea/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void create_abnormal_nullChoice() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("choice", "-3845");
        body.put("question", mockQuestion.getId().toString());

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/choicea/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void delete_normal() throws IllegalModelException {
        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(mockCustomer);
        a.setChoice(mockChoice);
        a = service.save(a);

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .delete("/api/choicea/delete/" + a.getId().toString())
        .then()
        .assertThat()
        .statusCode(200)
        ;

        assertNull(service.findById(a.getId()));
    }

    @Test
    void get_normal() throws IllegalModelException {
        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setQuestion(mockQuestion);
        a.setUser(mockCustomer);
        a.setChoice(mockChoice);
        a = service.save(a);

        MultipleChoiceAnswer a2 = new MultipleChoiceAnswer();
        a2.setQuestion(mockQuestion);
        a2.setUser(mockCustomer);
        a2.setChoice(mockChoice);
        a2 = service.save(a2);

        List<Map> l = RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .get("/api/choicea")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(List.class)
        ;

        assertEquals(a.getId(), l.get(0).get("id"));
        assertEquals(a2.getId(), l.get(1).get("id"));
        assertEquals(2, l.size());
    }
}