package ru.yandex.practicum.catsgram.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.catsgram.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Test
    void testUserEmailCannotBeEmptyOrInvalid() {
        User user = new User();
        user.setEmail(""); // Пустая электронная почта

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validateUser(user);
        });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", exception.getMessage());

        user.setEmail("invalidemail"); // Неверный формат электронной почты

        exception = assertThrows(ValidationException.class, () -> {
            validateUser(user);
        });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", exception.getMessage());
    }

    @Test
    void testUserLoginCannotBeEmptyOrContainSpaces() {
        User user = new User();
        user.setLogin(""); // Пустой логин

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validateUser(user);
        });
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());

        user.setLogin("invalid login"); // Логин с пробелом

        exception = assertThrows(ValidationException.class, () -> {
            validateUser(user);
        });
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void testUserBirthdayNotInFuture() {
        User user = new User();
        user.setBirthday(LocalDate.now().plusDays(1)); // Дата рождения в будущем

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validateUser(user);
        });
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    // Метод валидации, который нужно протестировать
    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
