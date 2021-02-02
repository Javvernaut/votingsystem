ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (id, name, email, password)
VALUES (global_seq.nextval, 'User', 'user@ya.ru', '{noop}password'),
       (global_seq.nextval, 'Admin', 'admin@gmail.com', '{noop}admin'),
       (global_seq.nextval, 'Alien', 'al@mail.ru', '{noop}alien');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001),
       ('USER', 100001),
       ('USER', 100002);

INSERT INTO RESTAURANTS(id, name)
VALUES (global_seq.nextval, 'McDonalds'),
       (global_seq.nextval, 'Burger King'),
       (global_seq.nextval, 'KFC');

INSERT INTO dishes(id, name, restaurant_id)
VALUES (global_seq.nextval, 'Chicken', 100003),
       (global_seq.nextval, 'Soup', 100003),
       (global_seq.nextval, 'Turkey in pita', 100003),
       (global_seq.nextval, 'Borscht', 100003),
       (global_seq.nextval, 'Juice', 100003),
       (global_seq.nextval, 'Pizza', 100004),
       (global_seq.nextval, 'Beer', 100004),
       (global_seq.nextval, 'BlackJack', 100004),
       (global_seq.nextval, 'Courtesans', 100004),
       (global_seq.nextval, 'Gingerbread with chili', 100005),
       (global_seq.nextval, 'Apple in ducks', 100005),
       (global_seq.nextval, 'Birch juice with pulp', 100005),
       (global_seq.nextval, 'Chicken in ice cream', 100005),
       (global_seq.nextval, 'Dumplings with hazelnuts', 100005),
       (global_seq.nextval, 'Puree with kotletka', 100005),
       (global_seq.nextval, 'Мыш кродеться', 100005);

INSERT INTO menus(id, date, restaurant_id)
VALUES (global_seq.nextval, '2020-12-10 00:00:00', 100003),
       (global_seq.nextval, '2020-12-11 00:00:00', 100003),
       (global_seq.nextval, '2020-12-12 00:00:00', 100003),
       (global_seq.nextval, '2020-12-10 00:00:00', 100004),
       (global_seq.nextval, '2020-12-11 00:00:00', 100004),
       (global_seq.nextval, '2020-12-12 00:00:00', 100004),
       (global_seq.nextval, '2020-12-09 00:00:00', 100005),
       (global_seq.nextval, '2020-12-10 00:00:00', 100005),
       (global_seq.nextval, '2020-12-12 00:00:00', 100005);

INSERT INTO items(menu_id, dish_id, price)
VALUES (100022, 100011, 333),
       (100022, 100009, 444),
       (100022, 100008, 555),
       (100023, 100007, 11),
       (100023, 100008, 12),
       (100023, 100010, 13),
       (100024, 100006, 55),
       (100024, 100007, 66),
       (100024, 100008, 123),
       (100024, 100009, 321),
       (100025, 100011, 456),
       (100025, 100012, 654),
       (100026, 100014, 753),
       (100027, 100012, 325),
       (100027, 100013, 475),
       (100027, 100010, 325),
       (100028, 100018, 220),
       (100028, 100020, 269),
       (100028, 100015, 900),
       (100028, 100016, 825),
       (100029, 100017, 880),
       (100029, 100016, 888),
       (100029, 100020, 801),
       (100029, 100018, 808),
       (100030, 100017, 330),
       (100030, 100018, 22),
       (100030, 100019, 77);

INSERT INTO votes(id, user_id, restaurant_id, date)
VALUES (global_seq.nextval, 100000, 100004, '2020-12-10'),
       (global_seq.nextval, 100000, 100003, '2020-12-11'),
       (global_seq.nextval, 100000, 100004, '2020-12-12'),
       (global_seq.nextval, 100000, 100005, '2020-12-09'),
       (global_seq.nextval, 100001, 100003, '2020-12-12'),
       (global_seq.nextval, 100001, 100005, '2020-12-10'),
       (global_seq.nextval, 100002, 100005, '2020-12-09'),
       (global_seq.nextval, 100002, 100004, '2020-12-10'),
       (global_seq.nextval, 100002, 100003, '2020-12-11'),
       (global_seq.nextval, 100002, 100005, '2020-12-12');