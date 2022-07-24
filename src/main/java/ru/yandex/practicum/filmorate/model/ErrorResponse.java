package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String errorMessage;


    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
