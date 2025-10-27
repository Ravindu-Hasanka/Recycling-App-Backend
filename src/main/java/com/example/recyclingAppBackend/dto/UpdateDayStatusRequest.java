package com.example.recyclingAppBackend.dto;

import com.example.recyclingAppBackend.model.QuizStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDayStatusRequest {

    @NotNull
    private QuizStatus status;
}
