package com.nps.answer.entity.mapper;

import com.nps.answer.entity.Answer;
import com.nps.answer.json.AnswerForm;

public class AnswerMapper {

    public static Answer fromFormToEntity(AnswerForm form) {
        return Answer.builder()
                .score(form.getScore())
                .response(form.getResponse())
                .questionId(form.getQuestionId())
                .build();
    }

    private AnswerMapper() {
    }
}
