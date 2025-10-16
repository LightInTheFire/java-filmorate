package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.controller.FilmsSortBy;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Optional<Film> findById(long id);

    Collection<Film> findAll();

    Film save(Film film);

    void update(Film film);

    void deleteById(long id);

    Collection<Film> findTopPopularFilms(int count, Integer genreId, Integer year);

    Collection<Film> findCommonFilms(long userId, long friendId);

    Collection<Film> findFilmsOfDirector(long directorId, FilmsSortBy sortFilmsBy);
}
