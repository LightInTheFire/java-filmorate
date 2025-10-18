package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.FilmsSortBy;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private static final String BASE_SELECT_SQL = """
            SELECT f.film_id,
                f.name,
                f.description,
                f.release_date,
                f.duration_in_minutes,
                f.mpa_id,
                mr.name                                                                                     AS mpa_name,
                GROUP_CONCAT(DISTINCT CONCAT(g.genre_id, ':', g.name) ORDER BY g.genre_id SEPARATOR ';')    AS genres,
                GROUP_CONCAT(DISTINCT CONCAT(d.director_id, ':', d.name)
                    ORDER BY d.director_id SEPARATOR ';') AS directors
                FROM films f
                         JOIN mpa_ratings mr ON f.mpa_id = mr.mpa_id
                         LEFT JOIN film_genres fg ON f.film_id = fg.film_id
                         LEFT JOIN genres g ON g.genre_id = fg.genre_id
                         LEFT JOIN film_directors fd on f.film_id = fd.film_id
                         LEFT JOIN directors d ON fd.director_id = d.director_id
            """;
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Film> filmRowMapper;

    @Override
    public Optional<Film> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String selectFilmByIdSql = BASE_SELECT_SQL.concat("""
                WHERE f.film_id = :id
                GROUP BY f.film_id""");

        List<Film> films = jdbc.query(selectFilmByIdSql, params, filmRowMapper);

        return films.isEmpty() ? Optional.empty() : Optional.of(films.getFirst());
    }

    @Override
    public Collection<Film> findAll() {
        String findAllSql = BASE_SELECT_SQL.concat("GROUP BY f.film_id");
        return jdbc.query(findAllSql, filmRowMapper);
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = prepareParamMap(film);

        String saveFilmSql = """
                INSERT INTO films(name, description, release_date, duration_in_minutes, mpa_id)
                VALUES (:name, :description, :release_date, :duration, :mpa_id)""";
        jdbc.update(saveFilmSql, params, keyHolder, new String[]{"film_id"});

        film.setId(keyHolder.getKeyAs(Long.class));

        saveGenres(film.getGenres(), film.getId());
        saveDirectors(film.getDirectors(), film.getId());
        return film;
    }

    @Override
    public void update(Film film) {
        String updateFilmSql = """
                UPDATE films
                SET name = :name, description = :description, release_date = :release_date,
                duration_in_minutes = :duration, mpa_id = :mpa_id
                WHERE film_id = :film_id""";
        MapSqlParameterSource params = prepareParamMap(film)
                .addValue("film_id", film.getId());
        jdbc.update(updateFilmSql, params);

        String deleteFilmGenresSql = """
                DELETE FROM film_genres WHERE film_id = :film_id""";
        MapSqlParameterSource filmIdParams = new MapSqlParameterSource()
                .addValue("film_id", film.getId());
        jdbc.update(deleteFilmGenresSql, filmIdParams);
        saveGenres(film.getGenres(), film.getId());

        String deleteFilmDirectorsSql = """
                DELETE FROM film_directors WHERE film_id = :film_id""";

        jdbc.update(deleteFilmDirectorsSql, filmIdParams);
        saveDirectors(film.getDirectors(), film.getId());
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        String deleteFilmByIdSql = "DELETE FROM films WHERE film_id = :id";

        jdbc.update(deleteFilmByIdSql, params);
    }

    @Override
    public Collection<Film> findTopPopularFilms(int count, Integer genreId, Integer year) {
        String selectTopFilmsSql = BASE_SELECT_SQL.concat("""
                LEFT JOIN likes fl ON f.film_id = fl.film_id
                WHERE (g.genre_id = :genreId OR :genreId IS NULL)
                AND (YEAR(f.release_date) =:year OR :year IS NULL)
                GROUP BY f.film_id
                ORDER BY COUNT(fl.user_id) DESC, f.film_id
                LIMIT :count""");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("count", count)
                .addValue("genreId", genreId)
                .addValue("year", year);

        return jdbc.query(selectTopFilmsSql, params, filmRowMapper);
    }

    @Override
    public Collection<Film> findCommonFilms(long userId, long friendId) {
        String selectCommonFilmsSql = BASE_SELECT_SQL.concat("""
                LEFT JOIN likes fl ON f.film_id = fl.film_id
                WHERE f.film_id in (SELECT l1.film_id
                                    FROM likes l1
                                    WHERE l1.user_id = :user_id
                                    INTERSECT
                                    SELECT l2.film_id
                                    FROM likes l2
                                    WHERE l2.user_id = :friend_id)
                GROUP BY f.film_id, g.genre_id
                ORDER BY COUNT(fl.user_id) DESC, f.film_id
                """);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);

        return jdbc.query(selectCommonFilmsSql, params, filmRowMapper);
    }

    @Override
    public Collection<Film> findFilmsOfDirector(long directorId, FilmsSortBy sortFilmsBy) {
        String sortBySql = switch (sortFilmsBy) {
            case YEAR -> "EXTRACT(YEAR from f.release_date)";
            case LIKES -> "COUNT(fl.user_id) DESC";
        };

        String selectFilmsOfDirectorSortedSql = BASE_SELECT_SQL.concat("""
                LEFT JOIN likes fl ON f.film_id = fl.film_id
                WHERE d.director_id = :director_id
                GROUP BY f.film_id, g.genre_id, f.release_date
                ORDER BY %s""".formatted(sortBySql)
        );
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("director_id", directorId);
        return jdbc.query(selectFilmsOfDirectorSortedSql, params, filmRowMapper);
    }

    @Override
    public Collection<Film> findFilmRecommendations(long userId, long similarUserId) {
        String sqlRecommendations = BASE_SELECT_SQL.concat("""
                LEFT JOIN likes l ON f.film_id = l.film_id
                WHERE l.user_id = :similarUserId
                  AND f.film_id NOT IN (
                      SELECT film_id FROM likes WHERE user_id = :userId
                  )
                GROUP BY f.film_id
                """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("similarUserId", similarUserId)
                .addValue("userId", userId);

        return jdbc.query(sqlRecommendations, params, filmRowMapper);
    }

    private void saveGenres(Collection<Genre> genres, long filmId) {
        String insertGenresSql = """
                INSERT INTO film_genres (film_id, genre_id)
                VALUES (:film_id, :genre_id)""";

        SqlParameterSource[] batchParams = genres.stream()
                .map(Genre::getId)
                .map(genreId -> new MapSqlParameterSource()
                        .addValue("film_id", filmId)
                        .addValue("genre_id", genreId))
                .toArray(SqlParameterSource[]::new);

        jdbc.batchUpdate(insertGenresSql, batchParams);
    }

    private void saveDirectors(Collection<Director> directors, long filmId) {
        String insertDirectorsSql = """
                INSERT INTO film_directors (film_id, director_id)
                VALUES (:film_id, :director_id)""";
        SqlParameterSource[] batchParams = directors.stream()
                .map(Director::getId)
                .map(directorId -> new MapSqlParameterSource()
                        .addValue("film_id", filmId)
                        .addValue("director_id", directorId))
                .toArray(SqlParameterSource[]::new);

        jdbc.batchUpdate(insertDirectorsSql, batchParams);
    }

    private MapSqlParameterSource prepareParamMap(Film film) {
        return new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpaRating().getId());
    }
}
