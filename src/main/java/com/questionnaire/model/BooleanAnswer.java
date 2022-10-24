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
@Table(name = "bulanswer")
public class BooleanAnswer {
    @Id
    @GeneratedValue
    private Integer id = 0;

    private Boolean answerval;

    private LocalDate answerDatetime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "bquestion_id", nullable = false)
    private BooleanQuestion question;

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

    public Boolean getAnswerval() {
        return answerval;
    }

    public void setAnswerval(Boolean answerval) {
        this.answerval = answerval;
    }

    public BooleanQuestion getQuestion() {
        return question;
    }

    public void setQuestion(BooleanQuestion question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(Object o) {
        if (o instanceof BooleanAnswer) {
            return getId().equals(((BooleanAnswer) o).getId());
        } else {
            return false;
        }
    }
}
