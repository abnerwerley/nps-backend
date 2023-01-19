package com.nps.question.json.mapper;

import com.nps.question.entity.Question;
import com.nps.question.json.QuestionResponse;

public class QuestionResponseMapper {
    public static QuestionResponse fromEntityToResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .enquiry(question.getEnquiry())
                .build();
    }

    private QuestionResponseMapper() {
    }
}
