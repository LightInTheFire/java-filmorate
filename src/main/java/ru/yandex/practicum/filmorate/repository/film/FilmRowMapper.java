package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        MPARating mpaRating = MPARating.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();

        String genresString = rs.getString("genres");
        Set<Genre> genres;
        if (genresString != null && !genresString.isEmpty() && !genresString.startsWith(":")) {
            genres = Arrays.stream(genresString.split(";"))
                    .map(genreIdNameStr -> {
                        String[] splitIdNameStr = genreIdNameStr.split(":");
                        return Genre.builder()
                                .id(Long.valueOf(splitIdNameStr[0]))
                                .name(splitIdNameStr[1])
                                .build();
                    })
                    .collect(Collectors.toSet());
        } else {
            genres = Collections.emptySet();
        }

        String directorsString = rs.getString("directors");
        Set<Director> directors;
        if (directorsString != null && !directorsString.isEmpty() && !directorsString.startsWith(":")) {
            directors = Arrays.stream(directorsString.split(";"))
                    .map(directorIdNameStr -> {
                        String[] splitIdNameStr = directorIdNameStr.split(":");
                        return Director.builder()
                                .id(Long.valueOf(splitIdNameStr[0]))
                                .name(splitIdNameStr[1])
                                .build();
                    })
                    .collect(Collectors.toSet());
        } else {
            directors = Collections.emptySet();
        }

        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getObject("release_date", LocalDate.class))
                .duration(rs.getInt("duration_in_minutes"))
                .mpaRating(mpaRating)
                .genres(genres)
                .directors(directors)
                .build();
    }
}
