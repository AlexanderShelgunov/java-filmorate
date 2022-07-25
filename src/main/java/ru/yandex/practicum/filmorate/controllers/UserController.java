package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        log.info("Пользователь {} полученый по ID={}", userService.getUser(userId), userId);
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addAsFriend(@PathVariable final int userId, @PathVariable final int friendId) {
        log.info("Пользователь {} добавляет в друзья {}", userService.getUser(userId), userService.getUser(friendId));
        userService.addAsFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteAsFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Пользователь {} удаляет из друзей {}", userService.getUser(userId), userService.getUser(friendId));
        userService.deleteAsFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public ArrayList<Integer> findAllFriends(@PathVariable int userId) {
        log.info("Пользователь {} запросил список друзей", userService.getUser(userId));
        return userService.findAllFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ArrayList<Integer> findCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("Пользователь {} запросил список общих друзей c пользователем {}",
                userService.getUser(userId), userService.getUser(otherId));
        return userService.findCommonFriends(userId, otherId);
    }

    @GetMapping
    public ArrayList<User> findAll() {
        log.info("Текущее количество пользователей: {}", userService.findAll().size());
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        checkValidation(user);
        log.info("Сохраняемый пользователь: {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        checkValidation(user);
        log.info("Обновляемый пользователь: {}", user);
        return userService.update(user);
    }

    public void checkValidation(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate dateNow = LocalDate.now();
        String dateNowString = dateNow.format(formatter);
        LocalDate dateNowFormat = LocalDate.parse(dateNowString, formatter);
        String birthdayDateString = user.getBirthday().format(formatter);
        LocalDate birthdayDateFormat = LocalDate.parse(birthdayDateString, formatter);

        if (user.getEmail() == null || "".equals(user.getEmail()) || !user.getEmail().contains("@")) {
            log.info("электронная почта {} не корректная", user.getEmail());
            throw new ValidateException("электронная почта " + user.getEmail() +
                    " не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || "".equals(user.getLogin()) || user.getLogin().contains(" ")) {
            log.info("Логин \"{}\" пустой или содержит пробелы", user.getLogin());
            throw new ValidateException("Логин " + user.getLogin() + " не может быть пустым и содержать пробелы");
        }

        if (birthdayDateFormat.isAfter(dateNowFormat)) {
            log.info("Дата рождения {} не может быть в будущем", user.getBirthday());
            throw new ValidateException("Дата рождения " + user.getBirthday() + " не может быть в будущем");
        }
    }
}
