package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

@Repository
public class LikesDBRepository extends BaseStorage<Long> implements LikesStorage {
    private static final RowMapper<Long> LIKE_ID_MAPPER
            = (((rs, rowNum) -> rs.getLong("user_id")));
    private static final String INSERT_LIKES_QUERY = """
            INSERT INTO likes (user_id, film_id)
            VALUES (?, ?)""";
    private static final String DELETE_LIKES_QUERY = """
            DELETE FROM likes
            WHERE user_id = ?
                  AND film_id = ?""";

    public LikesDBRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, LIKE_ID_MAPPER);
    }

    @Override
    public void addLike(long userId, long filmId) {
        insertSimpleQuery(INSERT_LIKES_QUERY, userId, filmId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        deleteQuery(DELETE_LIKES_QUERY, userId, filmId);
    }
}
