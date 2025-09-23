package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;

public interface GenreStorage extends Storage<Genre> {
    Collection<Genre> getAllGenresOfFilm(long filmId);
}
