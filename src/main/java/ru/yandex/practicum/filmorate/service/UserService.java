package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public Optional<User> findById(Long id) {
        return userStorage.findById(id);
    }

    public User create(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User newUser) {
        Optional<User> userOptional = userStorage.findById(newUser.getId());

        User user = userOptional.orElseThrow(
                () -> new NotFoundException("User with id %d not found".formatted(newUser.getId()))
        );

        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }

        if (newUser.getLogin() != null) {
            user.setLogin(newUser.getLogin());
        }

        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }

        if (newUser.getBirthday() != null) {
            user.setBirthday(newUser.getBirthday());
        }

        return userStorage.update(user);
    }

    public User deleteById(long id) {
        return userStorage.deleteById(id);
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException("User with id %d not found".formatted(userId))
                );
        User userFriend = userStorage.findById(friendId)
                .orElseThrow(
                        () -> new NotFoundException("User with id %d not found".formatted(friendId))
                );
        user.addFriend(friendId);
        userFriend.addFriend(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException("User with id %d not found".formatted(userId))
                );
        User userFriend = userStorage.findById(friendId)
                .orElseThrow(
                        () -> new NotFoundException("User with id %d not found".formatted(friendId))
                );
        user.removeFriend(friendId);
        userFriend.removeFriend(userId);
    }

    public Collection<User> getFriends(long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException("User with id %d not found".formatted(userId))
                );
        Set<Long> friendIds = user.getFriends();
        return getUsersFromIds(friendIds);
    }

    public Collection<User> getCommonFriends(long firstUserId, long secondUserId) {
        User firstUser = userStorage.findById(firstUserId)
                .orElseThrow(
                        () -> new NotFoundException("User with id %d not found".formatted(firstUserId))
                );
        Collection<User> firstUserFriends = getUsersFromIds(firstUser.getFriends());

        User secondUser = userStorage.findById(secondUserId)
                .orElseThrow(
                        () -> new NotFoundException("User with id %d not found".formatted(secondUserId))
                );
        Collection<User> secondUserFriends = getUsersFromIds(secondUser.getFriends());

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .toList();
    }

    private Collection<User> getUsersFromIds(Collection<Long> ids) {
        return ids.stream()
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
