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

import java.util.Collection;
import java.util.List;

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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(friendId)));

        if (userId == friendId) {
            throw new IllegalArgumentException("User cannot add themselves as a friend");
        }

        try {
            friendshipsRepository.addFriendship(userId, friendId);
            log.info("Friendship successfully added between user {} and user {}", userId, friendId);

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

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(friendId)));

        if (!friendshipsRepository.isFriends(userId, friendId)) {
            log.warn("Friendship between user {} and user {} does not exist", userId, friendId);
            throw new NotFoundException("Friendship between user " + userId + " and user " + friendId + " not found");
        }

        friendshipsRepository.removeFriendship(userId, friendId);
        log.info("Friendship successfully removed between user {} and user {}", userId, friendId);

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

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));

        Collection<User> friends = userRepository.findAllFriends(userId);
        log.debug("Retrieved {} friends for user {}", friends.size(), userId);

        return List.copyOf(friends);
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        log.debug("Getting common friends between user {} and user {}", userId, otherUserId);

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        userRepository.findById(otherUserId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(otherUserId)));

        Collection<User> commonFriends = userRepository.findAllCommonFriends(userId, otherUserId);
        log.debug("Retrieved {} common friends between user {} and user {}",
                 commonFriends.size(), userId, otherUserId);

        return List.copyOf(commonFriends);
    }

    public boolean isFriends(long userId, long friendId) {
        log.trace("Checking if user {} and user {} are friends", userId, friendId);

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(friendId)));

        boolean areFriends = friendshipsRepository.isFriends(userId, friendId);
        log.trace("Friendship check result: user {} and user {} are friends: {}", userId, friendId, areFriends);

        return areFriends;
    }
}
