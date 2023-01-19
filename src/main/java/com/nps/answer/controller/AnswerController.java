package com.nps.answer.controller;

import com.nps.answer.json.AnswerForm;
import com.nps.answer.json.AnswerResponse;
import com.nps.answer.service.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/answer")
@AllArgsConstructor
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class AnswerController {

    @Autowired
    private AnswerService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    AnswerResponse registerAnswer(@RequestBody AnswerForm form) {
        return service.registerAnswer(form);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<AnswerResponse> filterAnswer(@RequestParam(value = "answerId", required = false) Long answerId,
                                      @RequestParam(value = "response", required = false) String response,
                                      @RequestParam(value = "score", required = false) Integer score,
                                      @RequestParam(value = "questionId", required = false) Long questionId) {
        return service.filterAnswer(answerId, response, score, questionId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Optional<AnswerResponse> getAnswerById(@PathVariable Long id) {
        return service.getAnswerById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteAnswerById(@PathVariable Long id) {
        return service.deleteAnswerById(id);
    }

}
