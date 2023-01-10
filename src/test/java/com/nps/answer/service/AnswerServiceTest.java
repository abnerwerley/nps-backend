package com.nps.answer.service;

import com.nps.answer.entity.Answer;
import com.nps.answer.entity.mapper.AnswerMapper;
import com.nps.answer.json.AnswerForm;
import com.nps.answer.json.AnswerResponse;
import com.nps.answer.persistence.AnswerRepository;
import com.nps.exception.RequestException;
import com.nps.exception.ResourceNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
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
class AnswerServiceTest {

    @InjectMocks
    private AnswerService service;

    @Mock
    private AnswerRepository repository;

    public static final Long ID = 1L;
    public static final String RESPONSE = "Pretty good response for a pretty good question.";
    public static final int POINTS = 1;

    @Test
    void testRegisterAnswer() {
        doReturn(getAnswer()).when(repository).save(AnswerMapper.fromFormToEntity(getAnswerForm()));
        AnswerResponse response = service.registerAnswer(getAnswerForm());
        Assertions.assertNotNull(response);
        verify(repository).save(AnswerMapper.fromFormToEntity(getAnswerForm()));
    }

    @Test
    void testRegisterAnswerWithNoPoints() {
        doThrow(ConstraintViolationException.class).when(repository).save(AnswerMapper.fromFormToEntity(getAnswerFormWithNoPoints()));
        Exception e = assertThrows(RequestException.class, () -> service.registerAnswer(getAnswerFormWithNoPoints()));
        assertEquals("Every answer must have a score.", e.getMessage());
    }

    @Test
    void testRegisterAnswerException() {
        doThrow(RequestException.class).when(repository).save(AnswerMapper.fromFormToEntity(getAnswerForm()));
        Exception e = assertThrows(RequestException.class, () -> service.registerAnswer(getAnswerForm()));
        assertEquals("Error when registering answer.", e.getMessage());
    }

    @Test
    void testGetAllAnswers() {
        doReturn(getAllAnswers()).when(repository).findAll();
        Stream<AnswerResponse> response = service.getAllAnswers();
        assertNotNull(response);
    }

    @Test
    void testGetAllAnswersException() {
        doThrow(RequestException.class).when(repository).findAll();
        Exception exception = assertThrows(Exception.class, () -> service.getAllAnswers());
        assertEquals("Error when getting all answers.", exception.getMessage());
    }

    @Test
    void testGetAnswerById() {
        doReturn(getOptionalAnswer()).when(repository).findById(ID);
        Optional<AnswerResponse> response = service.getAnswerById(ID);
        assertNotNull(response);
    }

    @Test
    void testGetAnswerByIdWithNonexistentId() {
        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Answer does not exist.", exception.getMessage());
    }

    @Test
    void testGetAnswerByIdException() {
        doThrow(RequestException.class).when(repository).findById(ID);
        Exception exception = assertThrows(RequestException.class, () -> service.getAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Error when getting answer by id.", exception.getMessage());
    }

    @Test
    void testDeleteAnswer() {
        doReturn(getOptionalAnswer()).when(repository).findById(ID);
        String response = service.deleteAnswerById(ID);
        assertNotNull(response);
        assertEquals("Answer deleted.", response);
    }

    @Test
    void testDeleteAnswerWithNonexistentId() {
        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Answer does not exist.", exception.getMessage());
    }

    @Test
    void testDeleteAnswerException() {
        doThrow(RequestException.class).when(repository).findById(ID);
        Exception exception = assertThrows(RequestException.class, () -> service.deleteAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Error when deleting answer by id.", exception.getMessage());
    }

    private AnswerForm getAnswerForm() {
        return AnswerForm.builder()
                .response(RESPONSE)
                .points(POINTS)
                .build();
    }

    private AnswerForm getAnswerFormWithNoPoints() {
        return AnswerForm.builder()
                .response(RESPONSE)
                .build();
    }

    private Answer getAnswer() {
        return Answer.builder()
                .id(ID)
                .response(RESPONSE)
                .points(POINTS)
                .build();
    }

    private List<Answer> getAllAnswers() {
        return List.of(Answer.builder()
                .id(ID)
                .points(POINTS)
                .response(RESPONSE
                ).build());
    }

    private Optional<Answer> getOptionalAnswer() {
        return Optional.of(Answer.builder()
                .id(ID)
                .response(RESPONSE)
                .points(POINTS)
                .build());
    }
}
