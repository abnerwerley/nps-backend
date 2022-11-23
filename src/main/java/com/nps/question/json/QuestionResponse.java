package com.nps.question.json;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionResponse {
    private Long id;
    private String enquiry;
}
