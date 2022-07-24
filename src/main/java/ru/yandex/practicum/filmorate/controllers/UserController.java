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
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addAsFriend(@PathVariable final int userId, @PathVariable final int friendId) {
        return userService.addAsFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteAsFriend(@PathVariable int userId, @PathVariable int friendId) {
        return userService.deleteAsFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public ArrayList<User> findAllFriends() {
        return userService.findAllFriends();
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ArrayList<User> findCommonFriends() {
        return userService.findCommonFriends();
    }

    @GetMapping
    public ArrayList<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        checkValidation(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        checkValidation(user);
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
