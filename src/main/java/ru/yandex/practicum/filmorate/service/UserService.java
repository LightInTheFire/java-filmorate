package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDBRepository")
    private final UserStorage userStorage;
    private final FriendshipsStorage friendshipsStorage;

    public Collection<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto findById(Long id) {
        return userStorage.findById(id)
                .map(UserMapper::toUserDto)
                .orElseThrow(NotFoundException.supplier("User with id %d not found", id));
    }

    public UserDto create(NewUserRequest request) {
        if (request.getName() == null) {
            request.setName(request.getLogin());
        }
        User user = UserMapper.mapToUser(request);
        user = userStorage.create(user);
        return UserMapper.toUserDto(user);
    }

    public UserDto update(UpdateUserRequest request) {
        User user = userStorage.findById(request.getId()).orElseThrow(
                NotFoundException.supplier("User with id %d not found", request.getId())
        );
        user = UserMapper.updateUserFields(user, request);
        user = userStorage.update(user);
        return UserMapper.toUserDto(user);
    }

    public boolean deleteById(long id) {
        return userStorage.deleteById(id);
    }

    public void addFriend(long userId, long friendId) {
        userStorage.throwIfNotExists(userId);
        userStorage.throwIfNotExists(friendId);
        friendshipsStorage.addFriendship(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userStorage.throwIfNotExists(userId);
        userStorage.throwIfNotExists(friendId);
        friendshipsStorage.removeFriendship(userId, friendId);
    }

    public Collection<UserDto> findFriends(long userId) {
        userStorage.throwIfNotExists(userId);
        return userStorage.getFriendsOfUser(userId)
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public Collection<UserDto> findAllCommonFriends(long userId1, long userId2) {
        userStorage.throwIfNotExists(userId1);
        userStorage.throwIfNotExists(userId2);
        return userStorage.getCommonFriendsOfTwoUsers(userId1, userId2)
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }
}
