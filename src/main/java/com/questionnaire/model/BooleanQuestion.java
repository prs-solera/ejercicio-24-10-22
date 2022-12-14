package com.questionnaire.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bulquestion")
public class BooleanQuestion {
    @Id
    @GeneratedValue
    private Integer id = 0;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<BooleanAnswer> answers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean equals(Object o) {
        if (o instanceof BooleanQuestion) {
            return getId().equals(((BooleanQuestion) o).getId());
        } else {
            return false;
        }
    }
}
