package com.nps.question.service;

import com.nps.exception.RequestException;
import com.nps.exception.ResourceNotFoundException;
import com.nps.question.entity.Question;
import com.nps.question.entity.mapper.QuestionMapper;
import com.nps.question.json.QuestionForm;
import com.nps.question.json.QuestionResponse;
import com.nps.question.json.QuestionUpdateForm;
import com.nps.question.persistence.QuestionCustomRepository;
import com.nps.question.persistence.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    private QuestionService service;

    @Mock
    private QuestionRepository repository;

    @Mock
    private QuestionCustomRepository customRepository;

    public static final Long ID = 1L;
    public static final String ENQUIRY = "Pretend this is a very good question, ok?";
    public static final String ENQUIRY_TO_BE_UPDATED = "Pretend this question is better than the previous one.";

    @Test
    void testRegisterQuestion() {
        QuestionForm form = new QuestionForm(ENQUIRY);
        QuestionResponse response = service.registerQuestion(form);
        assertNotNull(response);
        assertEquals(ENQUIRY, response.getEnquiry());
    }

    @Test
    void testRegisterQuestionException() {
        QuestionForm form = new QuestionForm(ENQUIRY);
        doThrow(RequestException.class).when(repository).save(QuestionMapper.fromFormToEntity(form));
        Exception exception = assertThrows(RequestException.class, () -> service.registerQuestion(form));
        assertEquals("Error when registering a question.", exception.getMessage());
    }

    @Test
    void testGetAllQuestions() {
        doReturn(getQuestionsList()).when(customRepository).findQuestions(ID, ENQUIRY);
        List<QuestionResponse> questions = service.getAllQuestions(ID, ENQUIRY);
        assertNotNull(questions);
        verify(customRepository).findQuestions(ID, ENQUIRY);
    }

    @Test
    void testGetAllQuestionsException() {
        doThrow(RequestException.class).when(customRepository).findQuestions(null, null);
        Exception exception = assertThrows(RequestException.class, () -> service.getAllQuestions(null, null));
        assertEquals("Error when getting all questions.", exception.getMessage());
    }

    @Test
    void testGetQuestionById() {
        doReturn(getQuestionOptional()).when(repository).findById(ID);
        Optional<QuestionResponse> response = service.getQuestionById(ID);
        assertNotNull(response);
        verify(repository).findById(ID);

        Exception ResourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> service.getQuestionById(99L));
        assertNotNull(ResourceNotFoundException);
        assertEquals("Question not found with id: 99", ResourceNotFoundException.getMessage());
    }

    @Test
    void testUpdateQuestion() {
        doReturn(getQuestionOptional()).when(repository).findById(getQuestionUpdateForm().getId());
        QuestionResponse response = service.updateQuestion(getQuestionUpdateForm());
        assertNotNull(response);
        assertEquals(ENQUIRY_TO_BE_UPDATED, response.getEnquiry());
        verify(repository).findById(getQuestionUpdateForm().getId());
    }

    @Test
    void testUpdateQuestionResourceNotFoundException() {
        QuestionUpdateForm form = new QuestionUpdateForm(ID, ENQUIRY_TO_BE_UPDATED);
        doThrow(ResourceNotFoundException.class).when(repository).findById(form.getId());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateQuestion(form));
        assertEquals("Question does not exist.", exception.getMessage());
    }

    @Test
    void testUpdateQuestionException() {
        QuestionUpdateForm form = new QuestionUpdateForm(ID, " ");
        doReturn(getQuestionOptional()).when(repository).findById(form.getId());
        doThrow(RequestException.class).when(repository).save(QuestionMapper.fromFormToEntity(form));
        Exception exception = assertThrows(RequestException.class, () -> service.updateQuestion(form));
        assertEquals("Error when updating question.", exception.getMessage());
        verify(repository).findById(form.getId());
    }

    @Test
    void testDeleteQuestion() {
        doReturn(getQuestionOptional()).when(repository).findById(ID);
        String response = service.deleteQuestion(ID);
        assertEquals("Question deleted.", response);
        verify(repository).findById(ID);
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
        verify(repository).findById(ID);
    }

    private Optional<Question> getQuestionOptional() {
        return Optional.of(Question.builder()
                .questionId(ID)
                .enquiry(ENQUIRY)
                .build());
    }

    private List<Question> getQuestionsList() {
        return List.of(Question.builder()
                .questionId(ID)
                .enquiry(ENQUIRY)
                .build());
    }

    private QuestionUpdateForm getQuestionUpdateForm() {
        return QuestionUpdateForm.builder()
                .id(ID)
                .enquiry(ENQUIRY_TO_BE_UPDATED)
                .build();
    }
}
