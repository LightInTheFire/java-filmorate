package ru.yandex.practicum.filmorate.dto.event;

import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

public record EventDto(Long timestamp,
                       Long userId,
                       EventType eventType,
                       Operation operation,
                       Long eventId,
                       Long entityId) {

}
