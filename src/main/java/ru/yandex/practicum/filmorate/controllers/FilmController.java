package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int idGenerator = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ArrayList<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (film != null) {
            film.setId(++idGenerator);
            checkValidation(film);
            log.info("Сохраняемый фильм: {}", films);
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        checkValidation(film);
        log.info("Обновляемый фильм: {}", films);
        if (films.get(film.getId()) == null) {
            log.info("Фильма с таким ID {} не существует", film.getId());
            throw new ValidationException("Фильма с таким ID " + film.getId() + " не существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    public void checkValidation(Film film) throws ValidationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate minDate = LocalDate.parse("1895-12-28", formatter);
        String dateOfFilm = film.getReleaseDate().format(formatter);
        LocalDate parsedDateOfFilm = LocalDate.parse(dateOfFilm, formatter);

        if (film.getName() == null || "".equals(film.getName())) {
            log.info("Название фильма {} пустое", film.getName());
            throw new ValidationException("Название фильма \"" + film.getName() + "\" не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.info("Описание фильма длиннее 200 символов: {}", film.getDescription().length());
            throw new ValidationException("Описание фильма не может быть больше 200 символов: " + film.getName());
        }

        if (parsedDateOfFilm.isBefore(minDate)) {
            log.info("Дата фильма {} раньше 28 декабря 1895 года", film.getReleaseDate());
            throw new ValidationException("Дата фильма " + film.getReleaseDate() +
                    " не может быть раньше 28 декабря 1895 года;");
        }

        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма {} отрицательное", film.getDuration());
            throw new ValidationException("Продолжительность фильма \"" + film.getDuration() +
                    "\" не может быть отрицательной или 0");
        }
    }
}
