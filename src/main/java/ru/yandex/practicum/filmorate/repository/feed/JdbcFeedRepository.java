package ru.yandex.practicum.filmorate.repository.feed;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcFeedRepository implements FeedRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final EventRowMapper eventRowMapper;

    @Override
    public List<Event> findByUserId(long userId) {
        String sql = "SELECT * FROM feed_events WHERE user_id = :user_id ORDER BY timestamp ASC";
        return jdbc.query(sql, 
            new MapSqlParameterSource("user_id", userId), 
            eventRowMapper);
    }

    @Override
    public Event save(Event event) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("timestamp", event.getTimestamp())
            .addValue("user_id", event.getUserId())
            .addValue("event_type", event.getEventType().toString())
            .addValue("operation", event.getOperation().toString())
            .addValue("entity_id", event.getEntityId());
            
        String sql = """
            INSERT INTO feed_events (timestamp, user_id, event_type, operation, entity_id)
            VALUES (:timestamp, :user_id, :event_type, :operation, :entity_id)
            """;
            
        jdbc.update(sql, params, keyHolder, new String[]{"event_id"});
        event.setEventId(keyHolder.getKeyAs(Long.class));
        return event;
    }
}
