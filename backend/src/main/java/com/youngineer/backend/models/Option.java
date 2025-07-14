package com.youngineer.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.youngineer.backend.dto.responses.OptionDto;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "option")
public class Option {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "option_text")
    private String optionText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Question question;

    public Long getId() {
        return id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}

