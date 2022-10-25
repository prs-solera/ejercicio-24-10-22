package com.questionnaire.controllers;

import java.util.List;
import java.util.Map;

import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.MultipleChoiceAnswer;
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.MultipleChoiceAnswerService;
import com.questionnaire.services.MultipleChoiceChoiceService;
import com.questionnaire.services.MultipleChoiceQuestionService;
import com.questionnaire.services.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/choicea")
public class MultipleChoiceAnswerController {
    
    @Autowired
    private MultipleChoiceAnswerService service;

    @Autowired
    private UserService userService;

    @Autowired
    private MultipleChoiceQuestionService multipleChoiceQuestionService;

    @Autowired
    private MultipleChoiceChoiceService multipleChoiceChoiceService;

    @GetMapping
    public ResponseEntity<List<MultipleChoiceAnswer>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<MultipleChoiceAnswer>> getByUsername(@PathVariable(value = "username") String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findByUser(user));
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Map<String,String> params) {
        MultipleChoiceQuestion q;
        MultipleChoiceChoice c;
        try {
            q = multipleChoiceQuestionService.findById(Integer.parseInt(params.getOrDefault("question", "-1")));
            c = multipleChoiceChoiceService.findById(Integer.parseInt(params.getOrDefault("choice", "-1")));    
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        

        MultipleChoiceAnswer a = new MultipleChoiceAnswer();
        a.setUser(user);
        a.setChoice(c);
        a.setQuestion(q);

        try {
            a = service.save(a);
        } catch (IllegalModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(a);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {
        MultipleChoiceAnswer a = service.findById(id);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(a);
        return ResponseEntity.ok().build();
    }
}