package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Optional<Film> findById(Long id) {
        return filmStorage.findById(id);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        Optional<Film> filmOptional = filmStorage.findById(newFilm.getId());

        if (filmOptional.isEmpty()) {
            log.warn("Film with id {} not found", newFilm.getId());
            throw new NotFoundException("Film with id %d not found".formatted(newFilm.getId()));
        }
        Film film = filmOptional.get();

        if (newFilm.getName() != null) {
            @NotBlank(message = "Film name should not be null or empty") String name = newFilm.getName();
            film.setName(name);
        }

        if (newFilm.getDescription() != null) {
            @Size(message = "Description length must be less than 200", max = 200)
            @NotBlank(message = "Description should not be null or empty")
            String description = newFilm.getDescription();
            film.setDescription(description);
        }

        if (newFilm.getReleaseDate() != null) {
            @AssertTrue(message = "Release date must be after December 28, 1895")
            boolean filmReleaseDateAfter1895 = newFilm.isFilmReleaseDateAfter1895();
            film.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getDuration() != null) {
            @AssertTrue(message = "Film duration must be positive")
            boolean durationPositive = newFilm.isDurationPositive();
            film.setDuration(newFilm.getDuration());
        }

        return filmStorage.update(film);
    }

    public Film deleteById(long id) {
        return filmStorage.deleteById(id);
    }

    public void addLike(long filmId, long userId) {
        Film film = filmStorage.findById(filmId).orElseThrow(
                () -> new NotFoundException("Film with id %d not found".formatted(filmId))
        );

        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("User with id %d not found".formatted(userId));
        }

        film.addUserLike(userId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.findById(filmId).orElseThrow(
                () -> new NotFoundException("Film with id %d not found".formatted(filmId))
        );

        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("User with id %d not found".formatted(userId));
        }

        film.removeUserLike(userId);
    }

    public Collection<Film> findFilmsWithTopLikes(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .toList();
    }
}
