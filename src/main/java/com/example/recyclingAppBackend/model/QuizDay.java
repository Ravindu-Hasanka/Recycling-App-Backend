package com.example.recyclingAppBackend.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDay {

    @Builder.Default
    private Integer score = 0;

    @Builder.Default
    private QuizStatus status = QuizStatus.NOT_START;
}
