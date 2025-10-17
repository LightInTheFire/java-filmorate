package ru.yandex.practicum.filmorate.repository.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcFriendshipsRepository implements FriendshipsRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addFriendship(long userId, long friendId) {
        MapSqlParameterSource params = getParameterMap(userId, friendId);
        String addFriendshipSql = """
                INSERT INTO friendships (user_id1, user_id2)
                VALUES (:user_id1, :user_id2), (:user_id2, :user_id1)""";
        jdbc.update(addFriendshipSql, params);
    }

    @Override
    public void removeFriendship(long userId, long friendId) {
        MapSqlParameterSource params = getParameterMap(userId, friendId);
        String deleteFriendshipSql = """
                DELETE FROM friendships
                WHERE (user_id1 = :user_id1 AND user_id2 = :user_id2)
                   OR (user_id1 = :user_id2 AND user_id2 = :user_id1)""";
        jdbc.update(deleteFriendshipSql, params);
    }

    @Override
    public boolean isFriends(long userId, long friendId) {
        String checkFriendshipSql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM friendships
                    WHERE (user_id1 = :user_id AND user_id2 = :friend_id)
                       OR (user_id1 = :friend_id AND user_id2 = :user_id))""";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);

        return Boolean.TRUE.equals(jdbc.queryForObject(checkFriendshipSql, params, Boolean.class));
    }

    @Override
    public List<Long> findFriendIdsByUserId(long userId) {
        String findFriendsSql = """
                SELECT user_id2
                FROM friendships
                WHERE user_id1 = :user_id""";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId);

        return jdbc.queryForList(findFriendsSql, params, Long.class);
    }

    private MapSqlParameterSource getParameterMap(long userId, long friendId) {
        return new MapSqlParameterSource()
                .addValue("user_id1", userId)
                .addValue("user_id2", friendId);
    }
}