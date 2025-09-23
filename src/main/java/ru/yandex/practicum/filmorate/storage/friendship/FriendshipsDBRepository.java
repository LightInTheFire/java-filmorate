package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Repository
public class FriendshipsDBRepository extends BaseStorage<Long> implements FriendshipsStorage {
    private static final RowMapper<Long> FRIEND_ID_MAPPER
            = (((rs, rowNum) -> rs.getLong("user_id2")));
    private static final String INSERT_FRIENDSHIP_QUERY = """
            INSERT INTO friendships (user_id1, user_id2)
            VALUES (?, ?)""";
    private static final String DELETE_FRIENDSHIP_QUERY = """
            DELETE FROM friendships
            WHERE user_id1 = ?
                  AND user_id2 = ?""";

    public FriendshipsDBRepository(JdbcTemplate jdbcTemplate,
                                   @Qualifier("userDBRepository") UserStorage userStorage) {
        super(jdbcTemplate, FRIEND_ID_MAPPER);
    }

    @Override
    public void addFriendship(long userId, long friendId) {
        insertSimpleQuery(INSERT_FRIENDSHIP_QUERY, userId, friendId);
    }

    @Override
    public void removeFriendship(long userId, long friendId) {
        deleteQuery(DELETE_FRIENDSHIP_QUERY, userId, friendId);
    }
}
