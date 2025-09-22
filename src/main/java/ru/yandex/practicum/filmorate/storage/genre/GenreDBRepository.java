package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDBRepository extends BaseStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY genre_id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";

    public GenreDBRepository(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, rowMapper);
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
    public Genre deleteById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean existsById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
