package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int idGenerator = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();


    @Override
    public Film getFilm(int filmId) {
        return films.get(filmId);
    }

    @Override
    public Film saveFilm(Film film) {
        film.setId(++idGenerator);
        return films.put(film.getId(), film);
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public ArrayList<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void likeTheFilm(User user, Film film) {
        film.getLikeUserIds().add(user.getId());
    }

    @Override
    public void removeLikeFromFilm(User user, Film film) {
        film.getLikeUserIds().remove(user.getId());
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        List<Film> likes = new ArrayList<>(films.values());

        return likes
                .stream()
                .sorted((p0, p1) -> {
                    Integer sizeOne = p0.getLikeUserIds().size();
                    Integer sizeTwo = p1.getLikeUserIds().size();
                    return sizeTwo.compareTo(sizeOne);
                }).limit(count)
                .collect(Collectors.toList());

//        return likes
//                .stream()
//                .sorted(Comparator.comparingInt(f -> f.getLikeUserIds().size()))
//                .limit(count)
//                .collect(Collectors.toList());

    }
}
