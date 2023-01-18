package com.nps.answer.json;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerResponse {
    private Long answerId;
    private String response;
    private int score;
    private Long questionId;
}
