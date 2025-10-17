package ru.yandex.practicum.filmorate.repository.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JdbcFriendshipsRepository implements FriendshipsRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addFriendship(long userId, long friendId) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("user_id1", userId)
                    .addValue("user_id2", friendId);
            
            String sql = "INSERT INTO friendships (user_id1, user_id2) VALUES (:user_id1, :user_id2)";
            jdbc.update(sql, params);
            log.debug("Friendship added: user {} -> user {}", userId, friendId);
            
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to add friendship between user {} and user {}", userId, friendId, e);
            throw new RuntimeException("Failed to add friendship", e);
        }
    }

    @Override
    public void removeFriendship(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id1", userId)
                .addValue("user_id2", friendId);
        
        String sql = "DELETE FROM friendships WHERE user_id1 = :user_id1 AND user_id2 = :user_id2";
        int rows = jdbc.update(sql, params);
        log.debug("Friendship removed: user {} -> user {}, affected rows: {}", userId, friendId, rows);
    }

    @Override
    public boolean isFriends(long userId, long friendId) {
        String sql = """
            SELECT COUNT(*) FROM friendships 
            WHERE user_id1 = :user_id1 AND user_id2 = :user_id2
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id1", userId)
                .addValue("user_id2", friendId);
        
        Integer count = jdbc.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
