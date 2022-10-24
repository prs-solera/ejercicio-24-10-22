package com.questionnaire.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "multiplechoicechoice")
public class MultipleChoiceChoice {
    @Id
    @GeneratedValue
    private Integer id = 0;

    private String title;
    
    @OneToMany(mappedBy = "choice", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<MultipleChoiceAnswer> answers;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "mquestion_id", nullable = false)
    private MultipleChoiceChoice question;

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
        if (o instanceof MultipleChoiceChoice) {
            return getId().equals(((MultipleChoiceChoice) o).getId());
        } else {
            return false;
        }
    }
}
