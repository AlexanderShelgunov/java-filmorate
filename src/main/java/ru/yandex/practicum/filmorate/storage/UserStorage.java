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

}
