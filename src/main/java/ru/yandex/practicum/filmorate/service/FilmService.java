package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;

@Slf4j
@Service
public class FilmService {

    @Autowired
    FilmStorage filmStorage;

    public ArrayList<Film> findAll() {
        return filmStorage.findAllFilms();
    }

    public Film create(Film film) {
        if (film != null) {
            final int id = film.getId();

            if (filmStorage.getFilm(id) != null) {
                throw new ServerException("Фильм c таким id=" + id + " уже существует");
            }

            filmStorage.saveFilm(film);
        }
        return film;
    }

    public Film update(Film film) {
        if (film != null) {
            final int id = film.getId();

            if (filmStorage.getFilm(id) == null) {
                log.info("Фильма с таким ID {} не существует", film.getId());
                throw new NotFoundException("Фильма с таким ID " + film.getId() + " не существует");
            }
        }

        log.info("Обновляемый фильм: {}", film);
        filmStorage.updateFilm(film);
        return film;
    }

    //TODO
    public Film likeTheFilm (int filmId, final int userId) {


        return null;
    }

    //TODO
    public Film removeLikeFromFilm (int filmId, final int userId) {


        return null;
    }

    //TODO
    public ArrayList<Film> findPopularFilms(int count) {
        if (count < 0) {
            throw new ServerException("Параметр count=" + count + " не может быть отрицательным");
        }
        return null;
    }

}
