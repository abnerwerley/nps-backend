package com.nps.question.service;

import com.nps.exception.RequestException;
import com.nps.exception.ResourceNotFoundException;
import com.nps.question.entity.Question;
import com.nps.question.entity.mapper.QuestionMapper;
import com.nps.question.json.QuestionForm;
import com.nps.question.json.QuestionResponse;
import com.nps.question.json.QuestionUpdateForm;
import com.nps.question.json.mapper.QuestionResponseMapper;
import com.nps.question.persistence.QuestionCustomRepository;
import com.nps.question.persistence.QuestionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionService {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private QuestionCustomRepository customRepository;

    public static final String QUESTION_DOES_NOT_EXIST = "Question does not exist.";

    public QuestionResponse registerQuestion(QuestionForm form) {
        try {
            Question question = QuestionMapper.fromFormToEntity(form);
            repository.save(question);
            return QuestionResponseMapper.fromEntityToResponse(question);
        } catch (Exception e) {
            log.error("Error when registering a question.");
            throw new RequestException("Error when registering a question.");
        }

    }

    public List<QuestionResponse> getAllQuestions(Long questionId, String enquiry) {
        try {
            return customRepository.findQuestions(questionId, enquiry)
                    .stream()
                    .map(QuestionResponseMapper::fromEntityToResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Error when getting all questions.");
            throw new RequestException("Error when getting all questions.");
        }
    }

    public Optional<QuestionResponse> getQuestionById(Long id) {
        try {
            Optional<QuestionResponse> optionalQuestion = repository.findById(id).map(QuestionResponseMapper::fromEntityToResponse);
            if (optionalQuestion.isPresent()) {
                return optionalQuestion;
            }
            throw new ResourceNotFoundException("Question not found with id: " + id);
        } catch (ResourceNotFoundException e) {
            log.error("Question not found with id: " + id);
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Could not get question by id.");
            throw new RequestException("Could not get question by id.");
        }
    }

    public QuestionResponse updateQuestion(QuestionUpdateForm form) {
        try {
            Optional<Question> question = repository.findById(form.getId());
            if (question.isPresent()) {
                Question updating = question.get();
                updating.setEnquiry(form.getEnquiry());
                repository.save(updating);
                return QuestionResponseMapper.fromEntityToResponse(updating);
            }
            throw new ResourceNotFoundException(QUESTION_DOES_NOT_EXIST);
        } catch (ResourceNotFoundException exception) {
            log.error(QUESTION_DOES_NOT_EXIST);
            throw new ResourceNotFoundException(QUESTION_DOES_NOT_EXIST);
        } catch (Exception e) {
            log.error("Error when updating question.");
            throw new RequestException("Error when updating question.");
        }
    }

    public String deleteQuestion(Long id) {
        try {
            Optional<Question> question = repository.findById(id);
            if (question.isPresent()) {
                repository.deleteById(id);
                return "Question deleted.";
            }
            throw new ResourceNotFoundException(QUESTION_DOES_NOT_EXIST);
        } catch (ResourceNotFoundException exception) {
            log.error(QUESTION_DOES_NOT_EXIST);
            throw new ResourceNotFoundException(QUESTION_DOES_NOT_EXIST);
        } catch (Exception e) {
            log.error("Error when deleting question by id.");
            throw new RequestException("Error when deleting question by id.");
        }
    }
}
