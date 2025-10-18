package ru.yandex.practicum.filmorate.service.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.event.EventDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.repository.feed.FeedRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventDto> getUserFeed(long userId) {
        userRepository.findById(userId)
                .orElseThrow(NotFoundException.supplier("User with id %d not found", userId));

        List<Event> events = feedRepository.findByUserId(userId);
        log.info("Retrieved {} events for user {}", events.size(), userId);
        
        return events.stream()
                .map(EventMapper::toEventDto)
                .toList();
    }

    @Override
    public void addEvent(EventType eventType, Operation operation, long userId, long entityId) {
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
        
        feedRepository.save(event);
        log.debug("Event saved: type={}, operation={}, userId={}, entityId={}", 
            eventType, operation, userId, entityId);
    }
}
