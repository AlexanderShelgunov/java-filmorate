package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    FilmService filmService;

    @GetMapping
    public ArrayList<Film> findAll() {
        log.info("Текущее количество фильмов: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeTheFilm (@PathVariable final int filmId, @PathVariable final int userId) {
        return filmService.likeTheFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film removeLikeFromFilm (@PathVariable final int filmId, @PathVariable final int userId) {
        return filmService.removeLikeFromFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public ArrayList<Film> findPopularFilms(
            @RequestParam (value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.findPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidateException {
        checkValidation(film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidateException {
        checkValidation(film);
        return filmService.update(film);
    }

    public void checkValidation(Film film) throws ValidateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate minDate = LocalDate.parse("1895-12-28", formatter);
        String dateOfFilm = film.getReleaseDate().format(formatter);
        LocalDate parsedDateOfFilm = LocalDate.parse(dateOfFilm, formatter);

        if (film.getName() == null || "".equals(film.getName())) {
            log.info("Название фильма {} пустое", film.getName());
            throw new ValidateException("Название фильма \"" + film.getName() + "\" не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.info("Описание фильма длиннее 200 символов: {}", film.getDescription().length());
            throw new ValidateException("Описание фильма не может быть больше 200 символов: " + film.getName());
        }

        if (parsedDateOfFilm.isBefore(minDate)) {
            log.info("Дата фильма {} раньше 28 декабря 1895 года", film.getReleaseDate());
            throw new ValidateException("Дата фильма " + film.getReleaseDate() +
                    " не может быть раньше 28 декабря 1895 года;");
        }

        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма {} отрицательное", film.getDuration());
            throw new ValidateException("Продолжительность фильма \"" + film.getDuration() +
                    "\" не может быть отрицательной или 0");
        }
    }
}
