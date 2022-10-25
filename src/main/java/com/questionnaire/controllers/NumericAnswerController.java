package com.questionnaire.controllers;

import java.util.List;
import java.util.Map;

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
import com.questionnaire.model.NumericAnswer;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.NumericAnswerService;
import com.questionnaire.services.NumericQuestionService;
import com.questionnaire.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/numerica")
public class NumericAnswerController {
    
    @Autowired
    private NumericAnswerService service;

    @Autowired
    private UserService userService;

    @Autowired
    private NumericQuestionService booleanQuestionService;

    @GetMapping
    public ResponseEntity<List<NumericAnswer>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<NumericAnswer>> getByUsername(@PathVariable(value = "username") String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findByUser(user));
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Map<String,String> params) {
        String val = params.getOrDefault("answerval", "");
        if (val == null || val.equals("")) {
            return ResponseEntity.badRequest().build();
        }
        Integer answerval;
        try {
            answerval = Integer.parseInt(val);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        
        NumericQuestion b = booleanQuestionService.findById(Integer.parseInt(params.getOrDefault("question", "-1")));
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());


        NumericAnswer a = new NumericAnswer();
        a.setUser(user);
        a.setAnswerval(answerval);
        a.setQuestion(b);

        try {
            a = service.save(a);
        } catch (IllegalModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(a);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {
        NumericAnswer a = service.findById(id);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(a);
        return ResponseEntity.ok().build();
    }
}
