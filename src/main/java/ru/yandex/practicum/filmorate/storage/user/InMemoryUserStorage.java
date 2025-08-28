package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.trace("findAll() call");
        return users.values();
    }

    @Override
    public Optional<User> findById(long id) {
        log.trace("findById call with id: {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.trace("User created: {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.trace("update() call with id: {}", newUser.getId());
        return users.put(newUser.getId(), newUser);
    }

    @Override
    public User deleteById(long id) {
        log.trace("deleteById() call with id: {}", id);
        return users.remove(id);
    }

    @Override
    public boolean existsById(long id) {
        return users.containsKey(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
