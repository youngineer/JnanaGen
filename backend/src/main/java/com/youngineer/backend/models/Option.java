package com.youngineer.backend.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "options")
public class Option {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "option_text")
    private String optionText;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}

