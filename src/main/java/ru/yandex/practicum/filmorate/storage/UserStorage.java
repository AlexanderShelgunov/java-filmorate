package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;


@Component
public interface UserStorage {

    User getUser(int userId);

    User saveUser(User user);

    User updateUser(User user);

    List<User> findAllUsers();

    void addAsFriend(User user, User friend);

    void deleteAsFriend(User user, User friend);

    List<User> findAllFriends(User user);

    List<User> findCommonFriends(User user, User common);

}
