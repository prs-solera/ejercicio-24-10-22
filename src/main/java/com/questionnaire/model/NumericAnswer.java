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
@Table(name = "numericanswer")
public class NumericAnswer {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer value;

    private LocalDate answerDatetime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "nquestion_id", nullable = false)
    private NumericQuestion question;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public LocalDate getAnswerDatetime() {
        return answerDatetime;
    }

    public void setAnswerDatetime(LocalDate answerDatetime) {
        this.answerDatetime = answerDatetime;
    }
}
