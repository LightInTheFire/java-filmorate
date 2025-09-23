package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDBRepository")
    private final FilmStorage filmStorage;
    @Qualifier("userDBRepository")
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    public Collection<FilmDto> findAll() {
        return filmStorage.findAll()
                .stream()
                .map(FilmMapper::toFilmDto)
                .toList();
    }

    public FilmDto findById(long id) {
        return filmStorage.findById(id)
                .map(FilmMapper::toFilmDto)
                .orElseThrow(NotFoundException.supplier("Film with id %d not found", id));
    }

    public FilmDto create(NewFilmRequest request) {
        Film film = FilmMapper.toFilm(request);
        film = filmStorage.create(film);
        return FilmMapper.toFilmDto(film);
    }

    public FilmDto update(UpdateFilmRequest request) {
        Film film = filmStorage.findById(request.getId()).orElseThrow(
                NotFoundException.supplier("Film with id %d not found", request.getId())
        );
        film = FilmMapper.updateFilmFields(film, request);
        film = filmStorage.update(film);
        return FilmMapper.toFilmDto(film);
    }

    public boolean deleteById(long id) {
        return filmStorage.deleteById(id);
    }

    public void addLike(long filmId, long userId) {
        userStorage.throwIfNotExists(userId);
        filmStorage.throwIfNotExists(filmId);
        likesStorage.addLike(userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        userStorage.throwIfNotExists(userId);
        filmStorage.throwIfNotExists(filmId);
        likesStorage.removeLike(userId, filmId);
    }

    public Collection<FilmDto> findFilmsWithTopLikes(int count) {
        return filmStorage.getTopPopularFilms(count)
                .stream()
                .map(FilmMapper::toFilmDto)
                .toList();
    }
}
