package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.event.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

@UtilityClass
public class EventMapper {
    public EventDto toEventDto(Event event) {
        return new EventDto(
            event.getTimestamp(),
            event.getUserId(),
            event.getEventType(),
            event.getOperation(),
            event.getEventId(),
            event.getEntityId()
        );
    }
}

