package com.nps.question.entity.mapper;

import com.nps.question.entity.Question;
import com.nps.question.json.QuestionForm;
import com.nps.question.json.QuestionUpdateForm;

public class QuestionMapper {

    public static Question fromFormToEntity(QuestionForm form) {
        return Question.builder()
                .enquiry(form.getEnquiry())
                .build();
    }

    public static Question fromFormToEntity(QuestionUpdateForm form) {
        return Question.builder()
                .enquiry(form.getEnquiry())
                .build();
    }

    private QuestionMapper(){}
}
