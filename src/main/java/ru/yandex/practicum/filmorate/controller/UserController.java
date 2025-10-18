package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.recommendation.RecommendationService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;


@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final RecommendationService recommendationService;

    @GetMapping
    public Collection<UserDto> findAll() {
        log.trace("Collection of all users requested");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable @Positive long userId) {
        log.trace("Find user by id requested, id: {}", userId);
        return userService.findById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable @Positive long userId) {
        log.trace("Delete user by id requested, id: {}", userId);
        userService.deleteById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public UserDto create(@RequestBody @Valid NewUserRequest user) {
        log.trace("create new user requested {}", user);
        return userService.create(user);
    }

    @PutMapping
    @Validated
    public UserDto update(@RequestBody @Valid UpdateUserRequest user) {
        log.trace("Update user requested {}", user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id,
                          @PathVariable long friendId) {
        log.trace("Add friend requested for id {}, friend id: {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Positive long id,
                             @PathVariable @Positive long friendId) {
        log.trace("Delete friend requested for id {}, friend id: {}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> findFriends(@PathVariable @Positive long id) {
        log.trace("Find friends requested for id {}", id);
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> findCommonFriends(@PathVariable @Positive long id,
                                                 @PathVariable @Positive long otherId) {
        log.trace("Find common friends requested for id {}, other id: {}", id, otherId);
        return userService.findAllCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<FilmDto> findFilmRecommendations(@PathVariable @Positive long id) {
        log.trace("Find film recommendations requested for id {}", id);
        return recommendationService.findFilmRecommendations(id);
    }
}
