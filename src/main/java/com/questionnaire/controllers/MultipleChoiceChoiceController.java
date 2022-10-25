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
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.MultipleChoiceChoiceService;
import com.questionnaire.services.MultipleChoiceQuestionService;
import com.questionnaire.services.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/choicec")
public class MultipleChoiceChoiceController {
    
    @Autowired
    private MultipleChoiceChoiceService service;

    @Autowired
    private MultipleChoiceQuestionService multipleChoiceQuestionService;

    @GetMapping
    public ResponseEntity<List<MultipleChoiceChoice>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/byq/{questionid}")
    public ResponseEntity<List<MultipleChoiceChoice>> getByQuestion(@PathVariable(value = "questionid") Integer questionid) {
        MultipleChoiceQuestion q = multipleChoiceQuestionService.findById(questionid);
        if (q == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(service.findByQuestion(q));
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Map<String,String> params) {
        String title = params.getOrDefault("title", "");
        if (title == null || title.equals("")) {
            return ResponseEntity.badRequest().build();
        }
        
        MultipleChoiceQuestion b = multipleChoiceQuestionService.findById(Integer.parseInt(params.getOrDefault("question", "-1")));

        MultipleChoiceChoice a = new MultipleChoiceChoice();
        a.setQuestion(b);
        a.setTitle(title);

        try {
            a = service.save(a);
        } catch (IllegalModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(a);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {
        MultipleChoiceChoice a = service.findById(id);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(a);
        return ResponseEntity.ok().build();
    }
}