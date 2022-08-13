package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.ResultSet;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(int userId) {
        final String sqlQuery = "SELECT USER_ID, USER_LOGIN, USER_NAME, USER_EMAIL, USER_BIRTHDAY " +
                "FROM USERS " +
                "WHERE USER_ID = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
        if (users.size() != 1) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public User saveUser(User user) {
        final String sqlQuery = "INSERT INTO USERS(USER_LOGIN, USER_NAME, USER_EMAIL, USER_BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            final LocalDate birthday = user.getBirthday();

            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            return null;
        }

        String sqlQuery = "UPDATE USERS SET " +
                "USER_LOGIN = ?, USER_NAME = ?, USER_EMAIL = ?, USER_BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public List<User> findAllUsers() {
        final String sqlQuery = "SELECT USER_ID, USER_LOGIN, USER_NAME, USER_EMAIL, USER_BIRTHDAY " +
                "FROM USERS ";

        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
        return users;
    }

    @Override
    public void addAsFriend(User user, User friend) {
        final String sqlQuery = "MERGE INTO FRIENDSHIP(USER_ID, FRIENDS_ID) VALUES (?, ?)";

        jdbcTemplate.update(
                sqlQuery,
                user.getId(),
                friend.getId());
    }

    @Override
    public void deleteAsFriend(User user, User friend) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIENDS_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                user.getId(),
                friend.getId());
    }

    @Override
    public List<User> findAllFriends(User user) {
        final String sqlQuery = "SELECT u.USER_ID, u.USER_LOGIN, u.USER_NAME, u.USER_EMAIL, u.USER_BIRTHDAY " +
                "FROM USERS AS u " +
                "JOIN FRIENDSHIP f on u.USER_ID = f.FRIENDS_ID " +
                "WHERE f.USER_ID = ?";

        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, user.getId());
        return users;
    }

    @Override
    public List<User> findCommonFriends(User user, User common) {
        final String sqlQuery = "SELECT u.* FROM USERS AS u, FRIENDSHIP AS f1, FRIENDSHIP AS f2 " +
                "WHERE u.USER_ID = f1.FRIENDS_ID " +
                "AND u.USER_ID = f2.FRIENDS_ID " +
                "AND f1.USER_ID = ? " +
                "AND f2.USER_ID = ?";

        List<User> friends = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, user.getId(), common.getId());
        return friends;
    }

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getString("USER_EMAIL"),
                rs.getDate("USER_BIRTHDAY").toLocalDate());
    }
}
