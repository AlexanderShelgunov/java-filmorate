package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void init() {
        userController = new UserController();
        user = new User();

        user.setName("User");
        user.setBirthday(LocalDate.now());
        user.setEmail("mail@mail.ru");
        user.setLogin("Login");
    }

    @Test
    void validateEmail() {

        assertDoesNotThrow(() -> userController.checkValidation(user));

        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.checkValidation(user));

        user.setEmail("mail.ru");
        assertThrows(ValidationException.class, () -> userController.checkValidation(user));

        user.setEmail(null);
        assertThrows(ValidationException.class, () -> userController.checkValidation(user));

    }

    @Test
    void validateLogin() {

        assertDoesNotThrow(() -> userController.checkValidation(user));

        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.checkValidation(user));

        user.setLogin("login login");
        assertThrows(ValidationException.class, () -> userController.checkValidation(user));

        user.setLogin(null);
        assertThrows(ValidationException.class, () -> userController.checkValidation(user));

    }

    @Test
    void validateBirthday() {

        LocalDate date = LocalDate.now().minusDays(1);
        user.setBirthday(date);
        assertDoesNotThrow(() -> userController.checkValidation(user));

        date = LocalDate.now();
        user.setBirthday(date);
        assertDoesNotThrow(() -> userController.checkValidation(user));

        date = LocalDate.now().plusDays(1);
        user.setBirthday(date);
        assertThrows(ValidationException.class, () -> userController.checkValidation(user));

    }
}