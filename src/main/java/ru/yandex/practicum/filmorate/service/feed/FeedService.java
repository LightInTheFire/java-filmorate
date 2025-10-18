package ru.yandex.practicum.filmorate.service.feed;

import ru.yandex.practicum.filmorate.dto.event.EventDto;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.List;

public interface FeedService {
    List<EventDto> getUserFeed(long userId);

    void addEvent(EventType eventType, Operation operation, long userId, long entityId);
}
