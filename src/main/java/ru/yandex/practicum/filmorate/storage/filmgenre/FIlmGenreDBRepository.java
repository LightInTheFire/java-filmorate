package ru.yandex.practicum.filmorate.storage.filmgenre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;

@Repository
public class FIlmGenreDBRepository extends BaseStorage<Long> implements FilmGenreStorage {
    private static final RowMapper<Long> GENRE_ID_MAPPER
            = ((rs, rowNum) -> rs.getLong("genre_id"));
    private static final String FIND_GENRE_IDS_BY_FILM_ID_QUERY = """
            SELECT genre_id FROM film_genres WHERE film_id = ?""";
    private static final String INSERT_GENRE_IDS_BY_FILM_ID_QUERY = """
            INSERT INTO film_genres (film_id, genre_id)  VALUES (?, ?)""";

    public FIlmGenreDBRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, GENRE_ID_MAPPER);
    }

    @Override
    public Collection<Long> getGenreIdsForFilm(long filmId) {
        return findManyQuery(FIND_GENRE_IDS_BY_FILM_ID_QUERY, filmId);
    }

    @Override
    public Collection<Long> insertGenreIdsForFilm(long filmId, Collection<Long> genreIds) {

        for (Long genreId : genreIds) {
            insertSimpleQuery(INSERT_GENRE_IDS_BY_FILM_ID_QUERY, filmId, genreId);
        }

        return genreIds;
    }
}
