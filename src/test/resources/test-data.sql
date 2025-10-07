INSERT INTO users (email, login, name, birthday)
VALUES ('email1@dotcom', 'login1', 'name1', '2020-01-01'),
       ('email2@dotcom', 'login2', 'name2', '2020-01-01'),
       ('email3@dotcom', 'login3', 'name3', '2020-01-01');


INSERT INTO friendships (user_id1, user_id2)
VALUES (1, 3),
       (1, 2),
       (2, 3);