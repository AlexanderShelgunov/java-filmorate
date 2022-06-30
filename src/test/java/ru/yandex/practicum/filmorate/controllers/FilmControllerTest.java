package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
        film = new Film();

        film.setName("film");
        film.setDescription("film description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);
    }

    @Test
    void validateEmptyName() {

        assertDoesNotThrow(() -> filmController.checkValidation(film));

        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.checkValidation(film));

        film.setName(null);
        assertThrows(ValidationException.class, () -> filmController.checkValidation(film));

    }

    @Test
    void validateMaxLengthDescription() {

        //length 200
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Donec tincidunt sagittis lorem quis rhoncus. Etiam tempus tortor eu neque placerat consectetur. " +
                "Donec interdum cursus condimentum. Nunc quis el");

        assertDoesNotThrow(() -> filmController.checkValidation(film));

        //length 201
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Donec tincidunt sagittis lorem quis rhoncus. Etiam tempus tortor eu neque placerat consectetur. " +
                "Donec interdum cursus condimentum. Nunc quis el.");

        assertThrows(ValidationException.class, () -> filmController.checkValidation(film));

    }

    @Test
    void validateDate() {

        LocalDate date = LocalDate.of(1895, 12, 29);
        film.setReleaseDate(date);
        assertDoesNotThrow(() -> filmController.checkValidation(film));

        date = LocalDate.of(1895, 12, 28);
        film.setReleaseDate(date);
        assertDoesNotThrow(() -> filmController.checkValidation(film));

        date = LocalDate.of(1895, 12, 27);
        film.setReleaseDate(date);
        assertThrows(ValidationException.class, () -> filmController.checkValidation(film));

    }

    @Test
    void validateDurationMustBePositive() {

        film.setDuration(1);
        assertDoesNotThrow(() -> filmController.checkValidation(film));

        film.setDuration(-90);
        assertThrows(ValidationException.class, () -> filmController.checkValidation(film));

    }
}