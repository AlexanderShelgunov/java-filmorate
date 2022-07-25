package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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

    @Override
    public void addAsFriend(User user, User friend) {
        user.getFriendsId().add(friend.getId());
        friend.getFriendsId().add(user.getId());
    }

    @Override
    public void deleteAsFriend(User user, User friend) {
        user.getFriendsId().remove(friend.getId());
        friend.getFriendsId().remove(user.getId());
    }

    @Override
    public ArrayList<Integer> findAllFriends(User user) {
        return new ArrayList<>(user.getFriendsId());
    }

    @Override
    public ArrayList<Integer> findCommonFriends(User user, User common) {
        final Set<Integer> userFriends = user.getFriendsId();
        final Set<Integer> otherFriends = common.getFriendsId();
        ArrayList<Integer> result = new ArrayList<>();

        for (int userId : userFriends) {
            if (otherFriends.contains(userId)) {
                result.add(userId);
            }
        }
        return result;
    }
}
