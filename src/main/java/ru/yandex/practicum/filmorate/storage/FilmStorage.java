package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public interface FilmStorage {

    Film getFilmById(int filmId);

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findAllFilms();

    void likeTheFilm(User user, Film film);

    void removeLikeFromFilm(User user, Film film);

    List<Film> findPopularFilms(int count);
}
