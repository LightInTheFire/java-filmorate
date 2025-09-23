package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserDBRepository extends BaseStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users ORDER BY user_id ASC";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String CHECK_EXISTS_QUERY = """
            SELECT 0 < COUNT(*)
            FROM users
            WHERE user_id = ?""";
    private static final String UPDATE_USER_QUERY = """
            UPDATE users
            SET email = ?, login = ?, name = ?, birthday = ?
            WHERE user_id = ?""";
    private static final String INSERT_USER_QUERY = """
            INSERT INTO users(email, login, name, birthday)
            VALUES (?, ?, ?, ?)""";
    private static final String FIND_ALL_FRIENDS_OF_USER_QUERY = """
            SELECT u.user_id,
                   u.email,
                   u.login,
                   u.name,
                   u.birthday
            FROM users u
            WHERE u.user_id IN (SELECT f.user_id2
                                FROM friendships f
                                WHERE f.user_id1 = ?)
            ORDER BY u.name""";
    private static final String FIND_ALL_COMMON_FRIENDS_QUERY = """
            SELECT u.user_id,
                   u.email,
                   u.login,
                   u.name,
                   u.birthday
            FROM users u
            WHERE u.user_id IN (SELECT user_id2
                                FROM friendships
                                WHERE user_id1 = ?
                                INTERSECT
                                SELECT user_id2
                                FROM friendships
                                WHERE user_id1 = ?)""";

    public UserDBRepository(JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<User> findAll() {
        return findManyQuery(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> findById(long id) {
        return findOneQuery(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User create(User user) {
        long userId = insertQuery(
                INSERT_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );

        user.setId(userId);
        return user;
    }

    @Override
    public User update(User newUser) {
        updateQuery(
                UPDATE_USER_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );

        return newUser;
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

        throw NotFoundException.supplier("User with id %d not found", id).get();
    }

    @Override
    public Collection<User> getFriendsOfUser(long userId) {
        return findManyQuery(FIND_ALL_FRIENDS_OF_USER_QUERY, userId);
    }

    @Override
    public Collection<User> getCommonFriendsOfTwoUsers(long userId1, long userId2) {
        return findManyQuery(FIND_ALL_COMMON_FRIENDS_QUERY, userId1, userId2);
    }

}
