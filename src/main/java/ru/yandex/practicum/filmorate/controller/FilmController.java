package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

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
}
