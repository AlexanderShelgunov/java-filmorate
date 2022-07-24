package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {

    int idGenerator = 0;
    final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public User saveUser(User user) {
        user.setId(++idGenerator);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public ArrayList<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
}
