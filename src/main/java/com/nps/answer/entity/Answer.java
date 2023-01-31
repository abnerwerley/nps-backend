package com.nps.answer.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "ANSWER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ANSWER")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    private String response;

    private Integer score;

    private Long questionId;
}
