package com.questionnaire.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.BooleanQuestion;
import com.questionnaire.services.BooleanQuestionService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/boolq")
public class BooleanQuestionController {
    
    @Autowired
    private BooleanQuestionService service;

    @GetMapping
    public ResponseEntity<List<BooleanQuestion>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Map<String,String> params) {
        String title = params.getOrDefault("title", "");

        BooleanQuestion q = new BooleanQuestion();
        q.setTitle(title);

        try {
            q = service.save(q);
        } catch (IllegalModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(q);
    }

    @PutMapping("/edit")
    public ResponseEntity edit(@RequestBody Map<String,String> params) {
        String title = params.getOrDefault("title", "");
        Integer id = Integer.parseInt(params.getOrDefault("id", "0"));
        BooleanQuestion q = service.findById(id);
        
        if (q == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            q.setTitle(title);
            q = service.save(q);
        } catch (IllegalModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(q);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {
        BooleanQuestion q = service.findById(id);
        if (q == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(q);
        return ResponseEntity.ok().build();
    }
}
