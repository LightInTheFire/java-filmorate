package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.friendship.FriendshipsRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipsRepository friendshipsRepository;
    private final UserRepository userRepository;
    private final EventService eventService;

    @Transactional
    public void addFriendship(long userId, long friendId) {
        log.debug("Adding friendship between user {} and user {}", userId, friendId);

        // Проверяем существование пользователей
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(friendId)));

        // Проверяем, не пытается ли пользователь добавить себя в друзья
        if (userId == friendId) {
            throw new IllegalArgumentException("User cannot add themselves as a friend");
        }

        try {
            // Добавляем дружбу (двустороннюю)
            friendshipsRepository.addFriendship(userId, friendId);
            log.info("Friendship successfully added between user {} and user {}", userId, friendId);

            // Создаем событие для ленты
            Event event = Event.builder()
                    .userId(userId)
                    .eventType(EventType.FRIEND)
                    .operation(Operation.ADD)
                    .entityId(friendId)
                    .timestamp(System.currentTimeMillis())
                    .build();
            eventService.addEvent(event);
            log.debug("Event created for friendship addition: user {} -> user {}", userId, friendId);

        } catch (DuplicateKeyException e) {
            log.warn("Friendship between user {} and user {} already exists", userId, friendId);
            throw new IllegalArgumentException("Friendship already exists between user " + userId + " and user " + friendId);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while adding friendship between user {} and user {}", userId, friendId, e);
            throw new IllegalStateException("Failed to add friendship due to data integrity violation");
        }
    }

    @Transactional
    public void removeFriendship(long userId, long friendId) {
        log.debug("Removing friendship between user {} and user {}", userId, friendId);

        // Проверяем существование пользователей
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(friendId)));

        // Проверяем существование дружбы перед удалением
        if (!friendshipsRepository.isFriends(userId, friendId)) {
            log.warn("Friendship between user {} and user {} does not exist", userId, friendId);
            throw new NotFoundException("Friendship between user " + userId + " and user " + friendId + " not found");
        }

        // Удаляем дружбу (двустороннюю)
        friendshipsRepository.removeFriendship(userId, friendId);
        log.info("Friendship successfully removed between user {} and user {}", userId, friendId);

        // Создаем событие для ленты
        Event event = Event.builder()
                .userId(userId)
                .eventType(EventType.FRIEND)
                .operation(Operation.REMOVE)
                .entityId(friendId)
                .timestamp(System.currentTimeMillis())
                .build();
        eventService.addEvent(event);
        log.debug("Event created for friendship removal: user {} -> user {}", userId, friendId);
    }

    public List<User> getFriends(long userId) {
        log.debug("Getting friends for user {}", userId);

        // Проверяем существование пользователя
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));

        // Получаем ID друзей
        List<Long> friendIds = friendshipsRepository.findFriendIdsByUserId(userId);
        log.trace("Found {} friends for user {}", friendIds.size(), userId);

        // Получаем полные объекты пользователей
        List<User> friends = userRepository.findAllByIds(friendIds);
        log.debug("Retrieved {} friend objects for user {}", friends.size(), userId);

        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        log.debug("Getting common friends between user {} and user {}", userId, otherUserId);

        // Проверяем существование пользователей
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        userRepository.findById(otherUserId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(otherUserId)));

        // Получаем друзей первого пользователя
        List<Long> userFriends = friendshipsRepository.findFriendIdsByUserId(userId);
        // Получаем друзей второго пользователя
        List<Long> otherUserFriends = friendshipsRepository.findFriendIdsByUserId(otherUserId);

        // Находим пересечение (общих друзей)
        List<Long> commonFriendIds = userFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toList());

        log.trace("Found {} common friends between user {} and user {}", commonFriendIds.size(), userId, otherUserId);

        // Получаем полные объекты общих друзей
        List<User> commonFriends = userRepository.findAllByIds(commonFriendIds);
        log.debug("Retrieved {} common friend objects", commonFriends.size());

        return commonFriends;
    }

    public boolean isFriends(long userId, long friendId) {
        log.trace("Checking if user {} and user {} are friends", userId, friendId);

        // Проверяем существование пользователей
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(friendId)));

        boolean areFriends = friendshipsRepository.isFriends(userId, friendId);
        log.trace("Friendship check result: user {} and user {} are friends: {}", userId, friendId, areFriends);

        return areFriends;
    }
}