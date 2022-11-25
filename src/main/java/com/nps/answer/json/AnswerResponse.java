package com.nps.answer.json;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerResponse {
    private Long id;
    private String response;
    private int points;
}
