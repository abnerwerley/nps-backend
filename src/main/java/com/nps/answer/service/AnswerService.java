package com.nps.answer.service;

import com.nps.answer.entity.Answer;
import com.nps.answer.entity.mapper.AnswerMapper;
import com.nps.answer.json.AnswerForm;
import com.nps.answer.json.AnswerResponse;
import com.nps.answer.json.mapper.AnswerResponseMapper;
import com.nps.answer.persistence.AnswerCustomRepository;
import com.nps.answer.persistence.AnswerRepository;
import com.nps.exception.RequestException;
import com.nps.exception.ResourceNotFoundException;
import com.nps.question.entity.Question;
import com.nps.question.persistence.QuestionRepository;
import com.nps.question.utils.ValidateFirstAnswer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AnswerService {

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerCustomRepository customRepository;

    @Autowired
    private ValidateFirstAnswer validate;

    public static final String ANSWER_DOES_NOT_EXIST = "Answer does not exist.";

    public static final String QUESTION_NOT_FOUND = "Question not found with id: ";

    public AnswerResponse registerAnswer(AnswerForm form) {
        try {
            Optional<Question> question = questionRepository.findById(form.getQuestionId());
            if (question.isEmpty()) {
                throw new ResourceNotFoundException(QUESTION_NOT_FOUND + form.getQuestionId());
            }
            validate.validateFirstAnswer(form, question.get());
            Answer answer = AnswerMapper.fromFormToEntity(form);
            if (form.getScore() == null) {
                throw new NullPointerException("Every answer must not have a null score.");
            }
            if (answer.getResponse() == null || answer.getResponse().equals("")) {
                answer.setResponse("");
            }
            repository.save(answer);
            return AnswerResponseMapper.fromEntityToResponse(answer);

        } catch (RequestException e) {
            log.info(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (ResourceNotFoundException e) {
            log.info(QUESTION_NOT_FOUND + form.getQuestionId());
            throw new ResourceNotFoundException(QUESTION_NOT_FOUND + form.getQuestionId());

        } catch (NullPointerException e) {
            log.error("Every answer must not have a null core.");
            throw new RequestException("Every answer must not have a null score.");

        } catch (Exception e) {
            log.error("Error when registering answer.");
            throw new RequestException("Error when registering answer.");
        }
    }

    public List<AnswerResponse> filterAnswer(Long id, String response, Integer score, Long questionId) {
        try {
            return customRepository.findAnswer(id, response, score, questionId)
                    .stream()
                    .map(AnswerResponseMapper::fromEntityToResponse)
                    .toList();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RequestException("Error when getting answers.");
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
