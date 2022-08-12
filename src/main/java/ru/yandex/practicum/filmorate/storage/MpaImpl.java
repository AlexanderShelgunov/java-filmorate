package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Component
public class MpaImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Mpa getMpa(int mpaId) {
        final String sqlQuery = "SELECT MPA_ID, MPA_NAME " +
                "FROM MPA " +
                "WHERE MPA_ID = ?";

        List<Mpa> mpas = jdbcTemplate.query(sqlQuery, MpaImpl::makeMpa, mpaId);

        if (mpas.size() != 1) {
            return null;
        }
        return mpas.get(0);
    }

    @Override
    public List<Mpa> getAllMpa() {
        final String sqlQuery = "SELECT MPA_ID, MPA_NAME " +
                "FROM MPA ";

        List<Mpa> mpas = jdbcTemplate.query(sqlQuery, MpaImpl::makeMpa);

        return mpas;
    }


    private static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {

        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }


}
