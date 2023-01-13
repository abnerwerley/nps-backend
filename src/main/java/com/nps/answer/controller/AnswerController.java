package com.nps.answer.controller;

import com.nps.answer.json.AnswerForm;
import com.nps.answer.json.AnswerResponse;
import com.nps.answer.service.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;

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
    Stream<AnswerResponse> getAllAnswers() {
        return service.getAllAnswers();
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
