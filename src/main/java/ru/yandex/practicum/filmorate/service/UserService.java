package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserStorage userStorage;

    public User getUser(int userId) {
        final User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return userStorage.getUser(userId);
    }

    public ArrayList<User> findAll() {
        return userStorage.findAllUsers();
    }

    public User create(User user) {
        if (user != null) {
            final int id = user.getId();

            if (userStorage.getUser(id) != null) {
                throw new ServerException("Пользователь c id=" + id + " уже существует");
            }

            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            log.info("Сохраняемый пользователь: {}", user);
            userStorage.saveUser(user);
        }
        return user;
    }

    public User update(User user) {
        if (user != null) {
            final int id = user.getId();

            if (userStorage.getUser(id) == null) {
                throw new NotFoundException("Пользователь c id=" + id + " не найден");
            }

            log.info("Обновляемый пользователь: {}", user);
            userStorage.updateUser(user);
        }
        return user;
    }

    //TODO
    public User addAsFriend(int userId, int friendId) {
        return null;
    }

    //TODO
    public User deleteAsFriend(int userId, int friendId) {
        return null;
    }

    //TODO
    public ArrayList<User> findAllFriends() {
        return null;
    }

    //TODO
    public ArrayList<User> findCommonFriends() {
        return null;
    }
}
