package com.nps.question.json;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateForm {

    private Long id;
    @NotBlank
    @Size(min = 5)
    private String enquiry;
}
