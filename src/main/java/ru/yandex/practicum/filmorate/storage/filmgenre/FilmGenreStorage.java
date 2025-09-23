package ru.yandex.practicum.filmorate.storage.filmgenre;

import java.util.Collection;

public interface FilmGenreStorage {
    Collection<Long> getGenreIdsForFilm(long filmId);

    Collection<Long> insertGenreIdsForFilm(long filmId, Collection<Long> genreIds);
}
