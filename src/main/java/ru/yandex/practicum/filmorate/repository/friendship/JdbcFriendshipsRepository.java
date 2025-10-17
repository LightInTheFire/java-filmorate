package ru.yandex.practicum.filmorate.repository.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcFriendshipsRepository implements FriendshipsRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addFriendship(long userId, long friendId) {
        MapSqlParameterSource params = getParameterMap(userId, friendId);
        String addFriendshipSql = """
                INSERT INTO friendships (user_id1, user_id2)
                VALUES (:user_id1, :user_id2)""";
        jdbc.update(addFriendshipSql, params);
    }

    @Override
    public void removeFriendship(long userId, long friendId) {
        MapSqlParameterSource params = getParameterMap(userId, friendId);
        String deleteFriendshipSql = """
                DELETE FROM friendships
                WHERE user_id1 = :user_id1 AND user_id2 = :user_id2""";
        jdbc.update(deleteFriendshipSql, params);
    }

    @Override
    public boolean isFriends(long userId, long friendId) {
        String checkFriendshipSql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM friendships
                    WHERE user_id1 = :user_id AND user_id2 = :friend_id)""";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);

        return Boolean.TRUE.equals(jdbc.queryForObject(checkFriendshipSql, params, Boolean.class));
    }

    private MapSqlParameterSource getParameterMap(long userId, long friendId) {
        return new MapSqlParameterSource()
                .addValue("user_id1", userId)
                .addValue("user_id2", friendId);
    }
}