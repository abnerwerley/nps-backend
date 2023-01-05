package com.nps.question.service;

import com.nps.exception.RequestException;
import com.nps.exception.ResourceNotFoundException;
import com.nps.question.entity.Question;
import com.nps.question.entity.mapper.QuestionMapper;
import com.nps.question.json.QuestionForm;
import com.nps.question.json.QuestionResponse;
import com.nps.question.json.QuestionUpdateForm;
import com.nps.question.persistence.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    private QuestionService service;

    @Mock
    private QuestionRepository repository;

    public static final Long ID = 1L;

    public static final String ENQUIRY = "Pretend this is a very good question, ok?";

    public static final String ENQUIRY_TO_BE_UPDATED = "Pretend this question is better than the previous one.";

    @Test
    void testRegisterQuestion() {
        doReturn(getQuestion()).when(repository).save(QuestionMapper.fromFormToEntity(getQuestionForm()));
        QuestionResponse registering = service.registerQuestion(getQuestionForm());
        assertNotNull(registering);
        verify(repository).save(QuestionMapper.fromFormToEntity(getQuestionForm()));
    }

    @Test
    void testRegisterQuestionException() {
        doThrow(RequestException.class).when(repository).save(QuestionMapper.fromFormToEntity(getQuestionForm()));
        Exception exception = assertThrows(RequestException.class, () -> service.registerQuestion(getQuestionForm()));
        assertEquals("Error when registering a question.", exception.getMessage());
    }

    @Test
    void testGetAllQuestions() {
        doReturn(getQuestionsList()).when(repository).findAll();
        Stream<QuestionResponse> questions = service.getAllQuestions();
        assertNotNull(questions);
        verify(repository).findAll();
    }

    @Test
    void testGetAllQuestionsException() {
        doThrow(RequestException.class).when(repository).findAll();
        Exception exception = assertThrows(RequestException.class, () -> service.getAllQuestions());
        assertEquals("Error when getting all questions.", exception.getMessage());
    }

//    @Test
//    void testUpdateQuestion() {
//        doReturn(getQuestionOptional()).when(repository).findById(ID);
//        doReturn(getQuestionUpdated()).when(repository).save(QuestionMapper.fromFormToEntity(getQuestionUpdateForm()));
//        QuestionResponse response = service.updateQuestion(getQuestionUpdateForm());
//        assertNotNull(response);
//        verify(repository).findById(ID);
//        verify(repository).save(QuestionMapper.fromFormToEntity(getQuestionUpdateForm()));
//    }

    @Test
    void testUpdateQuestionResourceNotFoundException() {
        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateQuestion(getQuestionUpdateForm()));
        assertEquals("Question does not exist.", exception.getMessage());
    }

    @Test
    void testUpdateQuestionException() {
        doReturn(getQuestionOptional()).when(repository).findById(ID);
        doThrow(RequestException.class).when(repository).save(QuestionMapper.fromFormToEntity(getQuestionUpdateFormWithMistake()));
        Exception exception = assertThrows(RequestException.class, () -> service.updateQuestion(getQuestionUpdateFormWithMistake()));
        assertEquals("Error when updating question.", exception.getMessage());
    }

    @Test
    void testDeleteQuestion() {
        doReturn(getQuestionOptional()).when(repository).findById(ID);
        String response = service.deleteQuestion(ID);
        assertEquals("Question deleted.", response);
    }

    @Test
    void testDeleteQuestionResourceNotFoundException() {
        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteQuestion(ID));
        assertEquals("Question does not exist.", exception.getMessage());
    }

    @Test
    void testDeleteQuestionException() {
        doReturn(getQuestionOptional()).when(repository).findById(ID);
        doThrow(RequestException.class).when(repository).deleteById(ID);
        Exception exception = assertThrows(RequestException.class, () -> service.deleteQuestion(ID));
        assertEquals("Error when deleting question by id.", exception.getMessage());
    }

    private Question getQuestion() {
        return Question.builder()
                .id(ID)
                .enquiry(ENQUIRY)
                .build();
    }

    private Optional<Question> getQuestionOptional() {
        return Optional.of(Question.builder()
                .id(ID)
                .enquiry(ENQUIRY)
                .build());
    }

    private QuestionForm getQuestionForm() {
        return QuestionForm.builder()
                .enquiry(ENQUIRY)
                .build();
    }

    private List<Question> getQuestionsList() {
        return List.of(Question.builder()
                .id(ID)
                .enquiry(ENQUIRY)
                .build());
    }

    private QuestionUpdateForm getQuestionUpdateForm() {
        return QuestionUpdateForm.builder()
                .id(ID)
                .enquiry(ENQUIRY_TO_BE_UPDATED)
                .build();
    }

    private QuestionUpdateForm getQuestionUpdateFormWithMistake() {
        return QuestionUpdateForm.builder()
                .id(ID)
                .enquiry(" ")
                .build();
    }

    private Question getQuestionUpdated() {
        return Question.builder()
                .id(ID)
                .enquiry(ENQUIRY_TO_BE_UPDATED)
                .build();
    }
}
