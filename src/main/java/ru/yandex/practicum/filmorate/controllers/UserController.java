package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int idGenerator = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public ArrayList<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {

        if (user != null) {
            user.setId(++idGenerator);
            checkValidation(user);

            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            log.info("Сохраняемый пользователь: {}", user);
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        checkValidation(user);
        log.info("Обновляемый пользователь: {}", user);
        if (users.get(user.getId()) == null) {
            log.info("Пользователь с таким ID {} не существует", user.getId());
            throw new ValidationException("Пользователь с таким ID " + user.getId() + " не существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    public void checkValidation(User user) throws ValidationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate dateNow = LocalDate.now();
        String dateNowString = dateNow.format(formatter);
        LocalDate dateNowFormat = LocalDate.parse(dateNowString, formatter);
        String birthdayDateString = user.getBirthday().format(formatter);
        LocalDate birthdayDateFormat = LocalDate.parse(birthdayDateString, formatter);

        if (user.getEmail() == null || "".equals(user.getEmail()) || !user.getEmail().contains("@")) {
            log.info("электронная почта {} не корректная", user.getEmail());
            throw new ValidationException("электронная почта " + user.getEmail() +
                    " не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || "".equals(user.getLogin()) || user.getLogin().contains(" ")) {
            log.info("Логин \"{}\" пустой или содержит пробелы", user.getLogin());
            throw new ValidationException("Логин " + user.getLogin() + " не может быть пустым и содержать пробелы");
        }


        if (birthdayDateFormat.isAfter(dateNowFormat)) {
            log.info("Дата рождения {} не может быть в будущем", user.getBirthday());
            throw new ValidationException("Дата рождения " + user.getBirthday() + " не может быть в будущем");
        }
    }
}
