package ru.yandex.practicum.filmorate.model;

public record MPARating(Long id, String name) {
    public MPARating(Long id) {
        this(id, null);
    }
}
