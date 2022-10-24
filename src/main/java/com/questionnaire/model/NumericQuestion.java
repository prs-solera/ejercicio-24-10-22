package com.questionnaire.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "numericquestion")
public class NumericQuestion {
    @Id
    @GeneratedValue
    private Integer id = 0;

    private String title;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<NumericAnswer> answers;

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
        if (o instanceof NumericQuestion) {
            return getId().equals(((NumericQuestion) o).getId());
        } else {
            return false;
        }
    }
}
