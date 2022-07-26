package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int idGenerator = 0;
    final Map<Integer, User> users = new HashMap<>();

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public User saveUser(User user) {
        user.setId(++idGenerator);
        return users.put(user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.put(user.getId(), user);
    }

    @Override
    public ArrayList<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addAsFriend(User user, User friend) {
        user.getFriendsId().add(friend);
        friend.getFriendsId().add(user);
    }

    @Override
    public void deleteAsFriend(User user, User friend) {
        user.getFriendsId().remove(friend);
        friend.getFriendsId().remove(user);
    }

    @Override
    public ArrayList<User> findAllFriends(User user) {
        return new ArrayList<>(user.getFriendsId());
    }

    @Override
    public List<User> findCommonFriends(User user, User common) {
        final Set<User> userFriends = user.getFriendsId();
        final Set<User> otherFriends = common.getFriendsId();

        return userFriends
                .stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }
}
