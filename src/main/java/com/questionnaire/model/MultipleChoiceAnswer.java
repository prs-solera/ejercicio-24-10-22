package com.questionnaire.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "multiplechoiceanswer")
public class MultipleChoiceAnswer {
    @Id
    @GeneratedValue
    private Integer id = 0;

    private LocalDate answerDatetime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "mquestion_id", nullable = false)
    private MultipleChoiceQuestion question;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "choice_id", nullable = false)
    private MultipleChoiceChoice choice;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getAnswerDatetime() {
        return answerDatetime;
    }

    public void setAnswerDatetime(LocalDate answerDatetime) {
        this.answerDatetime = answerDatetime;
    }

    
}
