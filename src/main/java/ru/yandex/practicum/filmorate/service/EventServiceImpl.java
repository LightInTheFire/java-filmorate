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
        try {
            log.info("=== GET USER FEED START ===");
            log.info("Looking for events for user ID: {}", userId);
            List<Event> events = eventRepository.findByUserId(userId);
            log.info("Found {} events in database", events.size());
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                log.info("Event {}: id={}, userId={}, type={}, operation={}, entityId={}, timestamp={}",
                    i, event.getEventId(), event.getUserId(), event.getEventType(),
                    event.getOperation(), event.getEntityId(), event.getTimestamp());
            }
            List<EventDto> eventDtos = events.stream()
                    .map(EventMapper::toEventDto)
                    .toList();
            log.info("Converted to {} EventDto objects", eventDtos.size());
            log.info("=== GET USER FEED END ===");
            return eventDtos;
        } catch (Exception e) {
            log.error("Error getting feed for user {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void addEvent(Event event) {
        try {
            log.info("=== ADD EVENT START ===");
            log.info("Attempting to save event: userId={}, type={}, operation={}, entityId={}, timestamp={}",
                event.getUserId(), event.getEventType(), event.getOperation(),
                event.getEntityId(), event.getTimestamp());
            Event savedEvent = eventRepository.save(event);
            log.info("Event saved successfully with ID: {}", savedEvent.getEventId());
            log.info("=== ADD EVENT END ===");
        } catch (Exception e) {
            log.error("=== ADD EVENT ERROR ===");
            log.error("Failed to save event: {}", e.getMessage(), e);
            log.error("=== ADD EVENT ERROR END ===");
        }
    }
}

