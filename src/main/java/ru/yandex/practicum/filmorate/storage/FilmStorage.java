package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

@Component
public interface FilmStorage {

    Film getFilm(int filmId);

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    ArrayList<Film> findAllFilms();

}
