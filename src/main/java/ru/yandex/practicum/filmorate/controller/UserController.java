package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Collection of all users requested");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable long userId) {
        log.trace("Find user by id requested, id: {}", userId);
        Optional<User> userById = userService.findById(userId);
        return userById.orElseThrow(
                () -> new NotFoundException("User with id %d not found".formatted(userId))
        );
    }

    @DeleteMapping("/{userId}")
    public User deleteById(@PathVariable long userId) {
        log.trace("Delete user by id requested, id: {}", userId);
        return userService.deleteById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.trace("create new user requested {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.trace("Update user requested {}", user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.trace("Add friend requested for id {}, friend id: {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.trace("Delete friend requested for id {}, friend id: {}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable long id) {
        log.trace("Find friends requested for id {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.trace("Find common friends requested for id {}, other id: {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
