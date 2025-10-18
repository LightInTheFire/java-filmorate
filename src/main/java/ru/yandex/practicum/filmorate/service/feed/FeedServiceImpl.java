package ru.yandex.practicum.filmorate.service.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
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
    public List<Event> getUserFeed(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found", userId));

        List<Event> events = feedRepository.findByUserId(userId);
        log.info("Retrieved {} events for user {}", events.size(), userId);
        return events;
    }

    @Override
    public void addEvent(Event event) {
        feedRepository.save(event);
        log.debug("Event saved: type={}, operation={}, userId={}, entityId={}",
                event.getEventType(), event.getOperation(), event.getUserId(), event.getEntityId());
    }
}