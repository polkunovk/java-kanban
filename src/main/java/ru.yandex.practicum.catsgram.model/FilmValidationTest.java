package ru.yandex.practicum.catsgram.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.catsgram.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {

    @Test
    void testFilmNameCannotBeEmpty() {
        Film film = new Film();
        film.setName(""); // Пустое название

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validateFilm(film);
        });
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    void testFilmDescriptionMaxLength() {
        Film film = new Film();
        film.setName("Valid Film Name");
        film.setDescription("a".repeat(201)); // Длина описания больше 200 символов

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validateFilm(film);
        });
        assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    void testFilmReleaseDateNotBefore1895() {
        Film film = new Film();
        film.setName("Valid Film Name");
        film.setReleaseDate(LocalDate.of(1894, 12, 27)); // Дата релиза до 28 декабря 1895 года

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validateFilm(film);
        });
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    void testFilmDurationMustBePositive() {
        Film film = new Film();
        film.setName("Valid Film Name");
        film.setDuration(-1); // Неположительная продолжительность

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validateFilm(film);
        });
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    // Метод валидации, который нужно протестировать
    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
