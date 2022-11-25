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
                .id(answer.getId())
                .response(answer.getResponse())
                .points(answer.getPoints())
                .build();
    }

    private AnswerResponseMapper(){}
}
