package com.nps.answer.utils;

import com.nps.answer.json.AnswerForm;
import com.nps.exception.RequestException;
import com.nps.question.entity.Question;
import com.nps.question.persistence.QuestionCustomRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
public class ValidateFirstAnswer {
    @Autowired
    private QuestionCustomRepository questionCustomRepository;

    public void validateFirstAnswer(AnswerForm form, Question question) {
        try {
            List<Question> firstQuestion = questionCustomRepository.findQuestions(null, "a FireDev para um amigo ou familiar?");
            boolean questionToBeRespondedIsFirstQuestion = (firstQuestion.get(0).getEnquiry().equals(question.getEnquiry()));
            if (questionToBeRespondedIsFirstQuestion
                    && (form.getScore() == null || form.getResponse() == null || form.getResponse().equals(""))) {
                throw new RequestException("It is mandatory for the first response to have a score, and a opinion.");
            }
        } catch (RequestException e) {
            throw new RequestException(e.getMessage());
        }
    }
}
