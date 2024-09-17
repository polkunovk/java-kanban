package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ValidationException;
import ru.yandex.practicum.catsgram.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        validateFilm(film);
        films.add(film);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @RequestBody Film updatedFilm) {
        validateFilm(updatedFilm);
        for (Film film : films) {
            if (film.getId() == id) {
                film.setName(updatedFilm.getName());
                film.setDescription(updatedFilm.getDescription());
                film.setReleaseDate(updatedFilm.getReleaseDate());
                film.setDuration(updatedFilm.getDuration());
                return new ResponseEntity<>(film, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

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
