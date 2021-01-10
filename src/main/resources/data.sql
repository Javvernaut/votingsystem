ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (id, name, email, password)
VALUES (global_seq.nextval, 'User', 'user@ya.ru', '{noop}password'),
       (global_seq.nextval, 'Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001),
       ('USER', 100001);

INSERT INTO RESTAURANTS(id, name)
VALUES (global_seq.nextval, 'McDonalds'),
       (global_seq.nextval, 'Burger King');

INSERT INTO dishes(id, name, restaurant_id)
VALUES (global_seq.nextval, 'Chicken', 100002),
       (global_seq.nextval, 'Soup', 100002),
       (global_seq.nextval, 'Turkey in pita', 100002),
       (global_seq.nextval, 'Borscht', 100002),
       (global_seq.nextval, 'Juice', 100002),
       (global_seq.nextval, 'Pizza', 100003),
       (global_seq.nextval, 'Beer', 100003),
       (global_seq.nextval, 'BlackJack', 100003),
       (global_seq.nextval, 'Courtesans', 100003);

INSERT INTO menus(id, date, restaurant_id)
VALUES (global_seq.nextval, '2020-12-10 00:00:00', 100002),
       (global_seq.nextval, '2020-12-11 00:00:00', 100002),
       (global_seq.nextval, '2020-12-12 00:00:00', 100002),
       (global_seq.nextval, '2020-12-10 00:00:00', 100003),
       (global_seq.nextval, '2020-12-11 00:00:00', 100003),
       (global_seq.nextval, '2020-12-12 00:00:00', 100003);

INSERT INTO items(menu_id, dish_id, price)
VALUES (100013, 100008, 333),
       (100013, 100006, 444),
       (100013, 100005, 555),
       (100014, 100004, 11),
       (100014, 100005, 12),
       (100014, 100007, 13),
       (100015, 100004, 55),
       (100015, 100005, 66),
       (100015, 100006, 123),
       (100015, 100007, 321),
       (100016, 100009, 456),
       (100016, 100010, 654),
       (100017, 100012, 753),
       (100018, 100010, 325),
       (100018, 100011, 475),
       (100018, 100009, 325);