package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

@Component
public interface GenreDao {

    Genre getGenre(int genreId);

    List<Genre> getAllGenre();
}
