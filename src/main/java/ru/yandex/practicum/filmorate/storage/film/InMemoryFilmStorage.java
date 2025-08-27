package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        log.trace("findAll() call");
        return films.values();
    }

    @Override
    public Optional<Film> findById(long id) {
        log.trace("findById call with id: {}", id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.trace("Film created: {}", film);
        return film;

    }

    @Override
    public Film update(Film newFilm) {
        log.trace("update() call with id: {}", newFilm.getId());
        return films.put(newFilm.getId(), newFilm);
    }

    @Override
    public Film deleteById(long id) {
        log.trace("deleteById call with id: {}", id);
        return films.remove(id);
    }

    @Override
    public boolean existsById(long id) {
        return films.containsKey(id);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
