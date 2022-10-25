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
import com.questionnaire.model.BooleanAnswer;
import com.questionnaire.model.BooleanQuestion;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.BooleanAnswerService;
import com.questionnaire.services.BooleanQuestionService;
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
public class BooleanAnswerControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private BooleanAnswerService service;

    @Autowired
    private BooleanQuestionService booleanQuestionService;

    @Autowired
    private UserService userService;

    private User mockCustomer;

    private User mockAdmin;

    private BooleanQuestion mockBooleanQuestion;

    @BeforeEach
    void before() throws IllegalModelException {
        service.deleteAll();
        userService.deleteAll();
        booleanQuestionService.deleteAll();

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

        mockBooleanQuestion = new BooleanQuestion();
        mockBooleanQuestion.setTitle("title");
        mockBooleanQuestion = booleanQuestionService.save(mockBooleanQuestion);
    }

    @Test
    void no_permission() {
        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post("/api/boola/create")
        .then()
        .assertThat()
        .statusCode(401)
        ;
    }

    @Test
    void create_normal() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("answerval", "true");
        body.put("question", mockBooleanQuestion.getId().toString());

        Integer id = (Integer) RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/boola/create")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(Map.class).get("id")
        ;

        BooleanAnswer a = service.findById(id);

        assertEquals(true, a.getAnswerval());
        assertEquals(mockBooleanQuestion, a.getQuestion());
    }

    @Test
    void create_abnormal_nullQuestion() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("answerval", "true");
        body.put("question", "-37");

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/boola/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void create_abnormal_nullValue() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("answerval", null);
        body.put("question", mockBooleanQuestion.getId().toString());

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/boola/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void create_abnormal_emptyValue() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("answerval", "");
        body.put("question", mockBooleanQuestion.getId().toString());

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/boola/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void delete_normal() throws IllegalModelException {
        BooleanAnswer a = new BooleanAnswer();
        a.setQuestion(mockBooleanQuestion);
        a.setUser(mockCustomer);
        a.setAnswerval(true);
        a = service.save(a);

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .delete("/api/boola/delete/" + a.getId().toString())
        .then()
        .assertThat()
        .statusCode(200)
        ;

        assertNull(service.findById(a.getId()));
    }

    @Test
    void get_normal() throws IllegalModelException {
        BooleanAnswer a = new BooleanAnswer();
        a.setQuestion(mockBooleanQuestion);
        a.setUser(mockCustomer);
        a.setAnswerval(true);
        a = service.save(a);

        BooleanAnswer a2 = new BooleanAnswer();
        a2.setQuestion(mockBooleanQuestion);
        a2.setUser(mockCustomer);
        a2.setAnswerval(false);
        a2 = service.save(a2);

        List<Map> l = RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .get("/api/boola")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(List.class)
        ;

        assertEquals(a.getId(), l.get(0).get("id"));
        assertEquals(a2.getId(), l.get(1).get("id"));
        assertEquals(a.getAnswerval(), l.get(0).get("answerval"));
        assertEquals(a2.getAnswerval(), l.get(1).get("answerval"));
        assertEquals(2, l.size());
    }

    @Test
    void getByUsername_normal() throws IllegalModelException {
        BooleanAnswer a = new BooleanAnswer();
        a.setQuestion(mockBooleanQuestion);
        a.setUser(mockCustomer);
        a.setAnswerval(true);
        a = service.save(a);

        BooleanAnswer a2 = new BooleanAnswer();
        a2.setQuestion(mockBooleanQuestion);
        a2.setUser(mockCustomer);
        a2.setAnswerval(false);
        a2 = service.save(a2);

        BooleanAnswer a3 = new BooleanAnswer();
        a3.setQuestion(mockBooleanQuestion);
        a3.setUser(mockAdmin);
        a3.setAnswerval(false);
        a3 = service.save(a3);

        List<Map> l = RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .get("/api/boola/" + mockCustomer.getUsername())
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(List.class)
        ;

        assertEquals(a.getId(), l.get(0).get("id"));
        assertEquals(a2.getId(), l.get(1).get("id"));
        assertEquals(a.getAnswerval(), l.get(0).get("answerval"));
        assertEquals(a2.getAnswerval(), l.get(1).get("answerval"));
        assertEquals(2, l.size());
    }
}