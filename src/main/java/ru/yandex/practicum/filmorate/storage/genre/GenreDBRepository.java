package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDBRepository extends BaseStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY genre_id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String CHECK_EXISTS_QUERY = """
            SELECT 0 < COUNT(*)
            FROM genres
            WHERE genre_id = ?""";
    private static final String FIND_ALL_GENRES_QUERY = """
            SELECT g.genre_id AS genre_id,
                   g.name as name
            FROM genres g
            JOIN film_genres fg ON g.genre_id = fg.genre_id
            WHERE fg.film_id = ?""";


    public GenreDBRepository(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Genre> getAllGenresOfFilm(long filmId) {
       return findManyQuery(FIND_ALL_GENRES_QUERY,filmId);
    }

    @Override
    public Collection<Genre> findAll() {
        return findManyQuery(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> findById(long id) {
        return findOneQuery(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Genre create(Genre genre) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Genre update(Genre newT) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void throwIfNotExists(long id) {
        if (checkExists(CHECK_EXISTS_QUERY, id)) {
            return;
        }

        throw NotFoundException.supplier("Genre with id %d not found", id).get();
    }
}
