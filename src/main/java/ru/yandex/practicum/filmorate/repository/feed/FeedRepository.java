package ru.yandex.practicum.filmorate.repository.feed;

import ru.yandex.practicum.filmorate.model.Event;
import java.util.List;

public interface FeedRepository {
    List<Event> findByUserId(long userId);
    
    Event save(Event event);

}
