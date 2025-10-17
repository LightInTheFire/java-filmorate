package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.event.EventDto;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.event.EventRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public List<EventDto> getUserFeed(long userId) {
        List<Event> events = eventRepository.findByUserId(userId);
        log.trace("Retrieved {} events for user with id: {}", events.size(), userId);
        return events.stream()
                .map(EventMapper::toEventDto)
                .toList();
    }

    @Override
    public void addEvent(Event event) {
        eventRepository.save(event);
        log.debug("Event saved: {}", event);
    }
}

