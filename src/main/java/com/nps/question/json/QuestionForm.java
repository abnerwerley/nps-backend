package com.nps.question.json;

import lombok.*;

import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {

    @Size(min = 5)
    private String enquiry;
}
