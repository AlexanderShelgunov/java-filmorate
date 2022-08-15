package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(int filmId) {

        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, FILM_RELEASE_DATE, FILM_DESCRIPTION, " +
                "FILM_DURATION, FILM_RATE, FILM_MPA_ID, MPA.MPA_ID, MPA.MPA_NAME " +
                "FROM FILMS LEFT JOIN MPA ON FILMS.FILM_MPA_ID = MPA.MPA_ID WHERE FILM_ID = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, numRow) -> rowToFilm(rs, getFilmGenresById(filmId)), filmId);

        if (films.size() != 1) {
            return null;
        }
        return films.get(0);
    }

    @Override
    public Film saveFilm(Film film) {
        String sqlQuery = "INSERT INTO FILMS(FILM_NAME, FILM_RELEASE_DATE, FILM_DESCRIPTION, " +
                "FILM_DURATION, FILM_RATE, FILM_MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            final LocalDate releaseDate = film.getReleaseDate();

            if (releaseDate == null) {
                stmt.setNull(2, Types.DATE);
            } else {
                stmt.setDate(2, Date.valueOf(releaseDate));
            }

            stmt.setString(3, film.getDescription());
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        setFilmGenre(film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            return null;
        }

        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        sqlQuery = "UPDATE FILMS SET " +
                "FILM_NAME = ?, " +
                "FILM_RELEASE_DATE = ?, " +
                "FILM_DESCRIPTION = ?, " +
                "FILM_DURATION = ?, " +
                "FILM_RATE = ?, " +
                "FILM_MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        setFilmGenre(film);

        return film;
    }

    @Override
    public List<Film> findAllFilms() {

        final Map<Long, Set<Genre>> filmsGenres = getAllFilmsGenres();
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, FILM_RELEASE_DATE, FILM_DESCRIPTION, " +
                "FILM_DURATION, FILM_RATE, FILM_MPA_ID, MPA.MPA_ID, MPA.MPA_NAME " +
                "FROM FILMS " +
                "LEFT JOIN MPA ON FILMS.FILM_MPA_ID = MPA.MPA_ID";

        return jdbcTemplate.query(sqlQuery, (rs, numRow) -> {
            final Long filmId = rs.getLong("FILM_ID");
            return rowToFilm(rs, filmsGenres.get(filmId));
        });
    }

    @Override
    public void likeTheFilm(User user, Film film) {

        final String sqlQuery = "MERGE INTO LIKES(FILM_ID, USER_ID) VALUES (?, ?)";

        jdbcTemplate.update(
                sqlQuery,
                film.getId(),
                user.getId());
    }

    @Override
    public void removeLikeFromFilm(User user, Film film) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                film.getId(),
                user.getId());
    }

    @Override
    public List<Film> findPopularFilms(int count) {

        final String sqlQuery = "SELECT F.FILM_ID, F.FILM_NAME, F.FILM_RELEASE_DATE, F.FILM_DESCRIPTION, " +
                "F.FILM_DURATION, F.FILM_RATE, F.FILM_MPA_ID, MPA.MPA_ID, MPA.MPA_NAME " +
                "FROM FILMS F " +
                "LEFT JOIN (SELECT FILM_ID, COUNT(*) LIKES_COUNT FROM LIKES " +
                "GROUP BY FILM_ID) L ON F.FILM_ID = L.FILM_ID " +
                "LEFT JOIN MPA ON F.FILM_MPA_ID = MPA.MPA_ID " +
                "ORDER BY L.LIKES_COUNT DESC LIMIT ?";

        final Map<Long, Set<Genre>> filmsGenres = getAllFilmsGenres();

        return jdbcTemplate.query(sqlQuery, (rs, numRow) -> {
            final Long filmId = rs.getLong("film_id");
            return rowToFilm(rs, filmsGenres.get(filmId));
        }, count);
    }

    public void setFilmGenre(Film film) {

        final String deleteGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteGenres, film.getId());

        final Set<Genre> filmGenres = film.getGenres();

        if (filmGenres != null) {
            final String genreSaveSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
            filmGenres.forEach(x -> jdbcTemplate.update(genreSaveSql, film.getId(), x.getId()));
        }
    }

private Set<Genre> getFilmGenresById(int id) {
    final String sql = "SELECT * FROM FILM_GENRE INNER JOIN GENRES ON GENRES.GENRE_ID = FILM_GENRE.GENRE_ID"
            + " WHERE FILM_ID = ?";

    return new HashSet<>(jdbcTemplate.query(sql, (rs, getNum) -> Genre.builder().id(rs.getInt("GENRE_ID"))
            .name(rs.getString("GENRE_NAME")).build(), id));
}

    private Map<Long, Set<Genre>> getAllFilmsGenres() {
        final String sql = "SELECT * FROM FILM_GENRE INNER JOIN GENRES ON GENRES.GENRE_ID = FILM_GENRE.GENRE_ID";

        final Map<Long, Set<Genre>> filmsGenres = new HashMap<>();

        jdbcTemplate.query(sql, (RowCallbackHandler) rs -> {
            final Long filmId = rs.getLong("FILM_ID");
            filmsGenres.getOrDefault(filmId, new HashSet<>()).add(Genre.builder().id(rs.getInt("GENRE_ID"))
                    .name(rs.getString("GENRE_NAME")).build());
        });
        return filmsGenres;
    }

    private static Film rowToFilm(ResultSet rs, Set<Genre> genres) throws SQLException {

        return Film.builder()
                .id(rs.getInt("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .releaseDate(rs.getDate("FILM_RELEASE_DATE").toLocalDate())
                .description(rs.getString("FILM_DESCRIPTION"))
                .duration(rs.getInt("FILM_DURATION"))
                .rate(rs.getInt("FILM_RATE"))
                .mpa(Mpa.builder().id(rs.getInt("MPA_ID")).name(rs.getString("MPA_NAME")).build())
                .genres(genres != null && genres.isEmpty() ? null : genres)
                .build();
    }
}
