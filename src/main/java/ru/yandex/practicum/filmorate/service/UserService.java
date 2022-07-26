package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserStorage userStorage;

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
            if (user.getId() != null) {
                throw new ServerException("Пользователь c id=" + user.getId() + " уже существует");
            }

            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
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
            userStorage.updateUser(user);
        }
        return user;
    }

    public void addAsFriend(int userId, int friendId) {
        final User user = userStorage.getUser(userId);
        final User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь " + user + " или " + friend +" не найден");
        }
        userStorage.addAsFriend(user, friend);
    }

    public void deleteAsFriend(int userId, int friendId) {
        final User user = userStorage.getUser(userId);
        final User friend = userStorage.getUser(friendId);

        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь " + user + " или " + friend +" не найден");
        }
        userStorage.deleteAsFriend(user, friend);
    }

    public ArrayList<User> findAllFriends(int userId) {
        final User user = userStorage.getUser(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь " + user + " не найден");
        }

        return userStorage.findAllFriends(user);

    }

    public List<User> findCommonFriends(int userId, int commonId) {
        final User user = userStorage.getUser(userId);
        final User common = userStorage.getUser(commonId);

        if (user == null || common == null) {
            throw new NotFoundException("Пользователь " + user + " или " + common +" не найдены");
        }

        return userStorage.findCommonFriends(user, common);
    }
}
