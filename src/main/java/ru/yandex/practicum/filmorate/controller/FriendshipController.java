package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Adding friend {} to user {}", friendId, userId);
        friendshipService.addFriendship(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Removing friend {} from user {}", friendId, userId);
        friendshipService.removeFriendship(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable long userId) {
        log.info("Getting friends for user {}", userId);
        return friendshipService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        log.info("Getting common friends between user {} and user {}", userId, otherUserId);
        return friendshipService.getCommonFriends(userId, otherUserId);
    }
}