package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private LocalDate releaseDate;
    private String description;
    private int duration;
    private int rate;
    private Mpa mpa;
    private Set<Genre> genres;

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}
