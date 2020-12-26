DELETE
FROM user_roles;
DELETE
FROM menu_dishes;
DELETE
FROM users;
DELETE
FROM menus;
DELETE
FROM dishes;
DELETE
FROM restaurants;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@ya.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001),
       ('USER', 100001);

INSERT INTO RESTAURANTS(NAME)
VALUES ('McDonalds'),
       ('Burger King');

INSERT INTO dishes(name, price, restaurant_id)
VALUES ('Chicken', 253, 100002),
       ('Soup', 400, 100002),
       ('Turkey in pita', 350, 100002),
       ('Borscht', 333, 100002),
       ('Juice', 333, 100002),
       ('Pizza', 333, 100003),
       ('Beer', 333, 100003),
       ('BlackJack', 333, 100003),
       ('Courtesans', 333, 100003);

INSERT INTO menus(date, restaurant_id)
VALUES ('2020-12-10 00:00:00', 100002),
       ('2020-12-11 00:00:00', 100002),
       ('2020-12-12 00:00:00', 100002),
       ('2020-12-10 00:00:00', 100003),
       ('2020-12-11 00:00:00', 100003),
       ('2020-12-12 00:00:00', 100003);

INSERT INTO menu_dishes(menu_id, dish_id)
VALUES (100013, 100008),
       (100013, 100006),
       (100013, 100005),
       (100014, 100004),
       (100014, 100005),
       (100014, 100007),
       (100015, 100004),
       (100015, 100005),
       (100015, 100006),
       (100015, 100007),
       (100016, 100009),
       (100016, 100010),
       (100017, 100012),
       (100018, 100010),
       (100018, 100011),
       (100018, 100009);