package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.storage.filmgenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mparating.MPARatingStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class FilmDBRepository extends BaseStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films ORDER BY film_id ASC";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String CHECK_EXISTS_QUERY = """
            SELECT 0 < COUNT(*)
            FROM films
            WHERE film_id = ?""";
    private static final String FIND_TOP_POPULAR = """
            SELECT
                f.film_id,
                f.name,
                f.description,
                f.release_date,
                f.duration_in_minutes,
                f.mpa_id
            FROM films f
                     LEFT JOIN likes fl ON f.film_id = fl.film_id
            GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration_in_minutes
            ORDER BY COUNT(fl.user_id) DESC, f.film_id
            LIMIT ?""";
    private static final String UPDATE_FILM_QUERY = """
            UPDATE films
            SET name = ?, description = ?, release_date = ?, duration_in_minutes = ? , mpa_id = ?
            WHERE film_id = ?""";
    private static final String INSERT_FILM_QUERY = """
            INSERT INTO films(name, description, release_date, duration_in_minutes, mpa_id)
            VALUES(?, ?, ?, ?, ?)""";
    private final FilmGenreStorage filmGenreStorage;
    private final MPARatingStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmDBRepository(JdbcTemplate jdbcTemplate, RowMapper<Film> rowMapper, FilmGenreStorage filmGenreStorage, MPARatingStorage mpaStorage, GenreStorage genreStorage) {
        super(jdbcTemplate, rowMapper);
        this.filmGenreStorage = filmGenreStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Collection<Film> findAll() {
        return findManyQuery(FIND_ALL_QUERY).stream()
                .peek(film
                        -> film.addAllGenresIds(filmGenreStorage.getGenreIdsForFilm(film.getId())))
                .toList();
    }

    @Override
    public Optional<Film> findById(long id) {
        Optional<Film> filmOptional = findOneQuery(FIND_BY_ID_QUERY, id);
        filmOptional.ifPresent(film
                -> {
            film.addAllGenres(genreStorage.getAllGenresOfFilm(film.getId()));
            film.setMpaRating(mpaStorage.findById(film.getMpaRating().id())
                    .orElseThrow(
                            NotFoundException.supplier(
                                    "MPA rating with if %d for film %d not found",
                                    film.getMpaRating().id(),
                                    film.getId())));
        });
        return filmOptional;
    }

    @Override
    public Film create(Film film) {
        mpaStorage.throwIfNotExists(film.getMpaRating().id());

        long filmId = insertQuery(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().id()
        );

        film.setId(filmId);
        for (Genre genre : film.getGenres()) {
            genreStorage.throwIfNotExists(genre.id());
        }

        filmGenreStorage.insertGenreIdsForFilm(filmId, film.getGenres()
                .stream()
                .map(Genre::id)
                .toList());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        updateQuery(
                UPDATE_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpaRating().id(),
                newFilm.getId()
        );

        return newFilm;
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }

    @Override
    public void throwIfNotExists(long id) {
        if (checkExists(CHECK_EXISTS_QUERY, id)) {
            return;
        }

        throw NotFoundException.supplier("Film with id %d not found", id).get();
    }

    @Override
    public Collection<Film> getTopPopularFilms(int count) {
        return findManyQuery(FIND_TOP_POPULAR, count);
    }
}
