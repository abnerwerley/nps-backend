package com.nps.answer.json;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerForm {

    private String response;

    @NotNull
    private Integer score;

    private Long questionId;

    public AnswerForm(Long questionId) {
        this.questionId = questionId;
    }

    public AnswerForm(String response, Long questionId) {
        this.response = response;
        this.questionId = questionId;
    }

    public AnswerForm(int score, Long questionId) {
        this.questionId = questionId;
        this.score = score;
    }
}
