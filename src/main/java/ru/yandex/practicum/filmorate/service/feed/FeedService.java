package ru.yandex.practicum.filmorate.service.feed;

import ru.yandex.practicum.filmorate.model.Event;
import java.util.List;

public interface FeedService {
    List<Event> getUserFeed(long userId);
    void addEvent(Event event);
}