package com.example.recyclingAppBackend.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizProgress {

    @Builder.Default
    private QuizDay day1 = new QuizDay();

    @Builder.Default
    private QuizDay day2 = new QuizDay();

    @Builder.Default
    private QuizDay day3 = new QuizDay();
}
