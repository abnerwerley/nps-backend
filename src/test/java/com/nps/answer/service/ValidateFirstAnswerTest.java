package com.nps.answer.service;

import com.nps.answer.json.AnswerForm;
import com.nps.exception.RequestException;
import com.nps.question.entity.Question;
import com.nps.question.persistence.QuestionCustomRepository;
import com.nps.answer.utils.ValidateFirstAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateFirstAnswerTest {

    @InjectMocks
    private ValidateFirstAnswer validate;

    @Mock
    private QuestionCustomRepository questionCustomRepository;

    public static final Long QUESTION_ID = 1L;
    public static final String FIRST_ENQUIRY = "a FireDev para um amigo ou familiar?";
    public static final String RESPONSE = "A Firedev é boa demaizi";
    public static final int SCORE = 10;
    public static final String ERROR_MESSAGE = "It is mandatory for the first response to have a score, and a opinion.";

    @BeforeEach
    public void firstQuestion() {
        when(questionCustomRepository.findQuestions(null, FIRST_ENQUIRY)).thenReturn(getQuestionList());
    }

    @Test
    void testValidateFirstAnswerRequestException() {
        AnswerForm form = new AnswerForm(QUESTION_ID);
        Question firstQuestion = new Question(QUESTION_ID, FIRST_ENQUIRY);
        Exception exception = assertThrows(RequestException.class, () -> validate.validateFirstAnswer(
                form, firstQuestion));
        assertNotNull(exception);
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        verify(questionCustomRepository).findQuestions(null, FIRST_ENQUIRY);
    }

    @Test
    void testValidateNullScore() {
        AnswerForm form = new AnswerForm(RESPONSE, QUESTION_ID);
        Question firstQuestion = new Question(QUESTION_ID, FIRST_ENQUIRY);
        Exception exception = assertThrows(RequestException.class, () -> validate.validateFirstAnswer(
                form, firstQuestion));
        assertNotNull(exception);
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        verify(questionCustomRepository).findQuestions(null, FIRST_ENQUIRY);
    }

    @Test
    void testValidateBlankResponse() {
        AnswerForm form = new AnswerForm("", QUESTION_ID);
        Question firstQuestion = new Question(QUESTION_ID, FIRST_ENQUIRY);
        Exception exception = assertThrows(RequestException.class, () -> validate.validateFirstAnswer(
                form, firstQuestion));
        assertNotNull(exception);
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        verify(questionCustomRepository).findQuestions(null, FIRST_ENQUIRY);
    }

    @Test
    void testValidateNullResponse() {
        AnswerForm form = new AnswerForm(QUESTION_ID);
        Question firstQuestion = new Question(QUESTION_ID, FIRST_ENQUIRY);
        Exception exception = assertThrows(RequestException.class, () -> validate.validateFirstAnswer(
                form, firstQuestion));
        assertNotNull(exception);
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        verify(questionCustomRepository).findQuestions(null, FIRST_ENQUIRY);
    }

    @Test
    void testValidateFirstAnswerCompletelyResponded() {
        AnswerForm form = new AnswerForm(RESPONSE, SCORE, QUESTION_ID);
        validate.validateFirstAnswer(form, getFirstQuestion());
        verify(questionCustomRepository).findQuestions(null, FIRST_ENQUIRY);
    }

    @Test
    void testValidateSecondQuestionNullAttributes() {
        AnswerForm form = new AnswerForm(0, QUESTION_ID);
        validate.validateFirstAnswer(form, getAnyQuestion());
        verify(questionCustomRepository).findQuestions(null, FIRST_ENQUIRY);
    }

    private Question getFirstQuestion() {
        return Question.builder()
                .questionId(QUESTION_ID)
                .enquiry(FIRST_ENQUIRY)
                .build();
    }

    private Question getAnyQuestion() {
        return Question.builder()
                .questionId(2L)
                .enquiry("bla bla bla")
                .build();
    }

    private List<Question> getQuestionList() {
        return List.of(Question.builder()
                .questionId(QUESTION_ID)
                .enquiry(FIRST_ENQUIRY)
                .build());
    }
}
