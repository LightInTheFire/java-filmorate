# Java-Filmorate

Repository for filmorate - social network for film rating. Written in java.

# Contributors
Search - [Alexander-skipper](https://github.com/Alexander-skipper)<br>
Feed - [n20va](https://github.com/n20va)<br>
Recommendations - [Basementdoor](https://github.com/basementdoor)<br>
Reviews, top popular films by year and genre - [Nikolay Aleksandrov](https://github.com/Alextrusk27)<br>

# DB schema

![db schema](assets/readme/db_schema.svg)

# Tables description
This section provides a detailed description of each table in the database schema.

* Events  
This table logs user activities such as likes, reviews, and friendships, serving as an audit or history trail for operations.
* Friendships  
This table stores user-to-user friendship relationships.
* Users  
This table holds user profile information for personalization.
* Likes  
This table records users' likes on films, for recommendation systems or popularity tracking.
* Film Reviews  
This table links users and films to their reviews, acting as a junction for many-to-many relationships.
* Reviews  
This table stores the content and metadata of user reviews.
* Review Likes  
This table tracks likes on reviews, similar to upvoting for helpfulness.
* Films  
This core table contains movie details for the platform's catalog.
* Film Directors  
This junction table associates films with their directors.
* Directors  
This table lists film directors.
* MPA Ratings  
This table defines Motion Picture Association ratings (e.g., G, PG, R).
* Film Genres  
This junction table links films to genres.
* Genres  
This table categorizes film genres (e.g., Action, Drama).

## Examples of database queries

### Get all films

```sql
SELECT *
FROM films;
```

### Get all films with mpa rating and genres concatenated as string

```sql
SELECT f.film_id,
       f.name,
       f.description,
       f.release_date,
       f.duration_in_minutes,
       m.name               AS mpa_rating,
       GROUP_CONCAT(g.name) AS genres
FROM films f
         JOIN mpa_ratings m ON f.mpa_id = m.mpa_id
         LEFT JOIN film_genres fg ON f.film_id = fg.film_id
         LEFT JOIN genres g ON fg.genre_id = g.genre_id
GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration_in_minutes, m.name
ORDER BY f.film_id;
```

### Get top 10 films by likes amount

```sql
SELECT f.film_id,
       f.name,
       f.description,
       f.release_date,
       f.duration_in_minutes,
       f.mpa_id
FROM films f
         LEFT JOIN likes fl ON f.film_id = fl.film_id
GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration_in_minutes
ORDER BY COUNT(fl.user_id) DESC, f.film_id 
LIMIT 10;
```

### Get all users

```sql
SELECT *
FROM users;
```

### Get all friends of a user with id 1

```sql
SELECT u.user_id,
       u.login,
       u.name,
       u.email
FROM users u
WHERE u.user_id IN (SELECT f.user_id2
                    FROM friendships f
                    WHERE f.user_id1 = 1)
ORDER BY u.name;
```

### Get all common friends between users with id 1 and 2

```sql
SELECT u.user_id,
       u.login,
       u.name,
       u.email
FROM users u
WHERE u.user_id IN (SELECT user_id2
                    FROM friendships
                    WHERE user_id1 = 1

                    INTERSECT

                    SELECT user_id2
                    FROM friendships
                    WHERE user_id1 = 2)
```
