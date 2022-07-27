package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;


    public Film getFilm(int filmId) {
        final Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        return filmStorage.getFilm(filmId);
    }

    public User getUser(int userId) {
        final User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return userStorage.getUser(userId);
    }

    public ArrayList<Film> findAll() {
        return filmStorage.findAllFilms();
    }

    public Film create(Film film) {
        if (film != null) {
            if (film.getId() != null) {
                throw new ServerException("Фильм c таким id=" + film.getId() + " уже существует");
            }

            filmStorage.saveFilm(film);
        }
        return film;
    }

    public Film update(Film film) {
        if (film != null) {
            final int id = film.getId();

            if (filmStorage.getFilm(id) == null) {
                throw new NotFoundException("Фильма с таким ID " + film.getId() + " не существует");
            }
        }
        return filmStorage.updateFilm(film);
    }

    public void addLikeTheFilm(int filmId, final int userId) {
        final User user = userStorage.getUser(userId);
        final Film film = filmStorage.getFilm(filmId);

        if (user == null || film == null) {
                throw new NotFoundException("Пользователь " + user + " или фильм " + film +" не найден");
        }

        filmStorage.likeTheFilm(user, film);
    }

    public void removeLikeFromFilm (int filmId, final int userId) {
        final User user = userStorage.getUser(userId);
        final Film film = filmStorage.getFilm(filmId);

        if (user == null || film == null) {
            throw new NotFoundException("Пользователь " + user + " или фильм " + film +" не найден");
        }

        filmStorage.removeLikeFromFilm(user, film);
    }


    public List<Film> findPopularFilms(int count) {
        if (count < 0) {
            throw new ServerException("Параметр count=" + count + " не может быть отрицательным");
        }
        return filmStorage.findPopularFilms(count);
    }

}
