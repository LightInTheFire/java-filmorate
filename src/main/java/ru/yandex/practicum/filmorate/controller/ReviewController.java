package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto create(@Valid @RequestBody NewReviewRequest request) {
        log.trace("Create review request: {}", request);
        return reviewService.create(request);
    }

    @PutMapping
    public ReviewDto update(@Valid @RequestBody UpdateReviewRequest request) {
        log.trace("Update review request: {}", request);
        return reviewService.update(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive long id) {
        log.trace("Delete review request by reviewId: {}", id);
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public ReviewDto findById(@PathVariable @Positive long id) {
        log.trace("Find review by reviewId: {}", id);
        return reviewService.findById(id);
    }

    @GetMapping
    public Collection<ReviewDto> findAllByFilm(@RequestParam(required = false) @Positive Long filmId,
                                               @RequestParam(defaultValue = "10") @Positive long count) {
        log.trace("Find all reviews by film Id: {}, count: {}", filmId, count);
        return reviewService.findAllByFilm(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLike(@PathVariable @Positive long id,
                        @PathVariable @Positive long userId) {
        log.trace("Set review like by reviewId: {}, from userId: {}", id, userId);
        reviewService.setLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void setDislike(@PathVariable @Positive long id,
                           @PathVariable @Positive long userId) {
        log.trace("Set review dislike by reviewId: {}, from userId: {}", id, userId);
        reviewService.setDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable @Positive long id,
                           @PathVariable @Positive long userId) {
        log.trace("Remove review like by reviewId: {}, from userId: {}", id, userId);
        reviewService.removeLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable @Positive long id,
                              @PathVariable @Positive long userId) {
        log.trace("Remove review dislike by reviewId: {}, from userId: {}", id, userId);
        reviewService.removeDislike(id, userId);
    }
}