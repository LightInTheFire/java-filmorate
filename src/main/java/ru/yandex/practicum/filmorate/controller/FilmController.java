package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        log.trace("Collection of all films requested");
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable long filmId) {
        log.trace("Find film by id requested, id: {}", filmId);
        Optional<Film> filmById = filmService.findById(filmId);
        return filmById.orElseThrow(
                () -> new NotFoundException("User with id %d not found".formatted(filmId))
        );
    }

    @DeleteMapping("/{filmId}")
    public Film deleteById(@PathVariable long filmId) {
        log.trace("Delete film by id requested, id: {}", filmId);
        return filmService.deleteById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.trace("Create new film requested {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.trace("Update film requested {}", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.trace("Add like requested film id: {}, user id: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.trace("Delete like requested film id: {}, user id: {}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> findPopular(@RequestParam(defaultValue = "10")
                                        @Positive
                                        @Valid int count) {
        log.trace("Find popular film requested with count: {}", count);
        return filmService.findFilmsWithTopLikes(count);
    }
}
