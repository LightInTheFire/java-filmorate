package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.event.EventDto;
import ru.yandex.practicum.filmorate.service.feed.FeedService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping("/{id}/feed")
    public List<EventDto> getUserFeed(@PathVariable long id) {
        log.info("GET /users/{}/feed", id);
        return feedService.getUserFeed(id);
    }
}
