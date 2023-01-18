package com.nps.answer.json.mapper;

import com.nps.answer.entity.Answer;
import com.nps.answer.json.AnswerResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponseMapper {

    public static AnswerResponse fromEntityToResponse(Answer answer) {
        return AnswerResponse.builder()
                .answerId(answer.getAnswerId())
                .response(answer.getResponse())
                .score(answer.getScore())
                .questionId(answer.getQuestionId())
                .build();
    }

    private AnswerResponseMapper() {
    }
}
