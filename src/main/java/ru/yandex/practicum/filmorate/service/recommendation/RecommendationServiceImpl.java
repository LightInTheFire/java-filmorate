package ru.yandex.practicum.filmorate.service.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    @Override
    public Collection<FilmDto> findFilmRecommendations(long userId) {
        userRepository.findById(userId)
                .orElseThrow(NotFoundException.supplier("User with id %d not found", userId));

        Optional<Long> similarUserOpt = userRepository.findSimilarFilmTasteUser(userId);

        if (similarUserOpt.isEmpty()) {
            return Collections.emptyList();
        }

        long similarUserId = similarUserOpt.get();
        return filmRepository.findFilmRecommendations(userId, similarUserId)
                .stream()
                .map(FilmMapper::toFilmDto)
                .toList();
    }
}
