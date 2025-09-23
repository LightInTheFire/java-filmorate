package ru.yandex.practicum.filmorate.storage.mparating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MPARatingDBRepository extends BaseStorage<MPARating> implements MPARatingStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_ratings ORDER BY mpa_id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";
    private static final String CHECK_EXISTS_QUERY = """
            SELECT 0 < COUNT(*)
            FROM mpa_ratings
            WHERE mpa_id = ?""";

    public MPARatingDBRepository(JdbcTemplate jdbcTemplate, RowMapper<MPARating> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<MPARating> findAll() {
        return findManyQuery(FIND_ALL_QUERY);
    }

    @Override
    public Optional<MPARating> findById(long id) {
        return findOneQuery(FIND_BY_ID_QUERY, id);
    }

    @Override
    public MPARating create(MPARating mpaRating) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MPARating update(MPARating newT) {
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

        throw NotFoundException.supplier("MPA rating with id %d not found", id).get();
    }

    @Override
    public boolean existsById(long id) {
        return findById(id).isPresent();
    }
}
