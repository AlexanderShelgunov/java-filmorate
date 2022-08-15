package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        log.info("Фильм {} полученый по ID={}", filmService.getFilmById(filmId), filmId);
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void likeTheFilm (@PathVariable final int filmId, @PathVariable final int userId) {
        log.info("Пользователь {} ставит лайк фильму {}", filmService.getUser(userId), filmService.getFilmById(filmId));
        filmService.addLikeTheFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLikeFromFilm (@PathVariable final int filmId, @PathVariable final int userId) {
        log.info("Пользователь {} удаляет лайк фильма {}", filmService.getUser(userId), filmService.getFilmById(filmId));
        filmService.removeLikeFromFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(
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
        log.info("Обновляемый фильм: {}", film);
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
