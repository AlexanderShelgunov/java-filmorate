package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int idGenerator = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();


    @Override
    public Film getFilm(int filmId) {
        return null;
    }

    @Override
    public Film saveFilm(Film film) {
        film.setId(++idGenerator);
        films.put(film.getId(), film);
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public ArrayList<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }
}
