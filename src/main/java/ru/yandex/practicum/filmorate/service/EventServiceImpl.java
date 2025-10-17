@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public List<EventDto> getUserFeed(long userId) {
        try {
            List<Event> events = eventRepository.findByUserId(userId);
            log.info("=== FEED DEBUG ===");
            log.info("User ID: {}", userId);
            log.info("Found {} events", events.size());
            for (Event event : events) {
                log.info("Event: id={}, type={}, operation={}, entity={}, timestamp={}", 
                    event.getEventId(), event.getEventType(), event.getOperation(), 
                    event.getEntityId(), event.getTimestamp());
            }
            log.info("=== END FEED DEBUG ===");
            
            return events.stream()
                    .map(EventMapper::toEventDto)
                    .toList();
        } catch (Exception e) {
            log.error("Error getting feed for user {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void addEvent(Event event) {
        try {
            log.info("=== ADD EVENT DEBUG ===");
            log.info("Saving event: userId={}, type={}, operation={}, entityId={}, timestamp={}",
                event.getUserId(), event.getEventType(), event.getOperation(),
                event.getEntityId(), event.getTimestamp());
            
            eventRepository.save(event);
            
            log.info("Event saved with ID: {}", event.getEventId());
            log.info("=== END ADD EVENT DEBUG ===");
        } catch (Exception e) {
            log.error("Error saving event: {}", e.getMessage(), e);
        }
    }
}
