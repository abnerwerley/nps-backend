package com.nps.answer.entity.mapper;

import com.nps.answer.entity.Answer;
import com.nps.answer.json.AnswerForm;

public class AnswerMapper {

    public static Answer fromFormToEntity(AnswerForm form) {
        return Answer.builder()
                .points(form.getPoints())
                .response(form.getResponse())
                .build();
    }

    private AnswerMapper(){}
}
