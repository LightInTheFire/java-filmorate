package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("PUT /users/{}/friends/{} - Adding friend", userId, friendId);
        friendshipService.addFriendship(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("DELETE /users/{}/friends/{} - Removing friend", userId, friendId);
        friendshipService.removeFriendship(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable long userId) {
        log.info("GET /users/{}/friends - Getting user friends", userId);
        return friendshipService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        log.info("GET /users/{}/friends/common/{} - Getting common friends", userId, otherUserId);
        return friendshipService.getCommonFriends(userId, otherUserId);
    }
}
