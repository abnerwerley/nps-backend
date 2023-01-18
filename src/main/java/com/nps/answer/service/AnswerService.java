package com.nps.answer.service;

import com.nps.answer.entity.Answer;
import com.nps.answer.entity.mapper.AnswerMapper;
import com.nps.answer.json.AnswerForm;
import com.nps.answer.json.AnswerResponse;
import com.nps.answer.json.mapper.AnswerResponseMapper;
import com.nps.answer.persistence.AnswerRepository;
import com.nps.exception.RequestException;
import com.nps.exception.ResourceNotFoundException;
import com.nps.question.entity.Question;
import com.nps.question.persistence.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class AnswerService {

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    public static final String ANSWER_DOES_NOT_EXIST = "Answer does not exist.";

    public static final String QUESTION_NOT_FOUND = "Question not found with id: ";

    public AnswerResponse registerAnswer(AnswerForm form) {
        try {
            Optional<Question> question = questionRepository.findById(form.getQuestionId());
            if (question.isPresent()) {
                Answer answer = AnswerMapper.fromFormToEntity(form);
                return AnswerResponseMapper.fromEntityToResponse(repository.save(answer));
            }
            throw new ResourceNotFoundException(QUESTION_NOT_FOUND + form.getQuestionId());
        } catch (NullPointerException e) {
            log.error("Every answer must have a score.");
            throw new RequestException("Every answer must have a score.");

        } catch (ResourceNotFoundException e) {
            log.info(QUESTION_NOT_FOUND + form.getQuestionId());
            throw new ResourceNotFoundException(QUESTION_NOT_FOUND + form.getQuestionId());

        } catch (Exception e) {
            log.error("Error when registering answer.");
            throw new RequestException("Error when registering answer.");
        }
    }

    public Stream<AnswerResponse> getAllAnswers() {
        try {
            return repository.findAll().stream().map(AnswerResponseMapper::fromEntityToResponse);
        } catch (Exception e) {
            log.error("Error when getting all answers.");
            throw new RequestException("Error when getting all answers.");
        }
    }

    public Optional<AnswerResponse> getAnswerById(Long id) {
        try {
            Optional<AnswerResponse> answer = repository.findById(id).map(AnswerResponseMapper::fromEntityToResponse);
            if (answer.isPresent()) {
                return answer;
            }
            throw new ResourceNotFoundException(ANSWER_DOES_NOT_EXIST);
        } catch (ResourceNotFoundException exception) {
            log.error(ANSWER_DOES_NOT_EXIST);
            throw new ResourceNotFoundException(ANSWER_DOES_NOT_EXIST);
        } catch (Exception e) {
            log.error("Error when getting answer by id.");
            throw new RequestException("Error when getting answer by id.");
        }
    }

    public String deleteAnswerById(Long id) {
        try {
            Optional<Answer> answer = repository.findById(id);
            if (answer.isPresent()) {
                repository.deleteById(id);
                return "Answer deleted.";
            }
            throw new ResourceNotFoundException(ANSWER_DOES_NOT_EXIST);
        } catch (ResourceNotFoundException exception) {
            log.error(ANSWER_DOES_NOT_EXIST);
            throw new ResourceNotFoundException(ANSWER_DOES_NOT_EXIST);
        } catch (Exception e) {
            log.error("Error when deleting answer by id.");
            throw new RequestException("Error when deleting answer by id.");
        }
    }
}
