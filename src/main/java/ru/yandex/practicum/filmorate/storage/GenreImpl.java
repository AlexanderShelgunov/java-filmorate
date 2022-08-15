package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int genreId) {
        if (genreId < 0) {
            throw new NotFoundException("Параметр genreId=" + genreId + " не может быть отрицательным");
        }

        final String sqlQuery = "SELECT GENRE_ID, GENRE_NAME " +
                "FROM GENRES " +
                "WHERE GENRE_ID = ?";

        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreImpl::makeGenre, genreId);

        if (genres.size() != 1) {
            return null;
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAllGenre() {
        final String sqlQuery = "SELECT GENRE_ID, GENRE_NAME " +
                "FROM GENRES " +
                "ORDER BY GENRE_ID";

        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreImpl::makeGenre);

        return genres;
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {

        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }
}
