package com.nps.question.controller;

import com.nps.question.json.QuestionForm;
import com.nps.question.json.QuestionResponse;
import com.nps.question.json.QuestionUpdateForm;
import com.nps.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    QuestionResponse registerQuestion(@RequestBody QuestionForm form) {
        return service.registerQuestion(form);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    Stream<QuestionResponse> getAllQuestions() {
        return service.getAllQuestions();
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    QuestionResponse updateQuestion(@RequestBody QuestionUpdateForm form) {
        return service.updateQuestion(form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteQuestion(@PathVariable Long id) {
        return service.deleteQuestion(id);
    }
}
