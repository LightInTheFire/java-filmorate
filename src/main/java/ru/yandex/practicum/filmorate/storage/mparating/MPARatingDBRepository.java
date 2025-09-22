package ru.yandex.practicum.filmorate.storage.mparating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MPARatingDBRepository extends BaseStorage<MPARating> implements MPARatingStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_ratings ORDER BY mpa_id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";

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
    public MPARating deleteById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean existsById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
