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
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.MultipleChoiceQuestionService;
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
public class MultipleChoiceQuestionControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private MultipleChoiceQuestionService service;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private User mockCustomer;
    
    private User mockAdmin;

    @BeforeEach
    void before() throws IllegalModelException {
        service.deleteAll();
        userService.deleteAll();

        List<String> customer = new ArrayList<String>();
        customer.add("CUSTOMER");

        mockCustomer = new User();
        mockCustomer.setRoles(customer);
        mockCustomer.setUsername("Pepe");
        mockCustomer.setPassword("pass");
        mockCustomer = userService.save(mockCustomer);
    }

    public void auth(User user) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void get_normal() throws IllegalModelException {
        MultipleChoiceQuestion q1 = new MultipleChoiceQuestion();
        q1.setTitle("Test question 1");
        q1 = service.save(q1);

        MultipleChoiceQuestion q2 = new MultipleChoiceQuestion();
        q2.setTitle("Test question 2");
        q2 = service.save(q2);

        List<Map<String,String>> l = RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .get("/api/choiceq/")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(List.class)
        ;

        assertEquals(l.size(), 2);
        assertEquals("Test question 1", l.get(0).get("title"));
        assertEquals("Test question 2", l.get(1).get("title"));
    }

    @Test
    void get_abnormal_nonAuth() throws IllegalModelException {
        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .get("/api/choiceq/")
        .then()
        .assertThat()
        .statusCode(401)
        ;
    }

    @Test
    void create_normal() throws IllegalModelException {
        Map<String, String> body = new HashMap<String,String>();
        body.put("title", "test");
        
        Map<String,Object> m = RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .body(body)
        .when()
        .post("/api/choiceq/create")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(Map.class)
        ;

        assertEquals("test", m.get("title"));

        Integer id = (Integer) m.get("id");

        MultipleChoiceQuestion q = service.findById(id);

        assertEquals(q.getId(), id);
        assertEquals(q.getTitle(), "test");
    }

    @Test
    void edit_normal() throws IllegalModelException {
        MultipleChoiceQuestion q1 = new MultipleChoiceQuestion();
        q1.setTitle("Test");
        q1 = service.save(q1);

        Map<String, String> body = new HashMap<String,String>();
        body.put("title", "test2");
        body.put("id", q1.getId().toString());
        
        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .body(body)
        .when()
        .put("/api/choiceq/edit")
        .then()
        .assertThat()
        .statusCode(200)
        ;

        MultipleChoiceQuestion q2 = service.findById(q1.getId());

        assertEquals("test2", q2.getTitle());
    }

    @Test
    void delete_normal() throws IllegalModelException {
        MultipleChoiceQuestion q1 = new MultipleChoiceQuestion();
        q1.setTitle("Test");
        q1 = service.save(q1);
        
        RestAssured.given()
        .port(port)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .auth()
        .basic(mockCustomer.getUsername(), mockCustomer.getPassword())
        .when()
        .delete("/api/choiceq/delete/" + q1.getId())
        .then()
        .assertThat()
        .statusCode(200)
        ;

        MultipleChoiceQuestion q2 = service.findById(q1.getId());

        assertNull(q2);
    }
}
