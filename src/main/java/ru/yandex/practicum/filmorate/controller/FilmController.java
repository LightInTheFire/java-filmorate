package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> findAll() {
        log.trace("Collection of all films requested");
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public FilmDto findById(@PathVariable @Positive long filmId) {
        log.trace("Find film by id requested, id: {}", filmId);
        return filmService.findById(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteById(@PathVariable @Positive long filmId) {
        log.trace("Delete film by id requested, id: {}", filmId);
        filmService.deleteById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public FilmDto create(@RequestBody @Valid NewFilmRequest request) {
        log.trace("Create new film requested {}", request);
        return filmService.create(request);
    }

    @PutMapping
    @Validated
    public FilmDto update(@RequestBody @Valid UpdateFilmRequest request) {
        log.trace("Update film requested {}", request);
        return filmService.update(request);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id,
                        @PathVariable long userId) {
        log.trace("Add like requested film id: {}, user id: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id,
                           @PathVariable long userId) {
        log.trace("Delete like requested film id: {}, user id: {}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/common")
    public Collection<FilmDto> findCommonFilms(@RequestParam(name = "userId") @Positive long userId,
                                               @RequestParam(name = "friendId") @Positive long friendId) {
        log.trace("Find common films requested for user {} and friend {}", userId, friendId);
        return filmService.findCommonFilms(userId, friendId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> findPopular(@RequestParam(defaultValue = "10") @Positive int count,
                                           @RequestParam(required = false) @Positive Integer genreId,
                                           @RequestParam(required = false) @Positive Integer year) {
        log.trace("Find popular film requested with count: {} by genre: {} for year: {}", count, genreId, year);
        return filmService.findFilmsWithTopLikes(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public Collection<FilmDto> findFilmsOfDirector(@PathVariable @Positive long directorId,
                                                   @RequestParam String sortBy) {

        FilmsSortBy sortFilmsBy = FilmsSortBy.fromString(sortBy);
        if (sortFilmsBy == null) {
            throw new IllegalArgumentException("invalid sort by: %s".formatted(sortBy));
        }

        log.trace("Find films of director with id {} requested", directorId);
        return filmService.findFilmsOfDirector(directorId, sortFilmsBy);
    }
}
