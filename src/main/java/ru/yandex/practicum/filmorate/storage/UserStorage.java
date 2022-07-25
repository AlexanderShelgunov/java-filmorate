package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;


@Component
public interface UserStorage {

    User getUser(int userId);

    User saveUser(User user);

    User updateUser(User user);

    ArrayList<User> findAllUsers();

    void addAsFriend(User user, User friend);

    void deleteAsFriend(User user, User friend);

    ArrayList<Integer> findAllFriends(User user);

    ArrayList<Integer> findCommonFriends(User user, User common);
}
