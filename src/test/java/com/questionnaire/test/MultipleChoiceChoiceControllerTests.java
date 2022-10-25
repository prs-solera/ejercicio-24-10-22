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
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.model.User;
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

public class MultipleChoiceChoiceControllerTests {
    
    @LocalServerPort
    private int port;

    @Autowired
    private MultipleChoiceChoiceService service;

    @Autowired
    private MultipleChoiceQuestionService multipleChoiceQuestionService;

    @Autowired
    private UserService userService;

    private User mockCustomer;

    private User mockAdmin;

    private MultipleChoiceQuestion mockMultipleChoiceQuestion;

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

        mockMultipleChoiceQuestion = new MultipleChoiceQuestion();
        mockMultipleChoiceQuestion.setTitle("title");
        mockMultipleChoiceQuestion = multipleChoiceQuestionService.save(mockMultipleChoiceQuestion);
    }

    @Test
    void no_permission() {
        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post("/api/choicec/create")
        .then()
        .assertThat()
        .statusCode(401)
        ;
    }

    @Test
    void create_normal() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("title", "test");
        body.put("question", mockMultipleChoiceQuestion.getId().toString());

        Integer id = (Integer) RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/choicec/create")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(Map.class).get("id")
        ;

        MultipleChoiceChoice a = service.findById(id);

        assertEquals("test", a.getTitle());
        assertEquals(mockMultipleChoiceQuestion, a.getQuestion());
    }

    @Test
    void create_abnormal_nullQuestion() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("title", "test");
        body.put("question", "-37");

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/choicec/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void create_abnormal_nullTitle() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("title", null);
        body.put("question", mockMultipleChoiceQuestion.getId().toString());

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/choicec/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void create_abnormal_emptyTitle() {
        Map<String,String> body = new HashMap<String,String>();
        body.put("title", "");;
        body.put("question", mockMultipleChoiceQuestion.getId().toString());

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(body)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .post("/api/choicec/create")
        .then()
        .assertThat()
        .statusCode(400)
        ;
    }

    @Test
    void delete_normal() throws IllegalModelException {
        MultipleChoiceChoice a = new MultipleChoiceChoice();
        a.setQuestion(mockMultipleChoiceQuestion);
        a.setTitle("title");
        a = service.save(a);

        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .delete("/api/choicec/delete/" + a.getId().toString())
        .then()
        .assertThat()
        .statusCode(200)
        ;

        assertNull(service.findById(a.getId()));
    }

    @Test
    void get_normal() throws IllegalModelException {
        MultipleChoiceChoice a = new MultipleChoiceChoice();
        a.setQuestion(mockMultipleChoiceQuestion);
        a.setTitle("title");
        a = service.save(a);

        MultipleChoiceChoice a2 = new MultipleChoiceChoice();
        a2.setQuestion(mockMultipleChoiceQuestion);
        a2.setTitle("title2");
        a2 = service.save(a2);

        List<Map> l = RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .get("/api/choicec")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(List.class)
        ;

        assertEquals(a.getId(), l.get(0).get("id"));
        assertEquals(a2.getId(), l.get(1).get("id"));
        assertEquals(a.getTitle(), l.get(0).get("title"));
        assertEquals(a2.getTitle(), l.get(1).get("title"));
        assertEquals(2, l.size());
    }

    @Test
    void getByQuestion_normal() throws IllegalModelException {
        MultipleChoiceChoice a = new MultipleChoiceChoice();
        a.setQuestion(mockMultipleChoiceQuestion);
        a.setTitle("title");
        a = service.save(a);

        MultipleChoiceChoice a2 = new MultipleChoiceChoice();
        a2.setQuestion(mockMultipleChoiceQuestion);
        a2.setTitle("title2");
        a2 = service.save(a2);

        MultipleChoiceQuestion m = new MultipleChoiceQuestion();
        m.setTitle("test");
        m = multipleChoiceQuestionService.save(m);

        MultipleChoiceChoice a3 = new MultipleChoiceChoice();
        a3.setQuestion(m);
        a3.setTitle("title3");
        a3 = service.save(a3);

        List<Map> l = RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .get("/api/choicec/byq/" + mockMultipleChoiceQuestion.getId().toString())
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(List.class)
        ;

        assertEquals(a.getId(), l.get(0).get("id"));
        assertEquals(a2.getId(), l.get(1).get("id"));
        assertEquals(a.getTitle(), l.get(0).get("title"));
        assertEquals(a2.getTitle(), l.get(1).get("title"));
        assertEquals(2, l.size());
    }
}