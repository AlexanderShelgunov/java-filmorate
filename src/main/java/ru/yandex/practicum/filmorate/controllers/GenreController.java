package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;


@Slf4j
@RestController
@RequestMapping
public class GenreController {

    @Autowired
    private GenreDao genreDao;

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        log.info("Текущее количество жанров: {}", genreDao.getAllGenre().size());
        return genreDao.getAllGenre();
    }

    @GetMapping("/genres/{genreId}")
    public Genre findGenreById(@PathVariable int genreId) {
        log.info("Жанр {} полученый по ID={}", genreDao.getGenre(genreId), genreId);
        return genreDao.getGenre(genreId);
    }
}
